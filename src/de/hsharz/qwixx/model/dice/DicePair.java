package de.hsharz.qwixx.model.dice;

import java.util.Objects;

/**
 * Diese Klasse repr�sentiert ein W�rfelpaar. Ein W�rfelpaar besteht entweder
 * <ol>
 * <li>aus 2 gegebenen W�rfeln oder</li>
 * <li>aus einer Farbe und einer Summe.</li>
 * </ol>
 * Bei #1 wird die dominante W�rfelfarbe der beiden W�rfel ermittelt und als
 * Farbe dieses W�rfelpaares verwendet. Zudem werden beide W�rfelaugen addiert
 * und als Summe dieses W�rfelpaares verwendet. <br>
 * Bei #2 werden die gegebenen Parameter f�r dieses W�rfelpaar 1:1 �bernommen.
 * 
 * @author Oliver
 */
public class DicePair {

	/**
	 * Besonderes W�rfelpaar, repr�sentiert ein leeres W�rfelpaar. <br>
	 * Dieses W�rfelpaar wird dann verwendet, wenn der Benutzer keinen W�rfel
	 * ausw�hlen m�chte
	 */
	public static final DicePair EMPTY = new DicePair(DiceColor.NONE, -1);
	/**
	 * Besonderes W�rfelpaar, repr�sentiert einen Fehlwurf. <br>
	 * Dieses W�rfelpaar wird dann verwendet, wenn der Benutzer explizit keinen
	 * W�rfel verwendet und einen Fehlwurf w�hlt
	 */
	public static final DicePair MISS = new DicePair(DiceColor.NONE, -2);

	/** Summe dieses W�rfelpaares */
	private int sum;
	/** Dominante Farbe (von beiden W�rfeln) dieses W�rfelpaares */
	private DiceColor color;

	/**
	 * Erzeugt ein neues W�rfelpaar aus den beiden gegebenen W�rfeln
	 * {@code dice1, dice2}. Es wird von beiden W�rfeln die dominante Farbe
	 * ermittelt und die Summe beider W�rfel berechnet. Diese bilden dann dieses
	 * W�rfelpaar.
	 * 
	 * @param dice1 1. W�rfel dieses W�rfelpaares
	 * @param dice2 2. W�rfel dieses W�rfelpaares
	 */
	public DicePair(IDice dice1, IDice dice2) {
		this(DiceColor.getDominantColor(dice1.getColor(), dice2.getColor()),
				dice1.getCurrentValue() + dice2.getCurrentValue());
	}

	/**
	 * Erzeugt ein neues W�rfelpaar mit der gegebenen Farbe {@code color} und der
	 * W�rfelsumme {@code sum}.
	 * 
	 * @param color Farbe dieses W�rfelpaares
	 * @param sum   Summe dieses W�rfelpaares
	 */
	public DicePair(DiceColor color, int sum) {
		this.sum = sum;
		this.color = color;
	}

	/**
	 * @return Liefert die dominante Farbe dieses W�rfelpaares
	 */
	public DiceColor getColor() {
		return this.color;
	}

	/**
	 * @return Liefert die Summe dieses W�rfelpaares (beide W�rfel aufaddiert)
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
