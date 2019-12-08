package de.hsharz.qwixx.model.dice;

import java.util.Random;

public class Dice implements IDice {

	private static final int MIN_VALUE = 1;
	private static final int MAX_VALUE = 6;

	private int currentValue = -1;
	private DiceColor color;
	private Random dice = new Random();

	public Dice(DiceColor color) {
		this.color = color;
	}

	@Override
	public DiceColor getColor() {
		return this.color;
	}

	@Override
	public int rollDice() {
		this.currentValue = MIN_VALUE + dice.nextInt(MAX_VALUE);
		return currentValue;
	}

	@Override
	public int getCurrentValue() {
		return this.currentValue;
	}

	@Override
	public String toString() {
		return "(Color: " + color + ", Value: " + currentValue + ")";
	}

}
