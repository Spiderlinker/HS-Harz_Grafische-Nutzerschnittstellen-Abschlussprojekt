package de.hsharz.qwixx.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.OptionalInt;
import java.util.Queue;
import java.util.Set;

import de.hsharz.qwixx.model.board.row.RowsClosedSupplier;
import de.hsharz.qwixx.model.dice.Dice;
import de.hsharz.qwixx.model.dice.DiceColor;
import de.hsharz.qwixx.model.dice.DicesSum;
import de.hsharz.qwixx.model.dice.IDice;
import de.hsharz.qwixx.model.player.IPlayer;
import de.hsharz.qwixx.ui.GameListener;
import de.hsharz.qwixx.ui.dice.DiceListener;

public class Game implements RowsClosedSupplier {

	private Set<IPlayer> player = new HashSet<>();

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
	}

	public boolean isPlaying() {
		return isPlaying;
	}

	public List<IDice> getDices() {
		return dices;
	}

	public Set<IPlayer> getPlayer() {
		return player;
	}

	public void startGame() {
		if (player.isEmpty()) {
			throw new IllegalArgumentException("There are no players to play with");
		}

		isPlaying = true;

		while (isPlaying) {

			for (IPlayer currentPlayer : this.player) {

				System.out.println("\n\n Spieler ist an der Reihe: " + currentPlayer);
				gameListeners.forEach(l -> l.nextPlayersTurn(currentPlayer));

				rollDices();
				List<DicesSum> whiteDices = getWhiteDicesSums();
				List<DicesSum> colorDices = getColorDicesSums();

				System.out.println("---------- Other Player choosing dices");
				letOtherPlayerChooseWhiteDices(whiteDices, currentPlayer);

				System.out.println("---------- Current Player choosing dices");
				DicesSum selectedWhiteDice = currentPlayer.chooseWhiteDices(whiteDices);
				if (!selectedWhiteDice.equals(DicesSum.EMPTY)) {

					OptionalInt findFirst = whiteDices.stream().mapToInt(DicesSum::getSum)
							.filter(i -> i == selectedWhiteDice.getSum()).findFirst();

					if (!findFirst.isPresent()) {
						System.out.println("###################### DiceSum not in list, Schummler!: "
								+ selectedWhiteDice + " - " + whiteDices);

						for (DicesSum sum : whiteDices) {
							System.out.println(sum.equals(selectedWhiteDice));
						}

					}
					currentPlayer.getGameBoard().crossField(selectedWhiteDice.getColor(), selectedWhiteDice.getSum());
				}

				closeQueuedRows();
				if (isGameOver()) {
					isPlaying = false;
					break;
				}

				DicesSum selectedColorDice = currentPlayer.chooseColorDices(colorDices);
				if (!selectedColorDice.equals(DicesSum.EMPTY)) {
					if (!colorDices.contains(selectedColorDice)) {
						System.out.println("###################### DiceSum not in list, Schummler!: "
								+ selectedColorDice + " - " + colorDices);

						for (DicesSum sum : colorDices) {
							System.out.println(sum.equals(selectedColorDice));
						}
					}
					currentPlayer.getGameBoard().crossField(selectedColorDice.getColor(), selectedColorDice.getSum());
				}

				// Check if player selected any dice
				if (DicesSum.EMPTY.equals(selectedWhiteDice) && DicesSum.EMPTY.equals(selectedColorDice)) {
					// player did not select any dice, cross miss
					currentPlayer.getGameBoard().crossMiss();
				}

				closeQueuedRows();
				if (isGameOver()) {
					isPlaying = false;
					break;
				}
			}
		}
		System.out.println("Game over");
	}

	private void rollDices() {
		dices.forEach(IDice::rollDice);
		dices.forEach(System.out::println);

		diceListeners.forEach(DiceListener::dicesRolled);
	}

	private List<DicesSum> getWhiteDicesSums() {
		return Arrays.asList(DicesSum.EMPTY,
				new DicesSum(DiceColor.WHITE, diceWhite1.getCurrentValue() + diceWhite2.getCurrentValue()));
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
				DicesSum selectedDices = p.chooseWhiteDices(whiteDices);
				if (!selectedDices.equals(DicesSum.EMPTY)) {
					p.getGameBoard().crossField(selectedDices.getColor(), selectedDices.getSum());
				}
			}
		});
	}

	private void closeQueuedRows() {
		while (!rowsToCloseAfterRoundFinished.isEmpty()) {
			System.out.println("Row closed: " + rowsToCloseAfterRoundFinished.peek());
			closedRows.add(rowsToCloseAfterRoundFinished.poll());
		}
	}

	private boolean isGameOver() {
		return playerCrossedAllMisses() || closedRows.size() >= 2;
	}

	private boolean playerCrossedAllMisses() {
		for (IPlayer p : player) {
			if (p.getGameBoard().getRemainingMisses() <= 0) {
				return true;
			}
		}
		return false;
	}

	public IPlayer getWinningPlayer() {
		return player.stream().reduce(this::getPlayerWithMaxScore).orElseGet(null);
	}

	private IPlayer getPlayerWithMaxScore(IPlayer p1, IPlayer p2) {
		return p1.getGameBoard().getScore().getScoreComplete() > p2.getGameBoard().getScore().getScoreComplete() //
				? p1
				: p2;
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
