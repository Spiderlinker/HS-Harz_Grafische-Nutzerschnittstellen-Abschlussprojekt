package de.hsharz.qwixx.model.board.row.field;

/**
 * Diese Klasse repräsentiert ein Nummernfeld. <br>
 * Hierfür ist diese Klasse ein Wrapper der Klasse {@link AbstractField}. Sie
 * stellt keinerlei weitere Methoden zur Verfügung, da die Vaterklasse bereits
 * alle benötigten Methoden implementiert hat.
 * 
 * @author Oliver Lindemann
 */
public class NumberField extends AbstractField {

	public NumberField(int value) {
		super(value);
	}

}
