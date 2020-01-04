package de.hsharz.qwixx.ui.game.board;

import java.util.List;

import de.hsharz.qwixx.model.dice.DicesSum;
import de.hsharz.qwixx.model.player.IPlayer;
import de.hsharz.qwixx.ui.game.board.row.RowUI;

public class GameBoardSimple extends GameBoardUI {

	public GameBoardSimple(IPlayer player) {
		super(player);
	}

	@Override
	public void askForInput(IPlayer player, List<DicesSum> dices, int minDices, int maxDices) {
		super.askForInput(player, dices, minDices, maxDices);
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
