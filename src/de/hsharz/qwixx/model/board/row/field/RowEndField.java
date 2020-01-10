package de.hsharz.qwixx.model.board.row.field;

/**
 * Diese Klasse repräsentiert ein Reihen-End-Feld. Dies ist ein Feld, welches
 * als letztes in einer Reihe steht und dient als Zusatz- / Bonusfeld in der
 * Reihe.
 * <p>
 * Dieses Feld kann vom Benutzer nur angekreuzt werden, wenn die Reihe noch
 * nicht geschlossen wurde, er mindestens 6 andere (Nummern-) Felder in der
 * entsprechenden Reihe angekreuzt hat und das letzte Nummernfeld angekreuzt
 * hat.
 * <p>
 * Dieses Feld hat den Wert {@value #ROW_END_FIELD_VALUE}, damit es zu keinem
 * Konflikt mit anderen Nummernfeldern kommt. <br>
 * Sollte ein anderes Feld (z.B. ein Nummernfeld) doch diesen Wert benötigen, so
 * muss für das Reihenendfeld ein anderer Wert vergeben werden.
 * 
 * @author Oliver Lindemann
 */
public class RowEndField extends AbstractField {

	/** Wert dieses (Reihen-End-) Feldes */
	public static final int ROW_END_FIELD_VALUE = 0;

	public RowEndField() {
		super(ROW_END_FIELD_VALUE);
	}

}
