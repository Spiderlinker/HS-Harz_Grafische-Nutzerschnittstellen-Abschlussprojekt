package de.hsharz.qwixx.model.player;

import java.util.List;

import de.hsharz.qwixx.model.board.GameBoard;
import de.hsharz.qwixx.model.dice.DicesSum;
import de.hsharz.qwixx.model.dice.pair.Pair;

public interface IPlayer {

	Pair<DicesSum> chooseDices(List<DicesSum> dices, int minDices, int maxDices);

	GameBoard getGameBoard();

	String getName();

}
