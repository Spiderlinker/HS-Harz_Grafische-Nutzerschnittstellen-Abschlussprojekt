package de.hsharz.qwixx.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.OptionalInt;
import java.util.Queue;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import de.hsharz.qwixx.model.board.row.RowsClosedSupplier;
import de.hsharz.qwixx.model.dice.Dice;
import de.hsharz.qwixx.model.dice.DiceColor;
import de.hsharz.qwixx.model.dice.DicePair;
import de.hsharz.qwixx.model.dice.IDice;
import de.hsharz.qwixx.model.player.IPlayer;
import de.hsharz.qwixx.ui.game.dice.DiceListener;

public class Game implements RowsClosedSupplier {

	private List<IPlayer> player = new LinkedList<>();

	private List<IDice> dices = new ArrayList<>();
	private IDice diceWhite1 = new Dice(DiceColor.WHITE);
	private IDice diceWhite2 = new Dice(DiceColor.WHITE);

	private IDice diceRed = new Dice(DiceColor.RED);
	private IDice diceYellow = new Dice(DiceColor.YELLOW);
	private IDice diceGreen = new Dice(DiceColor.GREEN);
	private IDice diceBlue = new Dice(DiceColor.BLUE);

	private Set<DiceColor> closedRows = new HashSet<>();
	private Queue<DiceColor> rowsToCloseAfterRoundFinished = new ArrayDeque<>();

	private List<DiceListener> diceListeners = new ArrayList<>();
	private List<GameListener> gameListeners = new ArrayList<>();

	private Thread gameThread;
	private boolean isPlaying;

	/**
	 * Erzeugt ein neues Spiel mit den gegebenen Spielern {@code player}. <br>
	 * Das Spiel muss noch über die Methode {@link #startGame()} gestartet werden.
	 * 
	 * @param player Spieler, die diesem Spiel hinzugefügt werden sollen
	 */
	public Game(IPlayer... player) {
		addPlayer(player);
		addDices();
	}

	/**
	 * Fügt diesem Spiel die gegebenen Spieler {@code player} hinzu.
	 * 
	 * @param player Spieler, die diesem Spiel hinzugefügt werden sollen
	 */
	public void addPlayer(IPlayer... player) {
		for (IPlayer p : player) {
			this.player.add(p);
		}
	}

	/**
	 * Fügt diesem Spiel den gegebenen Spieler {@code player} hinzu.
	 * 
	 * @param player Spieler, der diesem Spiel hinzugefügt werden soll
	 */
	public void removePlayer(IPlayer... player) {
		for (IPlayer p : player) {
			this.player.remove(p);
		}
	}

	public void addDiceListener(DiceListener l) {
		this.diceListeners.add(l);
	}

	public void addGameListener(GameListener l) {
		this.gameListeners.add(l);
	}

	private void addDices() {
		dices.add(diceWhite1);
		dices.add(diceWhite2);
		dices.add(diceRed);
		dices.add(diceYellow);
		dices.add(diceGreen);
		dices.add(diceBlue);
	}

	/**
	 * Die Reihe der gegebenen Farbe {@code closedRow} der zu schließenden Reihen
	 * {@link #rowsToCloseAfterRoundFinished} -Queue hinzu. Alle Reihen werden nach
	 * Beendigung des aktuellen Spielzuges geschlossen.
	 * 
	 * @param closedRow Spielreihe, die am Ende des aktuellen Spielzuges geschlossen
	 *                  werden soll
	 */
	public void addClosedRow(DiceColor closedRow) {
		this.rowsToCloseAfterRoundFinished.add(closedRow);
	}

	/**
	 * Stoppt dieses Spiel
	 */
	public void stopGame() {
		this.isPlaying = false;
		if (gameThread != null) {
			this.gameThread.interrupt();
		}
	}

	/**
	 * @return Liefert einen {@link Boolean} der angibt, ob dieses Spiel aktuell
	 *         läuft
	 */
	public boolean isPlaying() {
		return isPlaying;
	}

	/**
	 * @return Liefert eine Liste mit allen Würfeln, die in diesem Spiel vorhanden
	 *         sind
	 */
	public List<IDice> getDices() {
		return dices;
	}

	/**
	 * @return Liefert eine Liste mit allen zu diesem Spiel hinzugefügten Spieler
	 */
	public List<IPlayer> getPlayer() {
		return player;
	}

	/**
	 * Startet das Spiel (in einem neuen Thread), falls es noch nicht gestartet ist
	 * und Spieler hinzugefügt worden sind.
	 */
	public void startGame() {
		if (player.isEmpty()) {
			throw new IllegalArgumentException("There are no players to play with");
		}

		if (isPlaying) {
			System.out.println("Game already running...");
			return;
		}

		createGameThread();
		gameThread.start();
	}

	private void createGameThread() {
		gameThread = new Thread(() -> {
			isPlaying = true;

			// Spieleschleife
			while (isPlaying) {

				// Jeder Spieler kommt pro Durchgang 1x dran
				for (IPlayer currentPlayer : this.player) {

					System.out.println("\n\nSpieler ist an der Reihe: " + currentPlayer);
					gameListeners.forEach(l -> l.nextPlayersTurn(currentPlayer));

					// Würfel würfeln
					rollDices();
					List<DicePair> whiteDices = getWhiteDicePairs(); // Aus den weißen Würfel Würfelpaare bilden
					List<DicePair> colorDices = getColorDicePairs(); // Aus den Farbwürfeln Würfelpaare bilden

					// Andere Spieler wählen das weiße Würfelpaar aus
					System.out.println("---------- Other Player choosing dices");
					letOtherPlayerChooseWhiteDices(whiteDices, currentPlayer);

					// Der aktive Spieler wählt nun aus den weißen und den Farbwürfeln Würfelpaare
					// aus
					System.out.println("---------- Current Player choosing dices");
					letPlayerSelectDice(currentPlayer, whiteDices, colorDices);

					closeQueuedRows();
					if (isGameOver()) {
						isPlaying = false;
						break;
					}
				}
			}

			System.out.println("Game over");
			gameListeners.forEach(GameListener::gameOver);
		});
	}

	private void rollDices() {
		dices.forEach(IDice::rollDice);
		dices.forEach(System.out::println);
		diceListeners.forEach(DiceListener::dicesRolled);
	}

	/**
	 * Aus den beiden weißen Würfeln wird die Summe berechnet und für jede Farbe ein
	 * DicePair mit der berechneten Summe erzeugt. <br>
	 * Da das weiße Würfelpaar für alle Farbreihen verwendet werden kann, muss auch
	 * für jede Farbe ein Würfelpaar erstellt werden.
	 * 
	 * @return Würfelpaare von jeder Farbe mit der Summe der beiden weißen Würfeln
	 */
	private List<DicePair> getWhiteDicePairs() {

		int sum = diceWhite1.getCurrentValue() + diceWhite2.getCurrentValue();
		return Arrays.asList(new DicePair(DiceColor.RED, sum), //
				new DicePair(DiceColor.YELLOW, sum), //
				new DicePair(DiceColor.GREEN, sum), //
				new DicePair(DiceColor.BLUE, sum));
	}

	/**
	 * Jeder weiße Würfel wird mit jedem Farbwürfel kombiniert. Daraus ergeben sich
	 * (Anzahl der weißen Würfel) * (Anzahl der Farbwürfel) verschiedene
	 * Würfelpaare.
	 * 
	 * @return Würfelpaare kombiniert aus den weißen Würfeln mit Farbwürfeln
	 */
	private List<DicePair> getColorDicePairs() {
		return Arrays.asList(new DicePair(diceWhite1, diceRed), //
				new DicePair(diceWhite1, diceYellow), //
				new DicePair(diceWhite1, diceGreen), //
				new DicePair(diceWhite1, diceBlue), //

				new DicePair(diceWhite2, diceRed), //
				new DicePair(diceWhite2, diceYellow), //
				new DicePair(diceWhite2, diceGreen), //
				new DicePair(diceWhite2, diceBlue)); //
	}

	/**
	 * Alle Spieler, die aktuell nicht der aktive Spieler {@code currentPlayer}
	 * sind, wählen aus den gegebenen Würfeln {@code whiteDices} ein weißes
	 * Würfelpaar aus.
	 * 
	 * @param whiteDices    Weiße Würfelpaare, aus denen alle Spieler ein Würfelpaar
	 *                      auswählen können
	 * @param currentPlayer Der aktive Spieler dieser Runde
	 */
	private void letOtherPlayerChooseWhiteDices(List<DicePair> whiteDices, IPlayer currentPlayer) {
		player.forEach(p -> {
			if (!p.equals(currentPlayer)) {
				letPlayerSelectWhiteDice(p, whiteDices);
			}
		});
	}

	private void letPlayerSelectDice(IPlayer player, List<DicePair> whiteDices, List<DicePair> colorDices) {

		DicePair selectedWhiteDice = letPlayerSelectWhiteDice(player, whiteDices);

		// Falls der Benutzer einen Fehlwurf angekreuzt hat, so soll der keinen
		// Farbwürfel mehr wählen dürfen.
		if (DicePair.MISS.equals(selectedWhiteDice)) {
			// User crossed MissField
			// prevent user to cross second dice
			player.getGameBoard().crossMiss();
			return;
		}

		closeQueuedRows();
		if (isGameOver()) {
			isPlaying = false;
			return;
		}

		DicePair selectedColorDice = letPlayerSelectColorDice(player, colorDices);

		// Prüfen, ob der Player 2x keinen Würfel ausgewählt hat bzw.
		// ob der Spieler beim ersten Mal keinen Würfel gewählt hat und beim zweiten Mal
		// dann ein Fehlwurf angekreuzt hat
		// -> Dies ist gleichbedeutend mit einem Fehlwurf
		if (isEmpty(selectedWhiteDice) && isEmptyOrMiss(selectedColorDice)) {
			// Fehlwurf ankreuzen
			player.getGameBoard().crossMiss();
		}
	}

	private DicePair letPlayerSelectWhiteDice(IPlayer player, List<DicePair> whiteDices) {
		return letPlayerSelectDice((p, d) -> p.chooseWhiteDices(d), player, whiteDices);
	}

	private DicePair letPlayerSelectColorDice(IPlayer player, List<DicePair> colorDices) {
		return letPlayerSelectDice((p, d) -> p.chooseColorDices(d), player, colorDices);
	}

	private DicePair letPlayerSelectDice(BiFunction<IPlayer, List<DicePair>, DicePair> diceSelection, IPlayer player,
			List<DicePair> dices) {
		// Der Spieler wählt so lange einen Würfel aus den gegebenen Würfel aus, bis er
		// einen gültigen Würfel ausgewählt hat.
		while (true) {
			try {
				// Spieler wählt aus den gegebenen Würfelpaaren ein Würfelpaar aus
				DicePair selectedDice = diceSelection.apply(player, dices);
				if (!isEmptyOrMiss(selectedDice)) {
					// Den Wurf nur ankreuzen, falls ein Würfel ausgewählt wurde
					crossSelectedDice(player, selectedDice, dices);
				}

				// Wenn bis hierher keine Fehlermeldung kam, dann hat der Spieler ein gültiges
				// Würfelpaar ausgewählt, dieses Würfelpaar wird zurückgegeben.
				return selectedDice;
			} catch (IllegalArgumentException e) {
				System.out
						.println("Der Spieler " + player + " hat keinen gültigen Würfel ausgewählt: " + e.getMessage());
				gameListeners.forEach(l -> l.invalidDiceChoiceMade(player, e.getMessage()));
			}
		}
	}

	private void crossSelectedDice(IPlayer player, DicePair selectedDice, List<DicePair> dices) {
		if (!isEmptyOrMiss(selectedDice) && !dices.contains(selectedDice)) {
			throw new IllegalArgumentException(
					"Diese Würfelsumme wurde nicht geworfen: " + selectedDice + " [" + dices + "]");
		}

		player.getGameBoard().crossField(selectedDice);
	}

	private boolean isEmptyOrMiss(DicePair dice) {
		return isEmpty(dice) || dice.equals(DicePair.MISS);
	}

	private boolean isEmpty(DicePair dice) {
		return dice == null || dice.equals(DicePair.EMPTY);
	}

	/**
	 * Schließt alle zur Schließung eingereihte Reihen in der
	 * {@link #rowsToCloseAfterRoundFinished}-Queue
	 */
	private void closeQueuedRows() {
		while (!rowsToCloseAfterRoundFinished.isEmpty()) {
			System.out.println("Row is now closed: " + rowsToCloseAfterRoundFinished.peek());
			closedRows.add(rowsToCloseAfterRoundFinished.poll());
		}
	}

	/**
	 * @return Liefert einen Boolean der angibt, ob das aktuelle Spiel beendet ist
	 */
	private boolean isGameOver() {
		return !isPlaying || playerCrossedAllMisses() || closedRows.size() >= 2;
	}

	private boolean playerCrossedAllMisses() {
		for (IPlayer p : player) {
			if (p.getGameBoard().getRemainingMisses() <= 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return Liefert eine Liste mit allen Spielern, die diese Spielrunde gewonnen
	 *         haben (die die höchste Punktzahl erreicht haben). Dies können unter
	 *         Umständen mehrere Spieler sein.
	 */
	public List<IPlayer> getWinningPlayer() {
		OptionalInt maxScore = player.stream() //
				.mapToInt(p -> p.getGameBoard().getScore().getScoreComplete()) //
				.max();

		if (maxScore.isPresent()) {
			// Filter for all players having same max score
			return player.stream() //
					.filter(p -> maxScore.getAsInt() == p.getGameBoard().getScore().getScoreComplete()) //
					.collect(Collectors.toList());
		}
		// No max score available -> no players in match, no winning players
		return new ArrayList<>();
	}

	@Override
	public void closeRow(DiceColor color) {
		System.out.println("Closing row after round finished: " + color);
		rowsToCloseAfterRoundFinished.add(color);
	}

	@Override
	public boolean isRowClosed(DiceColor rowColor) {
		return closedRows.contains(rowColor);
	}

}
