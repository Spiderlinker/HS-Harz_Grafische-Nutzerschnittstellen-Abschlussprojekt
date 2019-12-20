package de.hsharz.qwixx.model.board.row.field;

class NumberFieldTest extends AbstractFieldTest {

	@Override
	Field getField() {
		return getField(0);
	}

	@Override
	Field getField(int value) {
		return new NumberField(value);
	}

}
