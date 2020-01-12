package de.hsharz.qwixx.model.dice;

/**
 * Dieses Interface stellt die grundlegenden Methoden eines Würfels dar. <br>
 * Jeder Würfel besitzt eine (bei der Erzeugung festgelegte) Farbe
 * ({@link DiceColor}), welche über die Methode {@link #getColor()} abgefragt
 * werden kann. Zudem kann ein Würfel über die Methode {@link #rollDice()}
 * gewürfelt werden. Das (zufällig erzeugte) Ergebnis des Wurfes zwischen 1 und
 * 6 kann über die Methode {@link #getCurrentValue()} geholt werden. Der Wert,
 * der die Methode {@link #getCurrentValue()} liefert, ändert sich bei jedem
 * neuen Aufruf der Methode {@link #rollDice()}.
 * 
 * @author Oliver
 */
public interface IDice {

	/**
	 * @return Liefert die Farbe ({@link DiceColor) dieses Würfels
	 */
	DiceColor getColor();

	/**
	 * Würfelt den Würfel und erzeugt somit einen neuen zufälligen Zahlenwert
	 * zwischen 1 und 6, der diese Methode zurück gibt.
	 * 
	 * @return zufällig erzeugter Zahlenwert zwischen 1 und 6
	 */
	int rollDice();

	/**
	 * @return Liefert den zuletzt gewürfelten zufälligen Zahlenwert dieses Würfels
	 *         zwischen 1 und 6; -1 falls der Würfel noch nie geworfen wurde
	 */
	public int getCurrentValue();

}
