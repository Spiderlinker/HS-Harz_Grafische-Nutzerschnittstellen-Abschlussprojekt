package de.hsharz.qwixx.model;

import de.hsharz.qwixx.model.dice.DicesSum;

public class GameMove {

	private int minDices;
	private int maxDices;

	private DicesSum whiteDices;
	private DicesSum colorDices;

	public GameMove(int minDices, int maxDices) {
		this.minDices = minDices;
		this.maxDices = maxDices;
	}

	public int getMinDices() {
		return minDices;
	}

	public int getMaxDices() {
		return maxDices;
	}

	public void setWhiteDices(DicesSum whiteDices) {
		this.whiteDices = whiteDices;
	}

	public void setColorDices(DicesSum colorDices) {
		this.colorDices = colorDices;
	}

	public DicesSum getWhiteDices() {
		return whiteDices;
	}

	public DicesSum getColorDices() {
		return colorDices;
	}

}
