package de.hsharz.qwixx.model.dice;

import java.util.Random;

/**
 * Diese Klasse implementiert das Interface {@link IDice} und repräsentiert
 * somit einen Würfel.
 * 
 * @author Oliver
 */
public class Dice implements IDice {

	/** Minimaler (kleinster) Wert dieses Würfels, der geworfen werden kann */
	private static final int MIN_VALUE = 1;
	/** Maximaler (größter) Wert dieses Würfels, der geworfen werden kann */
	private static final int MAX_VALUE = 6;

	/** Zuletzt geworfener Wert */
	private int currentValue = -1;
	/** Farbe dieses Würfels */
	private DiceColor color;

	/** Random zur Erzeugung einer zufälliger Zahl */
	private Random numberGenerator = new Random();

	/**
	 * Erzeugt einen neuen Würfel mit der gegebenen Farbe {@code color}.
	 * 
	 * @param color Farbe dieses Würfels
	 */
	public Dice(DiceColor color) {
		this.color = color;
	}

	@Override
	public DiceColor getColor() {
		return this.color;
	}

	@Override
	public int rollDice() {
		this.currentValue = MIN_VALUE + numberGenerator.nextInt(MAX_VALUE);
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
