package de.hsharz.qwixx.model.board.row.field;

/**
 * Diese Klasse repr�sentiert ein Feld, welches einen bestimmten Wert hat.
 * Dieser Wert soll bei der Erstellung eines Feldes im Konstruktor festgelegt
 * werden. <br>
 * Jedes Feld kann zudem angekreuzt werden. Dies kann �ber die Methode
 * {@link #setCrossed(boolean)} gesetzt werden. <br>
 * Ob dieses Feld angekreuzt ist oder nicht, kann �ber die Methode
 * {@link #isCrossed()} ermittelt werden.
 * 
 * @author Oliver Lindemann
 */
public interface Field {

	/**
	 * @return Wert dieses Feldes
	 */
	int getValue();

	/**
	 * Der gegebene Boolean-Wert {@code crossed} gibt an, ob dieses Feld angekreuzt
	 * werden soll oder nicht.
	 * 
	 * @param crossed boolean, ob das Feld angekreuzt werden soll oder nicht
	 */
	void setCrossed(boolean crossed);

	/**
	 * Liefert einen boolean der angibt, ob dieses Feld angekreuzt ist
	 * 
	 * @param true, falls das Feld angekreuzt ist; andernfalls false
	 */
	boolean isCrossed();

}
