package de.hsharz.qwixx.model.board.row.field;

public abstract class AbstractField implements Field {

	protected int value;
	protected boolean crossed;

	public AbstractField(int value) {
//		this.value = StringUtils.requireNonNullOrEmpty(value);
		this.value = value;
	}

	@Override
	public void setCrossed(boolean crossed) {
		this.crossed = crossed;
	}

	@Override
	public int getValue() {
		return value;
	}

	@Override
	public boolean isCrossed() {
		return crossed;
	}

}
