package de.hsharz.qwixx.model.dice;

/**
 * Dieses Interface stellt die grundlegenden Methoden eines W�rfels dar. <br>
 * Jeder W�rfel besitzt eine (bei der Erzeugung festgelegte) Farbe
 * ({@link DiceColor}), welche �ber die Methode {@link #getColor()} abgefragt
 * werden kann. Zudem kann ein W�rfel �ber die Methode {@link #rollDice()}
 * gew�rfelt werden. Das (zuf�llig erzeugte) Ergebnis des Wurfes zwischen 1 und
 * 6 kann �ber die Methode {@link #getCurrentValue()} geholt werden. Der Wert,
 * der die Methode {@link #getCurrentValue()} liefert, �ndert sich bei jedem
 * neuen Aufruf der Methode {@link #rollDice()}.
 * 
 * @author Oliver
 */
public interface IDice {

	/**
	 * @return Liefert die Farbe ({@link DiceColor) dieses W�rfels
	 */
	DiceColor getColor();

	/**
	 * W�rfelt den W�rfel und erzeugt somit einen neuen zuf�lligen Zahlenwert
	 * zwischen 1 und 6, der diese Methode zur�ck gibt.
	 * 
	 * @return zuf�llig erzeugter Zahlenwert zwischen 1 und 6
	 */
	int rollDice();

	/**
	 * @return Liefert den zuletzt gew�rfelten zuf�lligen Zahlenwert dieses W�rfels
	 *         zwischen 1 und 6; -1 falls der W�rfel noch nie geworfen wurde
	 */
	public int getCurrentValue();

}
