package de.hsharz.qwixx.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import de.hsharz.qwixx.database.function.SqlConsumer;
import de.hsharz.qwixx.database.function.SqlFunction;
import de.hsharz.qwixx.model.Game;
import de.hsharz.qwixx.model.GameListener;
import de.hsharz.qwixx.model.dice.DiceColor;
import de.hsharz.qwixx.model.player.Human;
import de.hsharz.qwixx.model.player.IPlayer;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ScoreDatabase implements GameListener {

	public static final ScoreDatabase INSTANCE = new ScoreDatabase();

	public ScoreDatabase() {
		try {
			createDatabase();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void createDatabase() throws SQLException {
		runWithConnection(conn -> {
			Statement statement = conn.createStatement();
			statement.executeQuery("CREATE TABLE IF NOT EXISTS Score (" //
					+ "Index INTEGER IDENTITY PRIMARY KEY" //
					+ ", GameID VARCHAR(30) NOT NULL" //
					+ ", Username VARCHAR(100) NOT NULL" //
					+ ", Amount_Player INTEGER NOT NULL" //
					+ ", Player_Won BOOLEAN NOT NULL" //
					+ ", Aborted BOOLEAN NOT NULL" //
					+ ", Score_Red INTEGER NOT NULL" //
					+ ", Score_Yellow INTEGER NOT NULL" //
					+ ", Score_Green INTEGER NOT NULL" //
					+ ", Score_Blue INTEGER NOT NULL" //
					+ ", Score_Misses INTEGER NOT NULL" //
					+ ", Score_Complete INTEGER NOT NULL" //
					+ ");" //
			);
			conn.commit();
		});
	}

	public static List<PlayerScore> getScoresOfGameWithAmountPlayer(int amountPlayer) throws SQLException {
		return runWithConnection(conn -> {
			List<PlayerScore> scores = new ArrayList<>();

			PreparedStatement stmt = conn.prepareStatement( //
					"SELECT * FROM Score " //
							+ "WHERE Amount_Player = ? " //
							+ "ORDER BY Score_Complete DESC");
			stmt.setInt(1, amountPlayer);
			ResultSet result = stmt.executeQuery();

			while (result.next()) {
				String name = result.getString("Username");
				Integer score = result.getInt("Score_Complete");
				PlayerScore playerScore = new PlayerScore(name, score.intValue());
				scores.add(playerScore);
			}

			return scores;
		});
	}

	@Override
	public void gameOver(Game game) {
		try {
			runWithConnectionRollback(conn -> {
				long gameID = System.currentTimeMillis();

				for (IPlayer player : game.getPlayer()) {
					if (player instanceof Human) {
						System.out.println("Insert into database...");
						try {
							PreparedStatement statement = conn.prepareStatement(
									"INSERT INTO Score (GameID, Username, Amount_Player, Player_Won, Aborted, "
											+ "Score_Red, Score_Yellow, Score_Green, Score_Blue, Score_Misses, Score_Complete) "
											+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

							statement.setString(1, "qwixx#" + gameID); // GameID
							statement.setString(2, player.getName()); // Player Name
							statement.setInt(3, game.getPlayer().size()); // Amount Player
							statement.setBoolean(4, game.getWinningPlayer().contains(player)); // Player_Won
							statement.setBoolean(5, false); //

							statement.setInt(6, player.getGameBoard().getScore().getScoreOfRow(DiceColor.RED)); //
							statement.setInt(7, player.getGameBoard().getScore().getScoreOfRow(DiceColor.YELLOW)); //
							statement.setInt(8, player.getGameBoard().getScore().getScoreOfRow(DiceColor.GREEN)); //
							statement.setInt(9, player.getGameBoard().getScore().getScoreOfRow(DiceColor.BLUE)); //
							statement.setInt(10, player.getGameBoard().getScore().getScoreMisses()); //
							statement.setInt(11, player.getGameBoard().getScore().getScoreComplete()); //

							statement.executeUpdate();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
			});
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Führt die gegebene Funktion aus und übergibt dieser eine Verbindung zur
	 * Datenbank
	 * 
	 * @param function Funktion, die mit einer Datenbankverbindung ausgeführt werden
	 *                 soll
	 * @param <R>      Rückgabedatentyp der gegebenen Funktion
	 * @return Rückgabewert der gegebenen Function
	 * @throws SQLException Fehler bei der Ausführung
	 */
	private static <R> R runWithConnection(SqlFunction<Connection, R> function) throws SQLException {
		try (Connection conn = getConnection()) {
			return function.apply(conn);
		}
	}

	/**
	 * Führt den gegebenen Consumer aus und übergibt diesem eine Verbindung zur
	 * Datenbank
	 * 
	 * @param consumer Consumer, der mit einer Datenbankverbindung ausgeführt werden
	 *                 soll
	 * @throws SQLException Fehler bei der Ausführung
	 */
	private static void runWithConnection(SqlConsumer<Connection> consumer) throws SQLException {
		try (Connection conn = getConnection()) {
			consumer.accept(conn);
		}
	}

	/**
	 * Führt die gegebene Funktion mit einer Datenbankverbindung aus. Bei der in die
	 * gegebene Funktion übergebene Connection ist AutoCommit deaktiviert. Der
	 * Commit erfolgt nach der Ausführung der Funktion. Falls während der Ausführung
	 * der Funktion etwas schief läuft, so wird ein Rollback durchgeführt. Am Ende
	 * wird die Connection geschlossen und der von der Funktion zurückgegebene Wert
	 * zurückgegeben.
	 * 
	 * @param query Funktion, die mit einer Connection (mit ggf. Rollback bei
	 *              Fehlern) ausgeführt werden soll
	 * @param <R>   Rückgabedatentyp der gegebenen Funktion
	 * @return Rückgabewert der gegebenen Funktion
	 * @throws SQLException Fehler beim Verbinden zu der Datenbank
	 */
	private static <R> R runWithConnectionRollback(SqlFunction<Connection, R> query) throws SQLException {
		Connection conn = getConnection();
		R functionReturn = null;
		try {
			conn.setAutoCommit(false);
			functionReturn = query.apply(conn);
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			DatabaseConnectionUtils.close(conn);
		} finally {
			DatabaseConnectionUtils.rollback(conn);
		}
		return functionReturn;
	}

	/**
	 * Führt den gegebenen Consumer mit einer Datenbankverbindung aus. Bei der in
	 * den gegebenen Consumer übergebene Connection ist AutoCommit deaktiviert. Der
	 * Commit erfolgt nach der Ausführung des Consumers. Falls während der
	 * Ausführung des COnsumers etwas schief läuft, so wird ein Rollback
	 * durchgeführt. Am Ende wird die Connection geschlossen.
	 *
	 * @param query Consumer, der mit einer Connection (mit ggf. Rollback bei
	 *              Fehlern) ausgeführt werden soll
	 * @throws SQLException Fehler beim Verbinden zu der Datenbank
	 */
	private static void runWithConnectionRollback(SqlConsumer<Connection> query) throws SQLException {
		Connection conn = getConnection();
		try {
			conn.setAutoCommit(false);
			query.accept(conn);
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			DatabaseConnectionUtils.close(conn);
		} finally {
			DatabaseConnectionUtils.rollback(conn);
		}
	}

	/**
	 * Liefert eine Datenbankverbindung, auf der Operationen wie
	 * {@link Connection#prepareStatement} ausgeführt werden können
	 * 
	 * @return Connection zur Datenbank
	 * @throws SQLException Fehler bei der Verbindung zur Datenbank
	 */
	private static Connection getConnection() throws SQLException {
		return DatabaseConnection.getConnection();
	}

	public static class PlayerScore extends RecursiveTreeObject<PlayerScore> {

		private String name;
		private int score;

		public PlayerScore(String name, int score) {
			this.name = name;
			this.score = score;
		}

		public StringProperty getName() {
			return new SimpleStringProperty(name);
		}

		public StringProperty getScore() {
			return new SimpleStringProperty(String.valueOf(score));
		}

	}

}
