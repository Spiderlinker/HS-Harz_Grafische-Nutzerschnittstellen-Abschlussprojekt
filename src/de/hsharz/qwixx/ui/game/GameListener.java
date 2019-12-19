package de.hsharz.qwixx.ui.game;

import de.hsharz.qwixx.model.player.IPlayer;

public interface GameListener {

	void nextPlayersTurn(IPlayer nextPlayer);
	
}
