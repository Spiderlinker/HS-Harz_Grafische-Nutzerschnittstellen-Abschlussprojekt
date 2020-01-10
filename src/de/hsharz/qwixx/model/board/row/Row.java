package de.hsharz.qwixx.model.board.row;

import java.util.LinkedList;
import java.util.List;

import de.hsharz.qwixx.model.board.row.field.Field;
import de.hsharz.qwixx.model.board.row.field.NumberField;
import de.hsharz.qwixx.model.board.row.field.RowEndField;
import de.hsharz.qwixx.model.dice.DiceColor;

/**
 * Diese Klasse repräsentiert eine Reihe von Feldern ({@link Field}) einer
 * bestimmten Farbe ({@link DiceColor}) und Zahlenreihenfolge ({@link Order}).
 * <br>
 * Diese Farbe und Zahlenreihenfolge werden bei der Erzeugung der Reihe über den
 * Konstruktor bzw. die gegebenen Parameter ({@code color}, {@code order})
 * festglegt.
 * <p>
 * Je nachdem, welche Reihenfolge (Order) dieser Reihe übergeben wurden, so
 * beginnt bzw. endet diese Reihe mit unterschiedlichen Zahlenwerten (bei den
 * dessen Feldern ({@link Field}s)).
 * <p>
 * Bei einer <b>aufsteigenden</b> ({@link Order#ASC}) Reihe, ist der Wert des
 * ersten Feldes dieser Reihe {@value #ASC_FIRST_VALUE} und der Wert des letzten
 * Feldes {@value #ASC_LAST_VALUE}. <br>
 * Bei einer <b>absteigenden</b> ({@link Order#DESC}) Reihe, ist der Wert des
 * ersten Feldes dieser Reihe {@value #DESC_FIRST_VALUE} und der Wert des
 * letzten Feldes {@value #DESC_LAST_VALUE}.
 * 
 * @author Oliver Lindemann
 */
public class Row {

	/** Wert des ersten Feldes bei einer aufsteigenden Zahlenreihenfolge */
	public static final int ASC_FIRST_VALUE = 2;
	/** Wert des letzten Feldes bei einer aufsteigenden Zahlenreihenfolge */
	public static final int ASC_LAST_VALUE = 12;

	/** Wert des ersten Feldes bei einer absteigenden Zahlenreihenfolge */
	public static final int DESC_FIRST_VALUE = 12;
	/** Wert des letzten Feldes bei einer absteigenden Zahlenreihenfolge */
	public static final int DESC_LAST_VALUE = 2;

	/** Farbe dieser Reihe */
	private DiceColor color;
	/** Zahlenreihenfolge dieser Reihe */
	private Order order;

	/** Alle Felder dieser Reihe */
	private List<Field> fields;

	/**
	 * Erzeugt eine neue Reihe mit der gegebenen Farbe {@code color} und der
	 * gegebenen Zahlenreihenfolge {@code order}.
	 * 
	 * @param color Farbe dieser Reihe
	 * @param order Zahlenreihenfolge dieser Reihe (aufsteigend, absteigend)
	 */
	public Row(DiceColor color, Order order) {
		this.color = color;
		this.order = order;

		initializeFields();
	}

	private void initializeFields() {
		fields = new LinkedList<>();

		// Felder in Abhängigkeit der gegebenen Order erzeugen und der fields-Liste
		// hinzufuegen
		if (order.equals(Order.ASC)) {
			// Felder mit Zahlenwerten von 2 - 12 erzeugen
			for (int i = ASC_FIRST_VALUE; i <= ASC_LAST_VALUE; i++) {
				fields.add(new NumberField(i));
			}
		} else {
			// Felder mit Zahlenwerten von 12 - 2 erzeugen
			for (int i = DESC_FIRST_VALUE; i >= DESC_LAST_VALUE; i--) {
				fields.add(new NumberField(i));
			}
		}
		fields.add(new RowEndField());
	}

	/**
	 * @return Liefert die Farbe dieser Reihe
	 */
	public DiceColor getColor() {
		return color;
	}

	/**
	 * @return Liefert die Zahlenreihenfolge dieser Reihe (aufsteigend, absteigend)
	 */
	public Order getOrder() {
		return order;
	}

	/**
	 * @return Liefert den ersten Wert dieser Reihe (in Abhängigkeit der gesetzten
	 *         Zahlenreihenfolge)
	 */
	public int getFirstValue() {
		return order.equals(Order.ASC) ? ASC_FIRST_VALUE : DESC_FIRST_VALUE;
	}

	/**
	 * @return Liefert den letzten Wert dieser Reihe (in Abhängigkeit der gesetzten
	 *         Zahlenreihenfolge)
	 */
	public int getLastValue() {
		return order.equals(Order.ASC) ? ASC_LAST_VALUE : DESC_LAST_VALUE;
	}

	/**
	 * @return Liefert alle Felder dieser Reihe
	 */
	public List<Field> getFields() {
		return fields;
	}

}
