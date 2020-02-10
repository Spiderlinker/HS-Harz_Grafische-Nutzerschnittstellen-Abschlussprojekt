package de.hsharz.qwixx.model;

import de.hsharz.qwixx.model.player.IPlayer;

/**
 * Diese Klasse dient der Benachrichtigung über verschiedene Abläufe während
 * eines Spiels. Diese Klasse stellt Methoden bereit, die über den nächsten
 * Spieler an der Reihe ({@link #nextPlayersTurn(IPlayer)}), ungültige Spielzüge
 * ({@link #invalidDiceChoiceMade(IPlayer, String)} und das Spielende
 * ({@link #gameOver()}) benachrichtigen.
 * 
 * @author Oliver Lindemann
 */
public interface GameListener {

	/**
	 * Der gegebene Spieler als Parameter {@code nextPlayer} ist jetzt an der Reihe.
	 * 
	 * @param nextPlayer Dieser Spieler ist am Zug
	 */
	default void nextPlayersTurn(IPlayer nextPlayer) {
	}

	/**
	 * Der als Parameter gegebene Spieler {@code player} hat einen ungültigen
	 * Spielzug gemacht. Die Beschreibung des Fehlers bzw. des ungültigen Spielzuges
	 * ist in der Nachricht {@code msg} erläutert.
	 * 
	 * @param player Spieler, der einen ungültigen Spielzug gemacht hat
	 * @param msg    Beschreibung des ungültigen Spielzuges
	 */
	default void invalidDiceChoiceMade(IPlayer player, String msg) {
	}

	/**
	 * Das Spiel ist vorbei
	 * 
	 * @param game Spiel, das beendet worden ist
	 */
	default void gameOver(Game game) {
	}
}
