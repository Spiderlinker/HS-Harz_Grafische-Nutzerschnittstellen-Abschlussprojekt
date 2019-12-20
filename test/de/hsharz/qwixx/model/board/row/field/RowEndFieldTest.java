package de.hsharz.qwixx.model.board.row.field;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class RowEndFieldTest extends AbstractFieldTest {

	@Override
	Field getField() {
		return new RowEndField();
	}

	@Override
	Field getField(int value) {
		return getField();
	}

	@ParameterizedTest
	@ValueSource(ints = { -10, -3, 0, 1, 5, 7, 123, 345123 })
	@Override
	void testGetValue(int value) {
		Assertions.assertEquals(RowEndField.ROW_END_FIELD_VALUE, getField(value).getValue());
	}

}
