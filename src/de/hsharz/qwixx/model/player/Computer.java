package de.hsharz.qwixx.model.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import de.hsharz.qwixx.model.board.GameBoard;
import de.hsharz.qwixx.model.board.row.Row;
import de.hsharz.qwixx.model.board.row.RowUtils;
import de.hsharz.qwixx.model.dice.DiceColor;
import de.hsharz.qwixx.model.dice.DicePair;

public class Computer extends Player {

	public Computer(String name, GameBoard board) {
		super(name, board);
	}

	@Override
	public DicePair chooseWhiteDices(List<DicePair> dices) {
		return getBestWhiteDicesSum(dices);
	}

	@Override
	public DicePair chooseColorDices(List<DicePair> dices) {
		return getBestDicesSum(dices);
	}

	private DicePair getBestWhiteDicesSum(List<DicePair> whiteDices) {

		List<DicePair> mappedDices = new ArrayList<>();

		for (DicePair whiteDice : whiteDices) {
			mappedDices.add(new DicePair(DiceColor.RED, whiteDice.getSum()));
			mappedDices.add(new DicePair(DiceColor.YELLOW, whiteDice.getSum()));
			mappedDices.add(new DicePair(DiceColor.GREEN, whiteDice.getSum()));
			mappedDices.add(new DicePair(DiceColor.BLUE, whiteDice.getSum()));
		}

		return getBestDicesSum(mappedDices);
	}

	private DicePair getBestDicesSum(List<DicePair> dices) {
		DicePair bestDices = null;

		Map<DicePair, Integer> distance = getDistancesForDices(dices);
		Optional<Entry<DicePair, Integer>> shortestDistance = distance.entrySet().stream()//
				.filter(e -> !getGameBoard().getRowClosedSupplier().isRowClosed(e.getKey().getColor())) //
				.filter(e -> e.getValue() >= 0)//
				.reduce((first, second) -> first.getValue().compareTo(second.getValue()) <= 0 ? first : second);

		System.out.println("Distances: " + distance);
		System.out.println("Filtered dice: " + shortestDistance);
		if (shortestDistance.isPresent() && shortestDistance.get().getValue() <= 2) {
			System.out.println("Found dices <= 2 " + shortestDistance);
			bestDices = shortestDistance.get().getKey();
		} else {
			bestDices = DicePair.EMPTY;
			System.out.println("No dice found");
		}

		return bestDices;
	}

	private Map<DicePair, Integer> getDistancesForDices(List<DicePair> dices) {
		// Für jedes DicePair die Distanz berechnen und als Map (Dice, Distance)
		// zurückgeben
		return dices.stream().collect(Collectors.toMap(dice -> dice, this::getDistance));
	}

	private int getDistance(DicePair dice) {
		int lastCrossedValue = RowUtils.getLastCrossedValue(getGameBoard().getRow(dice.getColor()));
		if (DiceColor.RED.equals(dice.getColor()) || DiceColor.YELLOW.equals(dice.getColor())) {
			return getDistanceAsc(dice.getSum(), lastCrossedValue);
		} else {
			return getDistanceDesc(dice.getSum(), lastCrossedValue);
		}
	}

	private int getDistanceAsc(int diceSum, int lastCrossedValue) {
		return diceSum - (lastCrossedValue == -1 ? Row.ASC_FIRST_VALUE : lastCrossedValue);
	}

	private int getDistanceDesc(int diceSum, int lastCrossedValue) {
		return (lastCrossedValue == -1 ? Row.DESC_FIRST_VALUE : lastCrossedValue) - diceSum;
	}

}
