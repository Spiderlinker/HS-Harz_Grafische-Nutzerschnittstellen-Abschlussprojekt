package de.hsharz.qwixx.model.board.row;

import de.hsharz.qwixx.model.dice.DiceColor;

public interface RowsClosedSupplier {

	void closeRow(DiceColor color);
	
	boolean isRowClosed(DiceColor rowColor);
	
}
