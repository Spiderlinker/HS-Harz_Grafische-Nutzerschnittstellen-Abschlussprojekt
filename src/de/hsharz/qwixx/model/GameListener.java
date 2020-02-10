package de.hsharz.qwixx.model;

import de.hsharz.qwixx.model.player.IPlayer;

/**
 * Diese Klasse dient der Benachrichtigung �ber verschiedene Abl�ufe w�hrend
 * eines Spiels. Diese Klasse stellt Methoden bereit, die �ber den n�chsten
 * Spieler an der Reihe ({@link #nextPlayersTurn(IPlayer)}), ung�ltige Spielz�ge
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
	 * Der als Parameter gegebene Spieler {@code player} hat einen ung�ltigen
	 * Spielzug gemacht. Die Beschreibung des Fehlers bzw. des ung�ltigen Spielzuges
	 * ist in der Nachricht {@code msg} erl�utert.
	 * 
	 * @param player Spieler, der einen ung�ltigen Spielzug gemacht hat
	 * @param msg    Beschreibung des ung�ltigen Spielzuges
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
