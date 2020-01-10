package de.hsharz.qwixx.model.board.row;

/**
 * Diese Klasse enthält verschiedene Hilfsmethoden für das Arbeiten mit Reihen
 * 
 * @author Oliver Lindemann
 */
public class RowUtils {

	/** Standardwert für Abfragen bei Reihen, in denen kein Feld angekreuzt ist */
	public static final int NO_FIELD_CROSSED = -1;

	private RowUtils() {
		// Utility class
	}

	/**
	 * Liefert einen Boolean der angibt, ob in der gegebenen Reihe {@code row} der
	 * gegebene oder nach dem gegebenen Wert {@code value} bereits Felder angekreuzt
	 * sind. <br>
	 * Diese Methode berücksichtigt die Zahlenreihenfolge ({@link Order}) der Reihe.
	 * 
	 * @param row   Reihe, in der die Felder untersucht werden sollen
	 * @param value Wert, der oder nachdem auf angekreuzte Felder geprueft werden
	 *              sollen
	 * @return true, falls der gegebene oder nach dem gegebenen Wert ein Feld in der
	 *         gegebenen Reihe angekreuzt ist
	 */
	public static boolean isCrossedAfterValue(Row row, int value) {
		int lastCrossedValue = getLastCrossedValue(row);

		if (lastCrossedValue == NO_FIELD_CROSSED) {
			return false;
		}

		if (row.getOrder().equals(Order.ASC)) {
			return lastCrossedValue >= value;
		}
		return lastCrossedValue <= value;
	}

	/**
	 * Liefert den zuletzt angekreuzten Wert in der gegebenen Reihe {@code row}.
	 * <br>
	 * Falls kein Feld angekreuzt sein sollte und es somit auch kein letztes
	 * angekreuztes Feld gibt, so wird der default-Wert {@link #NO_FIELD_CROSSED}
	 * mit dem Wert {@value #NO_FIELD_CROSSED} zurückgegeben. <br>
	 * Diese Methode berücksichtigt die Zahlenreihenfolge ({@link Order}) der Reihe.
	 * 
	 * @param row Reihe, in der der letzte angekreuzte Wert ermittelt werden soll
	 * @return Wert des letzten angekreuzten Feldes in der gegebenen Reihe;
	 *         {@value #NO_FIELD_CROSSED} bei keinem angekreuzten Feld
	 */
	public static int getLastCrossedValue(Row row) {
		int index = getLastCrossedIndex(row);
		return index == NO_FIELD_CROSSED ? index : row.getFields().get(index).getValue();
	}

	/**
	 * Liefert den Index des letzten angekreuzten Feldes aus der gegebenen Reihe
	 * {@code row}. <br>
	 * Falls kein Feld angekreuzt sein sollte und es somit auch kein letztes
	 * angekreuztes Feld gibt, so wird der default-Wert {@link #NO_FIELD_CROSSED}
	 * mit dem Wert {@value #NO_FIELD_CROSSED} zurückgegeben.
	 * 
	 * @param row Reihe, in der der Index des letzten angekreuzten Feldes ermittelt
	 *            werden soll
	 * @return Index des letzten angekreuzten Feldes aus der gegebenen Reihe
	 *         {@code row}. {@value #NO_FIELD_CROSSED} bei keinem angekreuzten Feld
	 */
	public static int getLastCrossedIndex(Row row) {
		for (int i = row.getFields().size() - 1; i >= 0; i--) {
			if (row.getFields().get(i).isCrossed()) {
				return i;
			}
		}
		return NO_FIELD_CROSSED;
	}

}
