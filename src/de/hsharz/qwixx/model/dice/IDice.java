package de.hsharz.qwixx.model.dice;

public interface IDice {

	DiceColor getColor();
	
	int rollDice();
	
	public int getCurrentValue();

}
