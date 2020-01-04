package de.hsharz.qwixx.model.dice;

public class DicePair extends Pair<IDice> {

	public DicePair(IDice first, IDice second) {
		super(first, second);
	}

	public DiceColor getColor() {
		return DiceColor.getDominantColor(getFirst().getColor(), getSecond().getColor());
	}

	public int getSum() {
		return getFirst().getCurrentValue() + getSecond().getCurrentValue();
	}

}
