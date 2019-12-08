package de.hsharz.qwixx.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.hsharz.qwixx.model.board.row.RowsClosedSupplier;
import de.hsharz.qwixx.model.dice.Dice;
import de.hsharz.qwixx.model.dice.DiceColor;
import de.hsharz.qwixx.model.dice.DicesSum;
import de.hsharz.qwixx.model.dice.IDice;
import de.hsharz.qwixx.model.player.IPlayer;

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

	private void addDices() {
		dices.add(diceWhite1);
		dices.add(diceWhite2);
		dices.add(diceRed);
		dices.add(diceYellow);
		dices.add(diceGreen);
		dices.add(diceBlue);
	}

	public void addClosedRow(DiceColor closedRow) {
		this.closedRows.add(closedRow);
	}

	public void stopGame() {
		this.isPlaying = false;
	}

	public boolean isPlaying() {
		return isPlaying;
	}

	public void startGame() {
		if (player.isEmpty()) {
			throw new IllegalArgumentException("There are no players to play with");
		}

		isPlaying = true;

		while (isPlaying) {

			for (IPlayer currentPlayer : this.player) {

				rollDices();
				List<DicesSum> whiteDices = getWhiteDicesSums();
				List<DicesSum> colorDices = getColorDicesSums();

				letPlayerChooseWhiteDices(whiteDices);
				currentPlayer.chooseColorDices(colorDices);

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

	private void letPlayerChooseWhiteDices(List<DicesSum> whiteDices) {
		player.forEach(p -> p.chooseWhiteDices(whiteDices));
	}

	private boolean isGameOver() {
		return closedRows.size() >= 2;
	}

	@Override
	public void closeRow(DiceColor color) {
		System.out.println("Row closed: " + color);
		closedRows.add(color);
	}
	
	@Override
	public boolean isRowClosed(DiceColor rowColor) {
		return closedRows.contains(rowColor);
	}


}
