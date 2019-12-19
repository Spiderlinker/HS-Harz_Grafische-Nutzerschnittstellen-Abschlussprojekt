package de.hsharz.qwixx.ui.board;

import de.hsharz.qwixx.ui.board.row.RowUI;
import de.hsharz.qwixx.ui.board.row.field.NumberFieldUI;

public interface FieldCrossedListener {

	void fieldCrossed(RowUI ui, NumberFieldUI btn);
	
}
