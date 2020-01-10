package de.hsharz.qwixx.model.board.row.field;

/**
 * Diese Klasse liefert eine Standardimplementierung für das Interface
 * {@link Field}.
 * 
 * @author Oliver Lindemann
 */
public abstract class AbstractField implements Field {

	/** Wert dieses Feldes */
	protected int value;

	/** Gibt an, ob dieses Feld angekreuzt ist */
	protected boolean crossed;

	/**
	 * Erzeugt ein neues Feld mit dem gegebenen Wert {@code value}
	 * 
	 * @param value Wert, der diesem Feld zugewiesen werden soll
	 */
	public AbstractField(int value) {
		this.value = value;
	}

	@Override
	public void setCrossed(boolean crossed) {
		this.crossed = crossed;
	}

	@Override
	public int getValue() {
		return value;
	}

	@Override
	public boolean isCrossed() {
		return crossed;
	}

}
