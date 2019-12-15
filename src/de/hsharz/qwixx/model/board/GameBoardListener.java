package de.hsharz.qwixx.model.board;

import de.hsharz.qwixx.model.board.row.Row;
import de.hsharz.qwixx.model.board.row.field.Field;
import de.hsharz.qwixx.model.dice.DiceColor;

public interface GameBoardListener {

	void fieldCrossed(Row rowToCross, Field fieldToCross);

	void rowFinished(DiceColor color);
	
	void missCrossed();
	
}
