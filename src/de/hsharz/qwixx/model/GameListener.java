package de.hsharz.qwixx.model;

import de.hsharz.qwixx.model.player.IPlayer;

public interface GameListener {

	void nextPlayersTurn(IPlayer nextPlayer);

	void invalidDiceChoiceMade(IPlayer player, String msg);

	void gameOver();
}
