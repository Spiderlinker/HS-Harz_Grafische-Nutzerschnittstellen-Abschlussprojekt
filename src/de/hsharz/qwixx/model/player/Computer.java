package de.hsharz.qwixx.model.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.hsharz.qwixx.model.board.GameBoard;
import de.hsharz.qwixx.model.board.row.Row;
import de.hsharz.qwixx.model.board.row.field.Field;
import de.hsharz.qwixx.model.dice.DiceColor;
import de.hsharz.qwixx.model.dice.DicesSum;

public class Computer extends Player {

	public Computer(String name, GameBoard board) {
		super(name, board);
	}

	@Override
	public DicesSum chooseWhiteDices(List<DicesSum> dices) {
		System.out.println("Choosing white dice");
		return getBestWhiteDicesSum(dices);
	}

	@Override
	public DicesSum chooseColorDices(List<DicesSum> dices) {
		System.out.println("Choosing color dice");
		return getBestDicesSum(dices);
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
		Entry<DicesSum, Integer> entry = distance.entrySet().stream().filter(e -> e.getValue() >= 0)
				.reduce((first, second) -> first.getValue().compareTo(second.getValue()) <= 0 ? first : second).get();

		System.out.println("Distances: " + distance);
		System.out.println("Filtered dice: " + entry);
		if (entry.getValue() <= 2) {
			System.out.println("Found dices <= 2 " + entry);
			bestDices = entry.getKey();
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
			Field lastCrossedFieldOfRow = null;

			switch (dice.getColor()) {
			case RED:
				lastCrossedFieldOfRow = getLastCrossedFieldOfRow(getGameBoard().getRedRow());

				if (lastCrossedFieldOfRow == null) {
					distance = dice.getSum() - 2;
				} else {
					distance = dice.getSum() - lastCrossedFieldOfRow.getValue();
				}
				break;
			case YELLOW:
				lastCrossedFieldOfRow = getLastCrossedFieldOfRow(getGameBoard().getYellowRow());

				if (lastCrossedFieldOfRow == null) {
					distance = dice.getSum() - 2;
				} else {
					distance = dice.getSum() - lastCrossedFieldOfRow.getValue();
				}
				break;
			case GREEN:
				lastCrossedFieldOfRow = getLastCrossedFieldOfRow(getGameBoard().getGreenRow());

				if (lastCrossedFieldOfRow == null) {
					distance = 12 - dice.getSum();
				} else {
					distance = lastCrossedFieldOfRow.getValue() - dice.getSum();
				}
				break;
			case BLUE:
				lastCrossedFieldOfRow = getLastCrossedFieldOfRow(getGameBoard().getBlueRow());

				if (lastCrossedFieldOfRow == null) {
					distance = 12 - dice.getSum();
				} else {
					distance = lastCrossedFieldOfRow.getValue() - dice.getSum();
				}
				break;
			default:
//				lastCrossedFieldOfRow = getLastCrossedFieldOfRow(getGameBoard().getRedRow());
			}


			distances.put(dice, distance);
		}

		return distances;
	}

	private Field getLastCrossedFieldOfRow(Row row) {
		for (int i = row.getFields().size() - 1; i >= 0; i--) {
			Field field = row.getFields().get(i);
			if (field.isCrossed()) {
				return field;
			}
		}
		return null;
	}

}
