package de.hsharz.qwixx.model.dice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class DiceTest {

	@ParameterizedTest
	@EnumSource(value = DiceColor.class)
	void testGetDiceColor(DiceColor color) {
		Dice dice = new Dice(color);

		Assertions.assertEquals(color, dice.getColor());
	}

	@ParameterizedTest
	@EnumSource(value = DiceColor.class)
	void testGetCurrentValue(DiceColor color) {
		Dice dice = new Dice(color);

		Assertions.assertEquals(-1, dice.getCurrentValue());

		for (int i = 0; i < 10; i++) {
			dice.rollDice();
			Assertions.assertTrue(dice.getCurrentValue() >= 1);
			Assertions.assertTrue(dice.getCurrentValue() <= 6);
		}
	}

	@ParameterizedTest
	@EnumSource(value = DiceColor.class)
	void testToString(DiceColor color) {
		Dice dice = new Dice(color);

		Assertions.assertEquals("(Color: " + color.toString() + ", Value: -1)", dice.toString());
	}
}
