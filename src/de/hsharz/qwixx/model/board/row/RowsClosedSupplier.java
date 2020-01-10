package de.hsharz.qwixx.model.board.row;

import de.hsharz.qwixx.model.dice.DiceColor;

/**
 * Diese Klasse dient dem Mitteilen, dass eine Reihe geschlossen wurde und der
 * Abfrage, ob eine Reihe geschlossen ist. <br>
 * 
 * @author Oliver Lindemann
 */
public interface RowsClosedSupplier {

	/**
	 * Mittels dieser Methode kann die Reihe der gegebenen Farbe {@code color}
	 * geschlossen werden.
	 * 
	 * @param color Farbe der Reihe, die geschlossen werden soll
	 */
	void closeRow(DiceColor color);

	/**
	 * Diese Methode liefert true, falls die Reihe der gegebenen Farbe
	 * {@code rowColor} geschlossen ist. <br>
	 * Falls die Reihe noch nicht geschlossen sein sollte, so wird false geliefert
	 * 
	 * @param rowColor Farbe der Reihe, die geprueft werden soll
	 * @return Liefert true, falls die Reihe der gegebenen Farbe {@code rowColor}
	 *         geschlossen ist; false, falls die Reihe nicht geschlossen ist
	 */
	boolean isRowClosed(DiceColor rowColor);

}
