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
import java.util.stream.Collectors;

import de.hsharz.qwixx.model.board.row.RowsClosedSupplier;
import de.hsharz.qwixx.model.dice.Dice;
import de.hsharz.qwixx.model.dice.DiceColor;
import de.hsharz.qwixx.model.dice.DicesSum;
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
					List<DicesSum> whiteDices = getWhiteDicesSums();
					List<DicesSum> colorDices = getColorDicesSums();

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

	private List<DicesSum> getWhiteDicesSums() {
		int sum = diceWhite1.getCurrentValue() + diceWhite2.getCurrentValue();
		return Arrays.asList(DicesSum.EMPTY, //
				new DicesSum(DiceColor.RED, sum), //
				new DicesSum(DiceColor.YELLOW, sum), //
				new DicesSum(DiceColor.GREEN, sum), //
				new DicesSum(DiceColor.BLUE, sum));
	}

	private List<DicesSum> getColorDicesSums() {
		return Arrays.asList(DicesSum.EMPTY,
				new DicesSum(DiceColor.RED, diceWhite1.getCurrentValue() + diceRed.getCurrentValue()),
				new DicesSum(DiceColor.YELLOW, diceWhite1.getCurrentValue() + diceYellow.getCurrentValue()),
				new DicesSum(DiceColor.GREEN, diceWhite1.getCurrentValue() + diceGreen.getCurrentValue()),
				new DicesSum(DiceColor.BLUE, diceWhite1.getCurrentValue() + diceBlue.getCurrentValue()),

				new DicesSum(DiceColor.RED, diceRed.getCurrentValue() + diceWhite2.getCurrentValue()),
				new DicesSum(DiceColor.YELLOW, diceYellow.getCurrentValue() + diceWhite2.getCurrentValue()),
				new DicesSum(DiceColor.GREEN, diceGreen.getCurrentValue() + diceWhite2.getCurrentValue()),
				new DicesSum(DiceColor.BLUE, diceBlue.getCurrentValue() + diceWhite2.getCurrentValue()));
	}

	private void letOtherPlayerChooseWhiteDices(List<DicesSum> whiteDices, IPlayer currentPlayer) {
		player.forEach(p -> {
			if (!p.equals(currentPlayer)) {

				boolean selectedDicesValid = false;
				while (!selectedDicesValid) {
					try {
						DicesSum selectedWhiteDice = p.chooseWhiteDices(whiteDices);
						crossSelectedDice(p, selectedWhiteDice, whiteDices);
						selectedDicesValid = true;
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
						gameListeners.forEach(l -> l.invalidDiceChoiceMade(p, e.getMessage()));
					}
				}
			}
		});
	}

	private void letPlayerSelectDice(IPlayer player, List<DicesSum> whiteDices, List<DicesSum> colorDices) {

		boolean selectedDicesValid = false;
		while (!selectedDicesValid) {

			System.out.println("---------- Current Player choosing dices");

			try {
				DicesSum selectedWhiteDice = player.chooseWhiteDices(whiteDices);
				crossSelectedDice(player, selectedWhiteDice, whiteDices);

				closeQueuedRows();
				if (isGameOver()) {
					isPlaying = false;
					return;
				}

				DicesSum selectedColorDice = player.chooseColorDices(colorDices);
				crossSelectedDice(player, selectedColorDice, colorDices);

				// Check if player selected any dice
				if (isEmptyDice(selectedWhiteDice) && isEmptyDice(selectedColorDice)) {
					// player did not select any dice, cross miss
					player.getGameBoard().crossMiss();
				}

				selectedDicesValid = true;
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				gameListeners.forEach(l -> l.invalidDiceChoiceMade(player, e.getMessage()));
			}
		}

	}

	private void crossSelectedDice(IPlayer player, DicesSum selectedDice, List<DicesSum> dices) {
		if (isEmptyDice(selectedDice)) {
			return;
		}

		if (!dices.contains(selectedDice)) {
			throw new IllegalArgumentException(
					"Diese Würfelsumme wurde nicht geworfen: " + selectedDice + " [" + dices + "]");
		}

		player.getGameBoard().crossField(selectedDice.getColor(), selectedDice.getSum());
	}

	private boolean isEmptyDice(DicesSum sum) {
		return sum == null || sum.equals(DicesSum.EMPTY);
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
