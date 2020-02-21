package de.hsharz.qwixx.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import de.hsharz.qwixx.model.Game;
import de.hsharz.qwixx.model.GameListener;
import de.hsharz.qwixx.model.dice.DiceColor;
import de.hsharz.qwixx.model.player.IPlayer;

public class ScoreDatabase implements GameListener {

	public static final ScoreDatabase INSTANCE = new ScoreDatabase();

	private Connection connection;

	public ScoreDatabase() {
		createDatabase();
	}

	private void createDatabase() {
		try {
			connection = DriverManager.getConnection("jdbc:hsqldb:dbs\\hsqldb_bsp", "sa", "");
			Statement statement = connection.createStatement();
//			statement.execute("DROP TABLE Score;");
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
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void nextPlayersTurn(IPlayer nextPlayer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void invalidDiceChoiceMade(IPlayer player, String msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void gameOver(Game game) {

		long gameID = System.currentTimeMillis();

		for (IPlayer player : game.getPlayer()) {
			try {
				PreparedStatement statement = connection.prepareStatement(
						"INSERT INTO Score (GameID, Username, Amount_Player, Player_Won, Aborted, Score_Red, Score_Yellow, Score_Green, Score_Blue, Score_Misses, Score_Complete) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
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
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

}
