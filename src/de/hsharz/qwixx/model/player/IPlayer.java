package de.hsharz.qwixx.model.player;

import java.util.List;

import de.hsharz.qwixx.model.board.GameBoard;
import de.hsharz.qwixx.model.dice.DicesSum;

public interface IPlayer {

	DicesSum chooseWhiteDices(List<DicesSum> dices);
	
	DicesSum chooseColorDices(List<DicesSum> dices);
	
	GameBoard getGameBoard();
	
}
