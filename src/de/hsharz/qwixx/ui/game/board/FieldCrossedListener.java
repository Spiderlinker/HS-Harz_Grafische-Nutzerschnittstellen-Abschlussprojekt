package de.hsharz.qwixx.ui.game.board;

import de.hsharz.qwixx.ui.game.board.row.RowUI;
import de.hsharz.qwixx.ui.game.board.row.field.NumberFieldUI;

public interface FieldCrossedListener {

	void fieldCrossed(RowUI ui, NumberFieldUI btn);
	
}
