package de.hsharz.qwixx.model.board.row.field;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

abstract class AbstractFieldTest {

	abstract Field getField();

	abstract Field getField(int value);

	@Test
	void testSetCrossed() {
		Field field = getField();
		Assertions.assertFalse(field.isCrossed());

		field.setCrossed(true);
		Assertions.assertTrue(field.isCrossed());

		field.setCrossed(false);
		Assertions.assertFalse(field.isCrossed());
	}

	@ParameterizedTest
	@ValueSource(ints = { -10, -3, 0, 1, 5, 7, 123, 345123 })
	void testGetValue(int value) {
		Field field = getField(value);
		Assertions.assertEquals(value, field.getValue());
	}

}
