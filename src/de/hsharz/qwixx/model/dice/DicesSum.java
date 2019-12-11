package de.hsharz.qwixx.model.dice;

import java.util.Objects;

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
	public boolean equals(Object obj) {
		if (obj instanceof DicesSum) {
			DicesSum dice = (DicesSum) obj;
			return dice.sum == sum && Objects.equals(color, dice.color);
		}
		return false;
	}

	@Override
	public String toString() {
		if (this.equals(EMPTY)) {
			return "-";
		}
		return "(Color: " + color + ", Sum: " + sum + ")";
	}

}
