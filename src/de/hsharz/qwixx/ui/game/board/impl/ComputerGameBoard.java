package de.hsharz.qwixx.ui.game.board.impl;

import de.hsharz.qwixx.model.player.IPlayer;
import de.hsharz.qwixx.ui.game.board.GameBoardUI;

public class ComputerGameBoard extends GameBoardUI {

	public ComputerGameBoard(IPlayer player) {
		super(player);

		disableAllButtons();
		disableMissFields();
	}

	private void disableMissFields() {
		scoreLegend.getMissFields().forEach(missField -> missField.setDisabled(true));
	}

}
