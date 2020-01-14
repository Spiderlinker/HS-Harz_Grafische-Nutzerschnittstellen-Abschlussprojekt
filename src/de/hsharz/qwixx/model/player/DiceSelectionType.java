package de.hsharz.qwixx.model.player;

/**
 * Diese Repräsentiert die Würfel-Wahlart. Entweder wählt der Spieler einen
 * weißen Würfel ({@link #WHITE_DICE}), oder eine Kombination aus weißem- und
 * Farbwürfel ({@link #COLOR_DICE}).
 * 
 * @author Oliver Lindemann
 */
public enum DiceSelectionType {

	/** Weißer Würfel wird gewählt */
	WHITE_DICE,
	/** Farbwürfel wird gewählt (bzw. eine Kombination aus weißem- und Farbwürfel */
	COLOR_DICE;

}
