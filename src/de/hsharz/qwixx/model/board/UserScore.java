package de.hsharz.qwixx.model.board;

import java.util.Arrays;
import java.util.List;

public class UserScore {

	public static final int SCORE_PER_MISS = -5;

	private static List<Integer> score = Arrays.asList(0, 1, 3, 6, 10, 15, 21, 28, 36, 45, 55, 66, 78);

	private int scoreRedRow;
	private int scoreYellowRow;
	private int scoreGreenRow;
	private int scoreBlueRow;
	private int scoreMisses;

	public UserScore() {

	}

	public void setScoreRedRow(int amountRedFieldsCrossed) {
		this.scoreRedRow = score.get(amountRedFieldsCrossed);
	}

	public void setScoreYellowRow(int amountYellowFieldsCrossed) {
		this.scoreYellowRow = score.get(amountYellowFieldsCrossed);
	}

	public void setScoreGreenRow(int amountGreenFieldsCrossed) {
		this.scoreGreenRow = score.get(amountGreenFieldsCrossed);
	}

	public void setScoreBlueRow(int amountBlueFieldsCrossed) {
		this.scoreBlueRow = score.get(amountBlueFieldsCrossed);
	}

	public void setScoreMisses(int amountMisses) {
		this.scoreMisses = SCORE_PER_MISS * amountMisses;
	}

	public int getScoreRedRow() {
		return scoreRedRow;
	}

	public int getScoreYellowRow() {
		return scoreYellowRow;
	}

	public int getScoreGreenRow() {
		return scoreGreenRow;
	}

	public int getScoreBlueRow() {
		return scoreBlueRow;
	}

	public int getScoreMisses() {
		return scoreMisses;
	}

	public int getScoreComplete() {
		return getScoreRedRow() //
				+ getScoreYellowRow() //
				+ getScoreGreenRow() //
				+ getScoreBlueRow() //
				+ getScoreMisses();
	}

}
