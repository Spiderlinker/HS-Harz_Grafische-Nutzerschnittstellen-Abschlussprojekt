package de.hsharz.qwixx.model.board.row.field;

public class RowEndField extends AbstractField {

	public static final int ROW_END_FIELD_VALUE = 0;

	public RowEndField() {
		super(ROW_END_FIELD_VALUE);
	}

	public static Field getRowEndField() {
		return new RowEndField();
	}
}
