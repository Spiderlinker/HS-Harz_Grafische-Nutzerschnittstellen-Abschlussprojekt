package de.hsharz.qwixx.ui.game.board;

import de.hsharz.qwixx.model.player.IPlayer;
import de.hsharz.qwixx.ui.game.board.row.RowUI;

public class ComputerGameBoardUI extends GameBoardUI {

	public ComputerGameBoardUI(IPlayer player) {
		super(player);

		disableAllButtons();
		disableMissFields();
	}

	private void disableMissFields() {
		scoreLegend.getMissFields().forEach(missField -> missField.setDisabled(true));
	}

	@Override
	public void nextPlayersTurn(IPlayer nextPlayer) {
		super.nextPlayersTurn(nextPlayer);
		checkClosedRows();
	}

	private void checkClosedRows() {
		for (RowUI row : rows.values()) {
			if (player.getGameBoard().getRowClosedSupplier().isRowClosed(row.getRow().getColor())) {
				row.setStroked(true);
			}
		}
	}

}
