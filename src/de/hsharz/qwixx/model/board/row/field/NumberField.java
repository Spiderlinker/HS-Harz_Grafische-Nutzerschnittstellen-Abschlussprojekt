package de.hsharz.qwixx.model.board.row.field;

/**
 * Diese Klasse repr�sentiert ein Nummernfeld. <br>
 * Hierf�r ist diese Klasse ein Wrapper der Klasse {@link AbstractField}. Sie
 * stellt keinerlei weitere Methoden zur Verf�gung, da die Vaterklasse bereits
 * alle ben�tigten Methoden implementiert hat.
 * 
 * @author Oliver Lindemann
 */
public class NumberField extends AbstractField {

	public NumberField(int value) {
		super(value);
	}

}
