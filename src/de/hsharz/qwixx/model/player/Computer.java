package de.hsharz.qwixx.model.player;

import java.util.ArrayList;
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

	/** Gibt die maximale Distanz zwischen zwei Kreuzen an */
	private static final byte MAX_DICE_CROSS_DISTANCE = 2;

	public Computer(String name, GameBoard board) {
		super(name, board);
	}

	@Override
	public DicePair chooseDicePair(List<DicePair> dices, DiceSelectionType type) {
		if (type.equals(DiceSelectionType.WHITE_DICE)) {
			return getBestWhiteDicePair(dices);
		}
		return getBestDicePair(dices);
	}

	private DicePair getBestWhiteDicePair(List<DicePair> whiteDices) {

		List<DicePair> mappedDices = new ArrayList<>();
		for (DicePair whiteDice : whiteDices) {
			mappedDices.add(new DicePair(DiceColor.RED, whiteDice.getSum()));
			mappedDices.add(new DicePair(DiceColor.YELLOW, whiteDice.getSum()));
			mappedDices.add(new DicePair(DiceColor.GREEN, whiteDice.getSum()));
			mappedDices.add(new DicePair(DiceColor.BLUE, whiteDice.getSum()));
		}

		return getBestDicePair(mappedDices);
	}

	private DicePair getBestDicePair(List<DicePair> dices) {

		Map<DicePair, Integer> distance = getDistancesForDices(dices);
		Optional<Entry<DicePair, Integer>> shortestDistance = distance.entrySet().stream() //
				// Alle geschlossenen Reihen herausfiltern
				.filter(e -> !getGameBoard().getRowClosedSupplier().isRowClosed(e.getKey().getColor()))
				// Alle negativen Distanzen herausfiltern
				.filter(e -> e.getValue() >= 0)
				// Kürzeste Entfernung ermitteln
				.reduce((first, second) -> first.getValue().compareTo(second.getValue()) <= 0 ? first : second);

		
		if (shortestDistance.isPresent() && shortestDistance.get().getValue() <= MAX_DICE_CROSS_DISTANCE) {
			System.out.println(getName() + ": DicePair=" + shortestDistance.get().getKey());
			return shortestDistance.get().getKey();
		}
		System.out.println(getName() + ": No DicePair found!");
		return DicePair.EMPTY;
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
