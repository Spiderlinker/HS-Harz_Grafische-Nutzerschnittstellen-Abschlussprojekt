package de.hsharz.qwixx.model.dice;

public class DicesSum {

	public static final DicesSum EMPTY = new DicesSum(null, -1);

	private int sum;
	private DiceColor color;

	public DicesSum(DiceColor color, int sum) {
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
	public String toString() {
		if (this.equals(EMPTY)) {
			return "-";
		}
		return "(Color: " + color + ", Sum: " + sum + ")";
	}

}
