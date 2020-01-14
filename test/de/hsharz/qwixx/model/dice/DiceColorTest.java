package de.hsharz.qwixx.model.dice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class DiceColorTest {

	@ParameterizedTest
	@EnumSource(value = DiceColor.class, names = { "RED", "YELLOW", "GREEN", "BLUE" })
	void testGetDominantColor(DiceColor color) {

		Assertions.assertEquals(color, DiceColor.getDominantColor(DiceColor.WHITE, color));
		Assertions.assertEquals(color, DiceColor.getDominantColor(color, DiceColor.WHITE));

		Assertions.assertEquals(color, DiceColor.getDominantColor(color, DiceColor.RED));
		Assertions.assertEquals(color, DiceColor.getDominantColor(color, DiceColor.YELLOW));
		Assertions.assertEquals(color, DiceColor.getDominantColor(color, DiceColor.GREEN));
		Assertions.assertEquals(color, DiceColor.getDominantColor(color, DiceColor.BLUE));

		Assertions.assertEquals(DiceColor.RED, DiceColor.getDominantColor(DiceColor.RED, color));
		Assertions.assertEquals(DiceColor.YELLOW, DiceColor.getDominantColor(DiceColor.YELLOW, color));
		Assertions.assertEquals(DiceColor.GREEN, DiceColor.getDominantColor(DiceColor.GREEN, color));
		Assertions.assertEquals(DiceColor.BLUE, DiceColor.getDominantColor(DiceColor.BLUE, color));
	}

	@ParameterizedTest
	@EnumSource(value = DiceColor.class)
	void testHexIsNotNull(DiceColor color) {
		Assertions.assertNotNull(color.getAsHex());
	}

}
