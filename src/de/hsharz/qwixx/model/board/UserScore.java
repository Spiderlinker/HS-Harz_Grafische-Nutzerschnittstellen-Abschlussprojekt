package de.hsharz.qwixx.model.board;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import de.hsharz.qwixx.model.dice.DiceColor;

public class UserScore {

	public static final int SCORE_PER_MISS = -5;

	private static List<Integer> score = Arrays.asList(0, 1, 3, 6, 10, 15, 21, 28, 36, 45, 55, 66, 78);

	private Map<DiceColor, Integer> rowScores = new EnumMap<>(DiceColor.class);
	private int scoreMisses;

	public UserScore() {
		rowScores.put(DiceColor.RED, 0);
		rowScores.put(DiceColor.YELLOW, 0);
		rowScores.put(DiceColor.GREEN, 0);
		rowScores.put(DiceColor.BLUE, 0);
	}

	public void setScoreOfRow(DiceColor rowColor, int amountRedFieldsCrossed) {
		rowScores.put(rowColor, score.get(amountRedFieldsCrossed));
	}

	public void setScoreMisses(int amountMisses) {
		this.scoreMisses = SCORE_PER_MISS * amountMisses;
	}

	public int getScoreOfRow(DiceColor rowColor) {
		Integer scoreOfRow = rowScores.get(rowColor);
		return scoreOfRow == null ? 0 : scoreOfRow;
	}

	public int getScoreMisses() {
		return scoreMisses;
	}

	public int getScoreComplete() {
		return rowScores.values().stream()//
				.mapToInt(i -> i) //
				.sum() //
				+ getScoreMisses();
	}

}
