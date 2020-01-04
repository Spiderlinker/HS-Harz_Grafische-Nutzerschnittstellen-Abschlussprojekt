package de.hsharz.qwixx.ui.game.board;

import de.hsharz.qwixx.model.player.IPlayer;

public class ComputerGameBoardUI extends GameBoardUI {

	public ComputerGameBoardUI(IPlayer player) {
		super(player);
		
		disableAllButtons();
		scoreLegend.getMissFields().forEach(missField -> missField.setDisabled(true));
	}

}
