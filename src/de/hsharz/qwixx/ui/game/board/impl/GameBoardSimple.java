package de.hsharz.qwixx.ui.game.board.impl;

import java.util.List;

import de.hsharz.qwixx.model.dice.DicePair;
import de.hsharz.qwixx.model.player.DiceSelectionType;
import de.hsharz.qwixx.model.player.IPlayer;
import de.hsharz.qwixx.ui.game.board.row.RowUI;

public class GameBoardSimple extends HumanGameBoard {

	public GameBoardSimple(IPlayer player) {
		super(player);
	}

	@Override
	public void askForInput(List<DicePair> dices, DiceSelectionType selectionType) {
		super.askForInput(dices, selectionType);
		highlightRemainingFields();
	}

	private void highlightRemainingFields() {
		rows.values().forEach(this::highlightRemainingFieldsOfRow);
	}

	private void highlightRemainingFieldsOfRow(RowUI row) {
		if (player.getGameBoard().getRowClosedSupplier().isRowClosed(row.getRow().getColor())) {
			return;
		}

		for (int i = row.getButtons().size() - 1; i >= 0; i--) {
			if (row.getRow().getFields().get(i).isCrossed()) {
				break;
			}

			row.getButtons().get(i).setDisabled(false);
		}
	}

}
