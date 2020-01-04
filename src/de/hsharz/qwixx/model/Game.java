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
import de.hsharz.qwixx.model.dice.pair.Pair;
import de.hsharz.qwixx.model.player.IPlayer;
import de.hsharz.qwixx.ui.game.GameListener;
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
					List<DicesSum> dicesSums = getDicesSums();

					letOtherPlayerChooseWhiteDices(getWhiteDicesSums(), currentPlayer);

					System.out.println("Let Player choose 1 - 2 Dices");
					Pair<DicesSum> selectedDices = currentPlayer.chooseDices(dicesSums, 1, 2);

					// Check if player selected any dice
					if (selectedDices.isEmpty()) {
						// player did not select any dice, cross miss
						currentPlayer.getGameBoard().crossMiss();
					} else {
						validatePlayerCrosses(currentPlayer, selectedDices, 1, 2);
					}

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

	public List<DicesSum> getDicesSums() {
		return Arrays.asList(DicesSum.EMPTY,
				new DicesSum(DiceColor.WHITE, diceWhite1.getCurrentValue() + diceWhite2.getCurrentValue()),

				new DicesSum(DiceColor.RED, diceWhite1.getCurrentValue() + diceRed.getCurrentValue()),
				new DicesSum(DiceColor.YELLOW, diceWhite1.getCurrentValue() + diceYellow.getCurrentValue()),
				new DicesSum(DiceColor.GREEN, diceWhite1.getCurrentValue() + diceGreen.getCurrentValue()),
				new DicesSum(DiceColor.BLUE, diceWhite1.getCurrentValue() + diceBlue.getCurrentValue()),

				new DicesSum(DiceColor.RED, diceRed.getCurrentValue() + diceWhite2.getCurrentValue()),
				new DicesSum(DiceColor.YELLOW, diceYellow.getCurrentValue() + diceWhite2.getCurrentValue()),
				new DicesSum(DiceColor.GREEN, diceGreen.getCurrentValue() + diceWhite2.getCurrentValue()),
				new DicesSum(DiceColor.BLUE, diceBlue.getCurrentValue() + diceWhite2.getCurrentValue()));
	}

	private List<DicesSum> getWhiteDicesSums() {
		return Arrays.asList(DicesSum.EMPTY,
				new DicesSum(DiceColor.WHITE, diceWhite1.getCurrentValue() + diceWhite2.getCurrentValue()));
	}

	private void letOtherPlayerChooseWhiteDices(List<DicesSum> dices, IPlayer currentPlayer) {
		player.forEach(p -> {
			if (!p.equals(currentPlayer)) {
				System.out.println("Let other player choose 0 - 1 dices");
				Pair<DicesSum> selectedDices = p.chooseDices(dices, 0, 1);
				validatePlayerCrosses(p, selectedDices, 0, 1);
			}
		});
	}

	private void validatePlayerCrosses(IPlayer player, Pair<DicesSum> dices, int minDices, int maxDices) {
		DicesSum first = dices.getFirst();
		DicesSum second = dices.getSecond();

		System.out.println("-------------------------");
		System.out.println(
				"Validating Cross of Player: " + player + " (Dices: " + dices + ") - " + minDices + "/" + maxDices);

		// Auf zu wenige Würfel prüfen
		if (minDices == 1 && dices.isEmpty()) {
			throw new IllegalArgumentException("Keine Würfel ausgewählt. Min: " + minDices);
		}

		// Auf zu viele Würfel prüfen
		if (maxDices == 1 && !first.equals(DicesSum.EMPTY) && !second.equals(DicesSum.EMPTY)) {
			throw new IllegalArgumentException("Zu viele Würfel ausgewählt. Max: " + maxDices + "; Selected: 2");
		}

		if (!isEmptyDice(first) && !isEmptyDice(second)) { // zwei

			// Prüfen, ob Weiß- vor Farbwürfel gewählt wurde
			if (DiceColor.WHITE.equals(second.getColor())) {
				throw new IllegalArgumentException("Die weißen Würfel müssen zuerst ausgewählt werden!");
			}

			// An zweiter Stelle muss nun ein Farbwürfel sein, da es kein weißer sein kann

			// Prüfen, dass nur einmal Farbwürfel verwendet wurden
			if (!DiceColor.WHITE.equals(first.getColor())) {
				throw new IllegalArgumentException("Farbwürfel dürfen nur 1x verwendet werden!");
			}
		}

		if (!isEmptyDice(first)) {
			player.getGameBoard().crossField(first.getColor(), first.getSum());
		}

		if (!isEmptyDice(second)) {
			player.getGameBoard().crossField(second.getColor(), second.getSum());
		}

		System.out.println("Validation finished");
		System.out.println("-------------------------");
	}

	private boolean isEmptyDice(DicesSum sum) {
		return DicesSum.EMPTY.equals(sum);
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
