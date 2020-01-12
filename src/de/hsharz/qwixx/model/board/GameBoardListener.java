package de.hsharz.qwixx.model.board;

import de.hsharz.qwixx.model.board.row.Row;
import de.hsharz.qwixx.model.board.row.field.Field;
import de.hsharz.qwixx.model.dice.DiceColor;

/**
 * Dieser Listener dient der Benachrichtigung über angekreuzte Felder,
 * abgeschlossene Reihen oder angekreuzte Fehlwürfe.
 * 
 * @author Oliver Lindemann
 */
public interface GameBoardListener {

	/**
	 * Das Feld {@code fieldToCross} wurde in der Reihe {@code rowToCross}
	 * angekreuzt.
	 * 
	 * @param rowToCross   Reihe, in der das Feld angekreuzt wurde
	 * @param fieldToCross Feld, das in der Reihe {@code rowToCross} angekreuzt
	 *                     wurde
	 */
	void fieldCrossed(Row rowToCross, Field fieldToCross);

	/**
	 * Die Reihe der gegebenen Farbe {@code color} wurde geschlossen
	 * 
	 * @param color Reihe der Farbe, die abgeschlossen wurde
	 */
	void rowFinished(DiceColor color);

	/**
	 * Ein Fehlwurf wurde angekreuzt
	 */
	void missCrossed();

}
