package de.hsharz.qwixx.model.dice;

/**
 * Dieses Enum enth�lt verschiedene Farben, die ein W�rfel haben kann.
 * 
 * @author Oliver
 */
public enum DiceColor {

	/** Farbe f�r einen wei�en W�rfel */
	WHITE("#FFFFFF"),

	/** Farbe f�r einen roten W�rfel */
	RED("#B60E0F"),

	/** Farbe f�r einen gelben W�rfel */
	YELLOW("#E9A80D"),

	/** Farbe f�r einen gr�nen W�rfel */
	GREEN("#1F953F"),

	/** Farbe f�r einen blauen W�rfel */
	BLUE("#154E79"),

	/** Farbe f�r einen undefinierten W�rfel (Hilfsfarbe) */
	NONE("undefined");

	/** Farbe des Enums als Hex-Darstellung */
	private String colorAsHex;

	/** Setzt f�r das Enum die entsprechende Hex-Darstellung */
	private DiceColor(String colorAsHex) {
		this.colorAsHex = colorAsHex;
	}

	/**
	 * @return Liefert f�r dieses Enum den entsprechenden Hex-Wert zur grafischen
	 *         Darstellung
	 */
	public String getAsHex() {
		return colorAsHex;
	}

	/**
	 * Liefert von zwei W�rfeln die dominante Farbe. <br>
	 * Ein Farbw�rfel ist immer dominanter als ein wei�er W�rfel. Wenn also ein
	 * wei�er- und ein Farbw�rfel als Parameter gegeben werden, so wird immer der
	 * Farbw�rfel gew�hlt bzw. die Farbe zur�ckgeliefert. <br>
	 * Bei zwei unterschiedlichen Farben als Parameter wird immer der erste
	 * Parameter als Wert zur�ckgeliefert.
	 * 
	 * @param c1 Farbe 1
	 * @param c2 Farbe 2
	 * @return dominante Farbe der beiden gegebenen Farben {@code c1, c2}. Eine
	 *         Farbe ist immer dominanter. Bei zwei Farben wird die erste Farbe
	 *         zur�ckgeliefert.
	 */
	public static DiceColor getDominantColor(DiceColor c1, DiceColor c2) {
		return WHITE.equals(c1) ? c2 : c1;
	}

}
