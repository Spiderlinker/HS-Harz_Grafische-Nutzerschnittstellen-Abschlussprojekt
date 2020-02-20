package de.hsharz.qwixx.model.board;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import de.hsharz.qwixx.model.dice.DiceColor;

class UserScoreTest {

	@ParameterizedTest
	@EnumSource(value = DiceColor.class)
	void testSetGetRowScore(DiceColor color) {
		UserScore score = new UserScore();

		int testScore = 8;
		Assertions.assertEquals(0, score.getScoreOfRow(color));
		score.setScoreOfRow(color, 8);

		Assertions.assertEquals(UserScore.SCORE_PER_CROSS.get(testScore), score.getScoreOfRow(color));
	}

	@Test
	void testMissScore() {
		UserScore score = new UserScore();
		Assertions.assertEquals(0, score.getScoreMisses());

		for (int i = 1; i <= 4; i++) {
			score.setScoreMisses(i);
			Assertions.assertEquals(i * UserScore.SCORE_PER_MISS, score.getScoreMisses());
		}
	}

	@Test
	void testCompleteScore() {
		UserScore score = new UserScore();
		Assertions.assertEquals(0, score.getScoreComplete());

		int testScoreRed = 3;
		int testScoreYellow = 8;
		int testScoreGreen = 1;
		int testScoreBlue = 5;
		int testScoreMisses = 2;

		score.setScoreOfRow(DiceColor.RED, testScoreRed);
		score.setScoreOfRow(DiceColor.YELLOW, testScoreYellow);
		score.setScoreOfRow(DiceColor.GREEN, testScoreGreen);
		score.setScoreOfRow(DiceColor.BLUE, testScoreBlue);
		score.setScoreMisses(testScoreMisses);

		int completeScore = -testScoreMisses * UserScore.SCORE_PER_MISS // Fehlwürfel
				+ UserScore.SCORE_PER_CROSS.get(testScoreRed) // Rote Reihe
				+ UserScore.SCORE_PER_CROSS.get(testScoreYellow) // Gelbe Reihe
				+ UserScore.SCORE_PER_CROSS.get(testScoreGreen) // Grüne Reihe
				+ UserScore.SCORE_PER_CROSS.get(testScoreBlue);// Blaue Reihe

		Assertions.assertEquals(completeScore, score.getScoreComplete());

	}

}
