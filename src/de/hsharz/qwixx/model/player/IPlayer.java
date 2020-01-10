package de.hsharz.qwixx.model.player;

import java.util.List;

import de.hsharz.qwixx.model.board.GameBoard;
import de.hsharz.qwixx.model.dice.DicePair;

public interface IPlayer {

	DicePair chooseWhiteDices(List<DicePair> dices);

	DicePair chooseColorDices(List<DicePair> dices);

	GameBoard getGameBoard();

	String getName();

}
