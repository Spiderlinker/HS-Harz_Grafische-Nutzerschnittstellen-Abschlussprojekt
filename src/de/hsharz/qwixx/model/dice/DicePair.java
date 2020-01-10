package de.hsharz.qwixx.model.dice;

public class DicePair {

	public static final DicePair EMPTY = new DicePair(DiceColor.NONE, -1);
	public static final DicePair MISS = new DicePair(DiceColor.NONE, -2);

	private int sum;
	private DiceColor color;

	public DicePair(DiceColor color, int sum) {
		this.sum = sum;
		this.color = color;
	}

	public DiceColor getColor() {
		return this.color;
	}

	public int getSum() {
		return this.sum;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DicePair) {
			DicePair dice = (DicePair) obj;
			return dice.getSum() == sum //
					&& (color == dice.getColor() //
							|| (color != null && color.equals(dice.getColor())));
		}
		return super.equals(obj);
	}

	@Override
	public String toString() {
		if (this.equals(EMPTY)) {
			return "-";
		}
		return "(Color: " + color + ", Sum: " + sum + ")";
	}

}
