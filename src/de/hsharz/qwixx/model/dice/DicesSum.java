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
