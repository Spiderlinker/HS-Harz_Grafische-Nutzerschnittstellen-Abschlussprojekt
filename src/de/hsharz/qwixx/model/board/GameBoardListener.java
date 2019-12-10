package de.hsharz.qwixx.model.board;

import de.hsharz.qwixx.model.board.row.field.Field;
import de.hsharz.qwixx.model.dice.DiceColor;

public interface GameBoardListener {

	void fieldCrossed(Field fieldToCross);

	void rowFinished(DiceColor color);
	
	void missCrossed();
	
}
