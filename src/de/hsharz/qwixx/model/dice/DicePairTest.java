package de.hsharz.qwixx.model.dice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class DicePairTest {

	@ParameterizedTest
	@EnumSource(value = DiceColor.class, names = { "RED", "YELLOW", "GREEN", "BLUE" })
	void testCreateDicePairWithTwoDices(DiceColor color) {
		Dice pair1 = new Dice(DiceColor.WHITE);
		Dice pair2 = new Dice(color);

		pair1.rollDice();
		pair2.rollDice();

		DicePair created = new DicePair(pair1, pair2);

		Assertions.assertEquals(color, created.getColor());
		Assertions.assertEquals(pair1.getCurrentValue() + pair2.getCurrentValue(), created.getSum());
	}

	@ParameterizedTest
	@EnumSource(value = DiceColor.class, names = { "RED", "YELLOW", "GREEN", "BLUE" })
	void testToString(DiceColor color) {
		DicePair created = new DicePair(color, 12);

		Assertions.assertEquals("(Color: " + color.toString() + ", Sum: 12)", created.toString());
	}

	@Test
	void testToStringEmptyMiss() {
		Assertions.assertEquals("-", DicePair.EMPTY.toString());
		Assertions.assertEquals("-", DicePair.MISS.toString());
	}

	@Test
	void testEquals() {
		DicePair test = new DicePair(DiceColor.RED, 12);
		DicePair test2 = new DicePair(DiceColor.RED, 12);

		// Beide Paare sollten gleich sein
		Assertions.assertTrue(test.equals(test2));
		Assertions.assertTrue(test2.equals(test));

		// Aber nicht gleich zu einem anderen Objekttypen
		Assertions.assertFalse(test.equals(DiceColor.RED));

		// Bei unterschiedlicher Farbe sind paare nicht mehr gleich
		DicePair differentColor = new DicePair(DiceColor.BLUE, 12);
		Assertions.assertFalse(test.equals(differentColor));
		Assertions.assertFalse(differentColor.equals(test));

		// Bei unterschiedlicher Summe sind Paare nicht mehr gleich
		DicePair differentSum = new DicePair(DiceColor.RED, 4);
		Assertions.assertFalse(test.equals(differentSum));
		Assertions.assertFalse(differentSum.equals(test));

	}

}
