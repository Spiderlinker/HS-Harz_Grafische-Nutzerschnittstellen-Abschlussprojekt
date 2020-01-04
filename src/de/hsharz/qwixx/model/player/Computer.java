package de.hsharz.qwixx.model.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import de.hsharz.qwixx.model.board.GameBoard;
import de.hsharz.qwixx.model.board.row.Row;
import de.hsharz.qwixx.model.board.row.RowUtils;
import de.hsharz.qwixx.model.dice.DiceColor;
import de.hsharz.qwixx.model.dice.DicesSum;
import de.hsharz.qwixx.model.dice.pair.DicePair;
import de.hsharz.qwixx.model.dice.pair.Pair;

public class Computer extends Player {

	public Computer(String name, GameBoard board) {
		super(name, board);
	}

	@Override
	public Pair<DicesSum> chooseDices(List<DicesSum> dices, int minDices, int maxDices) {
		System.out.println("Choosing color dice");

		DicesSum firstSelection = null;
		DicesSum secondSelection = null;

		if (maxDices == 1) {
			firstSelection = getBestDicesSum(dices);
			secondSelection = DicesSum.EMPTY;
		} else {
			Predicate<DicesSum> isWhiteDice = dice -> DiceColor.WHITE.equals(dice.getColor());
			firstSelection = getBestWhiteDicesSum(dices.stream().filter(isWhiteDice).collect(Collectors.toList()));
			secondSelection = getBestDicesSum(dices.stream().filter(isWhiteDice.negate()).collect(Collectors.toList()));
		}

		return new DicePair(firstSelection, secondSelection);
	}

	private DicesSum getBestWhiteDicesSum(List<DicesSum> whiteDices) {

		List<DicesSum> mappedDices = new ArrayList<>();

		for (DicesSum whiteDice : whiteDices) {
			if (!DicesSum.EMPTY.equals(whiteDice)) {
				mappedDices.add(new DicesSum(DiceColor.RED, whiteDice.getSum()));
				mappedDices.add(new DicesSum(DiceColor.YELLOW, whiteDice.getSum()));
				mappedDices.add(new DicesSum(DiceColor.GREEN, whiteDice.getSum()));
				mappedDices.add(new DicesSum(DiceColor.BLUE, whiteDice.getSum()));
			}
		}

		return getBestDicesSum(mappedDices);
	}

	private DicesSum getBestDicesSum(List<DicesSum> dices) {
		DicesSum bestDices = null;

		Map<DicesSum, Integer> distance = getDistancesForDices(dices);
		Optional<Entry<DicesSum, Integer>> shortestDistance = distance.entrySet().stream()//
				.filter(e -> !getGameBoard().getRowClosedSupplier().isRowClosed(e.getKey().getColor())) //
				.filter(e -> e.getValue() >= 0)//
				.reduce((first, second) -> first.getValue().compareTo(second.getValue()) <= 0 ? first : second);

		System.out.println("Distances: " + distance);
		System.out.println("Filtered dice: " + shortestDistance);
		if (shortestDistance.isPresent() && shortestDistance.get().getValue() <= 2) {
			System.out.println("Found dices <= 2 " + shortestDistance);
			bestDices = shortestDistance.get().getKey();
		} else {
			bestDices = DicesSum.EMPTY;
			System.out.println("No dice found");
		}

		return bestDices;
	}

	private Map<DicesSum, Integer> getDistancesForDices(List<DicesSum> dices) {
		Map<DicesSum, Integer> distances = new HashMap<>();
		for (DicesSum dice : dices) {
			if (DicesSum.EMPTY.equals(dice)) {
				continue;
			}

			int distance = Integer.MAX_VALUE;
			int lastCrossedValue = -1;

			switch (dice.getColor()) {
			case RED:
				lastCrossedValue = RowUtils.getLastCrossedValue(getGameBoard().getRedRow());
				distance = getDistanceAsc(dice.getSum(), lastCrossedValue);
				break;
			case YELLOW:
				lastCrossedValue = RowUtils.getLastCrossedValue(getGameBoard().getYellowRow());
				distance = getDistanceAsc(dice.getSum(), lastCrossedValue);
				break;
			case GREEN:
				lastCrossedValue = RowUtils.getLastCrossedValue(getGameBoard().getGreenRow());
				getDistanceDesc(dice.getSum(), lastCrossedValue);
				break;
			case BLUE:
				lastCrossedValue = RowUtils.getLastCrossedValue(getGameBoard().getBlueRow());
				getDistanceDesc(dice.getSum(), lastCrossedValue);
				break;
			default:
//				lastCrossedFieldOfRow = getLastCrossedFieldOfRow(getGameBoard().getRedRow());
			}

			distances.put(dice, distance);
		}

		return distances;
	}

	private int getDistanceAsc(int diceSum, int lastCrossedValue) {
		return diceSum - (lastCrossedValue == -1 ? Row.ASC_FIRST_VALUE : lastCrossedValue);
	}

	private int getDistanceDesc(int diceSum, int lastCrossedValue) {
		return (lastCrossedValue == -1 ? Row.DESC_FIRST_VALUE : lastCrossedValue) - diceSum;
	}

}
