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
	 * Das Spiel muss noch �ber die Methode {@link #startGame()} gestartet werden.
	 * 
	 * @param player Spieler, die diesem Spiel hinzugef�gt werden sollen
	 */
	public Game(IPlayer... player) {
		addPlayer(player);
		addDices();
	}

	/**
	 * F�gt diesem Spiel die gegebenen Spieler {@code player} hinzu.
	 * 
	 * @param player Spieler, die diesem Spiel hinzugef�gt werden sollen
	 */
	public void addPlayer(IPlayer... player) {
		for (IPlayer p : player) {
			this.player.add(p);
		}
	}

	/**
	 * F�gt diesem Spiel den gegebenen Spieler {@code player} hinzu.
	 * 
	 * @param player Spieler, der diesem Spiel hinzugef�gt werden soll
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
	 * Die Reihe der gegebenen Farbe {@code closedRow} der zu schlie�enden Reihen
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
	 *         l�uft
	 */
	public boolean isPlaying() {
		return isPlaying;
	}

	/**
	 * @return Liefert eine Liste mit allen W�rfeln, die in diesem Spiel vorhanden
	 *         sind
	 */
	public List<IDice> getDices() {
		return dices;
	}

	/**
	 * @return Liefert eine Liste mit allen zu diesem Spiel hinzugef�gten Spieler
	 */
	public List<IPlayer> getPlayer() {
		return player;
	}

	/**
	 * Startet das Spiel (in einem neuen Thread), falls es noch nicht gestartet ist
	 * und Spieler hinzugef�gt worden sind.
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

					// W�rfel w�rfeln
					rollDices();
					List<DicePair> whiteDices = getWhiteDicePairs(); // Aus den wei�en W�rfel W�rfelpaare bilden
					List<DicePair> colorDices = getColorDicePairs(); // Aus den Farbw�rfeln W�rfelpaare bilden

					// Andere Spieler w�hlen das wei�e W�rfelpaar aus
					System.out.println("---------- Other Player choosing dices");
					letOtherPlayerChooseWhiteDices(whiteDices, currentPlayer);

					// Der aktive Spieler w�hlt nun aus den wei�en und den Farbw�rfeln W�rfelpaare
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
	 * Aus den beiden wei�en W�rfeln wird die Summe berechnet und f�r jede Farbe ein
	 * DicePair mit der berechneten Summe erzeugt. <br>
	 * Da das wei�e W�rfelpaar f�r alle Farbreihen verwendet werden kann, muss auch
	 * f�r jede Farbe ein W�rfelpaar erstellt werden.
	 * 
	 * @return W�rfelpaare von jeder Farbe mit der Summe der beiden wei�en W�rfeln
	 */
	private List<DicePair> getWhiteDicePairs() {

		int sum = diceWhite1.getCurrentValue() + diceWhite2.getCurrentValue();
		return Arrays.asList(new DicePair(DiceColor.RED, sum), //
				new DicePair(DiceColor.YELLOW, sum), //
				new DicePair(DiceColor.GREEN, sum), //
				new DicePair(DiceColor.BLUE, sum));
	}

	/**
	 * Jeder wei�e W�rfel wird mit jedem Farbw�rfel kombiniert. Daraus ergeben sich
	 * (Anzahl der wei�en W�rfel) * (Anzahl der Farbw�rfel) verschiedene
	 * W�rfelpaare.
	 * 
	 * @return W�rfelpaare kombiniert aus den wei�en W�rfeln mit Farbw�rfeln
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
	 * sind, w�hlen aus den gegebenen W�rfeln {@code whiteDices} ein wei�es
	 * W�rfelpaar aus.
	 * 
	 * @param whiteDices    Wei�e W�rfelpaare, aus denen alle Spieler ein W�rfelpaar
	 *                      ausw�hlen k�nnen
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
		// Farbw�rfel mehr w�hlen d�rfen.
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

		// Pr�fen, ob der Player 2x keinen W�rfel ausgew�hlt hat bzw.
		// ob der Spieler beim ersten Mal keinen W�rfel gew�hlt hat und beim zweiten Mal
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
		// Der Spieler w�hlt so lange einen W�rfel aus den gegebenen W�rfel aus, bis er
		// einen g�ltigen W�rfel ausgew�hlt hat.
		while (true) {
			try {
				// Spieler w�hlt aus den gegebenen W�rfelpaaren ein W�rfelpaar aus
				DicePair selectedDice = diceSelection.apply(player, dices);
				if (!isEmptyOrMiss(selectedDice)) {
					// Den Wurf nur ankreuzen, falls ein W�rfel ausgew�hlt wurde
					crossSelectedDice(player, selectedDice, dices);
				}

				// Wenn bis hierher keine Fehlermeldung kam, dann hat der Spieler ein g�ltiges
				// W�rfelpaar ausgew�hlt, dieses W�rfelpaar wird zur�ckgegeben.
				return selectedDice;
			} catch (IllegalArgumentException e) {
				System.out
						.println("Der Spieler " + player + " hat keinen g�ltigen W�rfel ausgew�hlt: " + e.getMessage());
				gameListeners.forEach(l -> l.invalidDiceChoiceMade(player, e.getMessage()));
			}
		}
	}

	private void crossSelectedDice(IPlayer player, DicePair selectedDice, List<DicePair> dices) {
		if (!isEmptyOrMiss(selectedDice) && !dices.contains(selectedDice)) {
			throw new IllegalArgumentException(
					"Diese W�rfelsumme wurde nicht geworfen: " + selectedDice + " [" + dices + "]");
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
	 * Schlie�t alle zur Schlie�ung eingereihte Reihen in der
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
	 *         haben (die die h�chste Punktzahl erreicht haben). Dies k�nnen unter
	 *         Umst�nden mehrere Spieler sein.
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
