package de.hsharz.qwixx.model.player;

/**
 * Diese Repr�sentiert die W�rfel-Wahlart. Entweder w�hlt der Spieler einen
 * wei�en W�rfel ({@link #WHITE_DICE}), oder eine Kombination aus wei�em- und
 * Farbw�rfel ({@link #COLOR_DICE}).
 * 
 * @author Oliver Lindemann
 */
public enum DiceSelectionType {

	/** Wei�er W�rfel wird gew�hlt */
	WHITE_DICE,
	/** Farbw�rfel wird gew�hlt (bzw. eine Kombination aus wei�em- und Farbw�rfel */
	COLOR_DICE;

}
