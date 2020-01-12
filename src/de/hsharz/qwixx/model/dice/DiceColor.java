package de.hsharz.qwixx.model.dice;

/**
 * Dieses Enum enthält verschiedene Farben, die ein Würfel haben kann.
 * 
 * @author Oliver
 */
public enum DiceColor {

	/** Farbe für einen weißen Würfel */
	WHITE("#FFFFFF"),

	/** Farbe für einen roten Würfel */
	RED("#B60E0F"),

	/** Farbe für einen gelben Würfel */
	YELLOW("#E9A80D"),

	/** Farbe für einen grünen Würfel */
	GREEN("#1F953F"),

	/** Farbe für einen blauen Würfel */
	BLUE("#154E79"),

	/** Farbe für einen undefinierten Würfel (Hilfsfarbe) */
	NONE("undefined");

	/** Farbe des Enums als Hex-Darstellung */
	private String colorAsHex;

	/** Setzt für das Enum die entsprechende Hex-Darstellung */
	private DiceColor(String colorAsHex) {
		this.colorAsHex = colorAsHex;
	}

	/**
	 * @return Liefert für dieses Enum den entsprechenden Hex-Wert zur grafischen
	 *         Darstellung
	 */
	public String getAsHex() {
		return colorAsHex;
	}

	/**
	 * Liefert von zwei Würfeln die dominante Farbe. <br>
	 * Ein Farbwürfel ist immer dominanter als ein weißer Würfel. Wenn also ein
	 * weißer- und ein Farbwürfel als Parameter gegeben werden, so wird immer der
	 * Farbwürfel gewählt bzw. die Farbe zurückgeliefert. <br>
	 * Bei zwei unterschiedlichen Farben als Parameter wird immer der erste
	 * Parameter als Wert zurückgeliefert.
	 * 
	 * @param c1 Farbe 1
	 * @param c2 Farbe 2
	 * @return dominante Farbe der beiden gegebenen Farben {@code c1, c2}. Eine
	 *         Farbe ist immer dominanter. Bei zwei Farben wird die erste Farbe
	 *         zurückgeliefert.
	 */
	public static DiceColor getDominantColor(DiceColor c1, DiceColor c2) {
		return WHITE.equals(c1) ? c2 : c1;
	}

}
