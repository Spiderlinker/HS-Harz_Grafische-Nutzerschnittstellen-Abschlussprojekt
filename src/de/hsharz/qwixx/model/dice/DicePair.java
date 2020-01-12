package de.hsharz.qwixx.model.dice;

import java.util.Objects;

/**
 * Diese Klasse repräsentiert ein Würfelpaar. Ein Würfelpaar besteht entweder
 * <ol>
 * <li>aus 2 gegebenen Würfeln oder</li>
 * <li>aus einer Farbe und einer Summe.</li>
 * </ol>
 * Bei #1 wird die dominante Würfelfarbe der beiden Würfel ermittelt und als
 * Farbe dieses Würfelpaares verwendet. Zudem werden beide Würfelaugen addiert
 * und als Summe dieses Würfelpaares verwendet. <br>
 * Bei #2 werden die gegebenen Parameter für dieses Würfelpaar 1:1 übernommen.
 * 
 * @author Oliver
 */
public class DicePair {

	/**
	 * Besonderes Würfelpaar, repräsentiert ein leeres Würfelpaar. <br>
	 * Dieses Würfelpaar wird dann verwendet, wenn der Benutzer keinen Würfel
	 * auswählen möchte
	 */
	public static final DicePair EMPTY = new DicePair(DiceColor.NONE, -1);
	/**
	 * Besonderes Würfelpaar, repräsentiert einen Fehlwurf. <br>
	 * Dieses Würfelpaar wird dann verwendet, wenn der Benutzer explizit keinen
	 * Würfel verwendet und einen Fehlwurf wählt
	 */
	public static final DicePair MISS = new DicePair(DiceColor.NONE, -2);

	/** Summe dieses Würfelpaares */
	private int sum;
	/** Dominante Farbe (von beiden Würfeln) dieses Würfelpaares */
	private DiceColor color;

	/**
	 * Erzeugt ein neues Würfelpaar aus den beiden gegebenen Würfeln
	 * {@code dice1, dice2}. Es wird von beiden Würfeln die dominante Farbe
	 * ermittelt und die Summe beider Würfel berechnet. Diese bilden dann dieses
	 * Würfelpaar.
	 * 
	 * @param dice1 1. Würfel dieses Würfelpaares
	 * @param dice2 2. Würfel dieses Würfelpaares
	 */
	public DicePair(IDice dice1, IDice dice2) {
		this(DiceColor.getDominantColor(dice1.getColor(), dice2.getColor()),
				dice1.getCurrentValue() + dice2.getCurrentValue());
	}

	/**
	 * Erzeugt ein neues Würfelpaar mit der gegebenen Farbe {@code color} und der
	 * Würfelsumme {@code sum}.
	 * 
	 * @param color Farbe dieses Würfelpaares
	 * @param sum   Summe dieses Würfelpaares
	 */
	public DicePair(DiceColor color, int sum) {
		this.sum = sum;
		this.color = color;
	}

	/**
	 * @return Liefert die dominante Farbe dieses Würfelpaares
	 */
	public DiceColor getColor() {
		return this.color;
	}

	/**
	 * @return Liefert die Summe dieses Würfelpaares (beide Würfel aufaddiert)
	 */
	public int getSum() {
		return this.sum;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DicePair) {
			DicePair dice = (DicePair) obj;
			return dice.getSum() == sum && Objects.equals(color, dice.getColor());
		}
		return super.equals(obj);
	}

	@Override
	public String toString() {
		if (this.equals(EMPTY)) {
			return "-";
		}
		return "(Color: " + color + ", Sum: " + sum + ")";
	}

}
