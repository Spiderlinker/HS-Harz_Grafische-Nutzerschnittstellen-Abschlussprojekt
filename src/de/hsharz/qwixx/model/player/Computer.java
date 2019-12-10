package de.hsharz.qwixx.model.player;

import java.util.List;

import de.hsharz.qwixx.model.board.GameBoard;
import de.hsharz.qwixx.model.dice.DicesSum;

public class Computer extends Player {

	public Computer(GameBoard board) {
		super(board);
	}

	@Override
	public DicesSum chooseWhiteDices(List<DicesSum> dices) {

		
		return null;
	}

	@Override
	public DicesSum chooseColorDices(List<DicesSum> dices) {
		return null;
	}

}
