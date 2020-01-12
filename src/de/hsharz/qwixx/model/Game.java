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

	public Game(IPlayer... player) {
		addPlayer(player);
		addDices();
	}

	public void addPlayer(IPlayer... player) {
		for (IPlayer p : player) {
			this.player.add(p);
		}
	}

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

	public void addClosedRow(DiceColor closedRow) {
		this.rowsToCloseAfterRoundFinished.add(closedRow);
	}

	public void stopGame() {
		this.isPlaying = false;
		if (gameThread != null) {
			this.gameThread.interrupt();
		}
	}

	public boolean isPlaying() {
		return isPlaying;
	}

	public List<IDice> getDices() {
		return dices;
	}

	public List<IPlayer> getPlayer() {
		return player;
	}

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

			while (isPlaying) {

				for (IPlayer currentPlayer : this.player) {

					System.out.println("\n\nSpieler ist an der Reihe: " + currentPlayer);
					gameListeners.forEach(l -> l.nextPlayersTurn(currentPlayer));

					rollDices();
					List<DicePair> whiteDices = getWhiteDicesSums();
					List<DicePair> colorDices = getColorDicesSums();

					System.out.println("---------- Other Player choosing dices");
					letOtherPlayerChooseWhiteDices(whiteDices, currentPlayer);

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

	private List<DicePair> getWhiteDicesSums() {
		int sum = diceWhite1.getCurrentValue() + diceWhite2.getCurrentValue();
		return Arrays.asList(new DicePair(DiceColor.RED, sum), //
				new DicePair(DiceColor.YELLOW, sum), //
				new DicePair(DiceColor.GREEN, sum), //
				new DicePair(DiceColor.BLUE, sum));
	}

	private List<DicePair> getColorDicesSums() {
		return Arrays.asList(new DicePair(DiceColor.RED, diceWhite1.getCurrentValue() + diceRed.getCurrentValue()),
				new DicePair(DiceColor.YELLOW, diceWhite1.getCurrentValue() + diceYellow.getCurrentValue()),
				new DicePair(DiceColor.GREEN, diceWhite1.getCurrentValue() + diceGreen.getCurrentValue()),
				new DicePair(DiceColor.BLUE, diceWhite1.getCurrentValue() + diceBlue.getCurrentValue()),

				new DicePair(DiceColor.RED, diceRed.getCurrentValue() + diceWhite2.getCurrentValue()),
				new DicePair(DiceColor.YELLOW, diceYellow.getCurrentValue() + diceWhite2.getCurrentValue()),
				new DicePair(DiceColor.GREEN, diceGreen.getCurrentValue() + diceWhite2.getCurrentValue()),
				new DicePair(DiceColor.BLUE, diceBlue.getCurrentValue() + diceWhite2.getCurrentValue()));
	}

	private void letOtherPlayerChooseWhiteDices(List<DicePair> whiteDices, IPlayer currentPlayer) {
		player.forEach(p -> {
			if (!p.equals(currentPlayer)) {
				letPlayerSelectWhiteDice(p, whiteDices);
			}
		});
	}

	private void letPlayerSelectDice(IPlayer player, List<DicePair> whiteDices, List<DicePair> colorDices) {

		DicePair selectedWhiteDice = letPlayerSelectWhiteDice(player, whiteDices);

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
		while (true) {
			try {
				DicePair selectedDice = diceSelection.apply(player, dices);
				if (!isEmptyOrMiss(selectedDice)) {
					// Den Wurf nur ankreuzen, falls ein Würfel ausgewählt wurde
					crossSelectedDice(player, selectedDice, dices);
				}
				return selectedDice;
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
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

	private void closeQueuedRows() {
		while (!rowsToCloseAfterRoundFinished.isEmpty()) {
			System.out.println("Row closed: " + rowsToCloseAfterRoundFinished.peek());
			closedRows.add(rowsToCloseAfterRoundFinished.poll());
		}
	}

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
