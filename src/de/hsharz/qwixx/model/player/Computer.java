package de.hsharz.qwixx.model.player;

import java.util.List;

import de.hsharz.qwixx.model.board.GameBoard;
import de.hsharz.qwixx.model.dice.DicesSum;
import de.hsharz.qwixx.model.dice.pair.Pair;

public class Computer extends Player {

	public Computer(GameBoard board) {
		super(board);
	}

	@Override
	public Pair<DicesSum> chooseDices(List<DicesSum> dices, int minDices, int maxDices) {
		
		
		for(DicesSum sum : dices) {
			if(!DicesSum.EMPTY.equals(sum)) {
				
			}
		}
		
		
		return null;
	}


}
