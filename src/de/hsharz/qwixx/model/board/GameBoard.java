package de.hsharz.qwixx.model.board;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hsharz.qwixx.model.board.row.Order;
import de.hsharz.qwixx.model.board.row.Row;
import de.hsharz.qwixx.model.board.row.RowsClosedSupplier;
import de.hsharz.qwixx.model.board.row.field.Field;
import de.hsharz.qwixx.model.dice.DiceColor;
import de.hsharz.qwixx.model.dice.DicePair;

/**
 * Diese Klasse repr�sentieirt das Spielfeld bzw. den Spielblock eines einzelnen
 * Spielers. <br>
 * Ein Spielblock beinhaltet 4 Reihen ({@link Row}), die Anzahl der
 * verbleibenden Fehlw�rfe und den Score.
 * <p>
 * Das GameBoard stellt zudem Methoden zur Registrierung von GameBoardListener
 * f�r dieses GameBoard zur Verf�gung. Die �ber diese Methoden registrierten
 * Listener werden �ber angekreuzte Felder, abgeschlossene Reihen und
 * angekreuzte Fehlw�rfel benachrichtigt.
 * 
 * @author Oliver Lindemann
 */
public class GameBoard {

	/**
	 * Mindestanzahl der angekreuzten Felder in einer Reihe, ab der erst das
	 * Zusatzfeld (Schloss) in einer Reihe angekreuzt und somit die Reihe
	 * abgeschlossen werden darf
	 */
	public static final int MIN_FIELDS_CROSSED_TO_CLOSE_ROW = 6;

	/** Anzahl der m�glichen Fehlw�rfe */
	public static final int AMOUNT_MISSES = 4;

	/**
	 * Supplier �ber den Reihen abgeschlossen werden k�nnen bzw. dies gepr�ft werden
	 * kann
	 */
	private RowsClosedSupplier rowClosedSupplier;
	/** Map mit Reihen auf diesem Spielfeld (<Reihenfarbe, Reihe>) */
	private Map<DiceColor, Row> rows = new EnumMap<>(DiceColor.class);

	/** Anzahl der verbleibenden Fehlw�rfe */
	private int remainingMisses = AMOUNT_MISSES;
	/** Punktezahl dieses Spielfeldes */
	private UserScore userScore;

	/** Liste mit registrierten GameBoardListener */
	private List<GameBoardListener> listeners = new ArrayList<>();

	/**
	 * Erzeugt ein neues Spielfeld.
	 * <p>
	 * Damit dieses Spielfeld verwendet werden kann, muss noch �ber die Methode
	 * {@link #setRowClosedSupplier(RowsClosedSupplier)} ein
	 * {@link RowsClosedSupplier} gesetzt werden. Ansonsten kann es zu einem
	 * fehlerhaften Verhalten bzw. Fehlermeldungen f�hren.
	 */
	public GameBoard() {
		userScore = new UserScore();
		createRows();
	}

	private void createRows() {
		// F�r die Farben RED, YELLOW, GREEN und BLUE Reihen erstellen
		rows.put(DiceColor.RED, new Row(DiceColor.RED, Order.ASC));
		rows.put(DiceColor.YELLOW, new Row(DiceColor.YELLOW, Order.ASC));
		rows.put(DiceColor.GREEN, new Row(DiceColor.GREEN, Order.DESC));
		rows.put(DiceColor.BLUE, new Row(DiceColor.BLUE, Order.DESC));
	}

	/**
	 * Der gegebene {@link GameBoardListener} wird auf dieses Spielfeld registriert
	 * und wird zuk�nftig �ber alle Ver�nderungen auf diesem Spielfeld
	 * benachrichtigt.
	 * 
	 * @param l GameBoardListener der auf dieses Spielfeld registriert werden soll
	 */
	public void addListener(GameBoardListener l) {
		this.listeners.add(l);
	}

	/**
	 * Der gegebene {@link GameBoardListener} wird von den auf diesem Spielfeld
	 * registrierte Listener entfernt und zuk�nftig nicht weiter �ber Ver�nderungen
	 * auf diesem Spielfeld benachrichtigt.
	 * 
	 * @param l GameBoardListener der nicht l�nger auf dieses Spielfeld registriert
	 *          sein soll
	 */
	public void removeListener(GameBoardListener l) {
		this.listeners.remove(l);
	}

	/**
	 * Setzt den {@link RowsClosedSupplier} dieses Spielfeldes. <b> Falls der
	 * gegebene {@code supplier} null sein sollte, so wird jeglicher zuvor
	 * registrierte {@link RowsClosedSupplier} von diesem Spielfeld entfernt.
	 * <p>
	 * Beachte, dass ein {@link RowsClosedSupplier} f�r ein funktionierendes
	 * Spielfeld ben�tigt ist.
	 * 
	 * @param supplier Supplier zum Abschlie�en von Reihen bzw. der Abfrage �ber
	 *                 geschlossene Reihen
	 */
	public void setRowClosedSupplier(RowsClosedSupplier supplier) {
		this.rowClosedSupplier = supplier;
	}

	/**
	 * Liefert die Reihen dieses Spielfeldes. Die Reihen sind in eienr Map
	 * organisiert, die der Logik <Reihenfarbe, Reihe> folgt.
	 * 
	 * @return Liefert alle Reihen dieses Spielfeldes
	 */
	public Map<DiceColor, Row> getRows() {
		return rows;
	}

	/**
	 * Liefert die passende Reihe ({@link Row}) zu der gegebenen Reihenfarbe
	 * {@code color}. <br>
	 * Falls es keine Reihe mit der gegebenen Farbe {@code color} gibt, so wird
	 * {@code null} zur�ckgeliefert.
	 * 
	 * @param color Farbe der gesuchten Reihe
	 * @return Reihe mit der gegebenen Farbe {@code color} oder null, falls keine
	 *         Reihe gefunden werden konnte
	 */
	public Row getRow(DiceColor color) {
		return rows.get(color);
	}

	/**
	 * @return Liefert die Anzahl der verbleibenden Fehlw�rfe
	 *         ({@link #AMOUNT_MISSES} - bereits verwendete Fehlw�rfe)
	 */
	public int getRemainingMisses() {
		return remainingMisses;
	}

	/**
	 * Kreuzt einen Fehlwurf auf diesem Spielfeld an. <br>
	 * Falls es keinen verbleibenden Fehlwurf mehr gibt, so wird eine
	 * {@link IllegalStateException} geworfen.
	 * 
	 * <p>
	 * Nach Verringern der verbleibenden Fehlw�rfe werden alle registrierten
	 * {@link GameBoardListener} �ber diesen Vorgang benachrichtigt.
	 */
	public void crossMiss() {
		if (remainingMisses <= 0) {
			throw new IllegalStateException("No remaining misses to cross! Remaining: " + remainingMisses);
		}

		remainingMisses--;
		updateScore(); // Score aktualisieren (-5 Punkte f�r jeden Fehlwurf)

		for (GameBoardListener l : listeners) {
			l.missCrossed();
		}
	}

	public void crossField(DicePair pair) {
		System.out.println("Should cross " + pair);
		Objects.requireNonNull(pair);

		// Wenn DicePair EMPTY ist, kann nichts angekreuzt werden
		if (pair.equals(DicePair.EMPTY)) {
			return;
		}

		// Falls DicePair ein MISS ist, dann soll ein Miss angekreuzt werden
		if (pair.equals(DicePair.MISS)) {
			crossMiss();
			return;
		}

		// Ansonsten das DicePair ankreuzen
		validateCross(rows.get(pair.getColor()), pair.getSum());
	}

	private void validateCross(Row rowToCross, int numberToCross) {

		int fieldToCrossIndex = getFieldToCrossIndex(rowToCross, numberToCross);

		checkCrossForValidity(rowToCross, numberToCross, fieldToCrossIndex);

		Field fieldToCross = rowToCross.getFields().get(fieldToCrossIndex);
		fieldToCross.setCrossed(true);

		checkIfCrossedFieldWasLastInRow(rowToCross, fieldToCross);
		updateScore();

		for (GameBoardListener listener : listeners) {
			listener.fieldCrossed(rowToCross, fieldToCross);
		}
	}

	private int getFieldToCrossIndex(Row rowToCross, int numberToCross) {
		int fieldToCrossIndex = -1;
		for (int i = 0; i < rowToCross.getFields().size(); i++) {
			Field field = rowToCross.getFields().get(i);
			if (field.getValue() == numberToCross) {
				// Ab der gefundenen Nummer wird nun nach Feldern gesucht, die bereits
				// abgekreuzt sind
				fieldToCrossIndex = i;
			}
		}
		return fieldToCrossIndex;
	}

	public void checkCrossForValidity(Row rowToCross, int numberToCross, int fieldToCrossIndex) {
		if (rowClosedSupplier.isRowClosed(rowToCross.getColor())) {
			throw new IllegalAccessError("Row already closed: " + rowToCross.getColor());
		}

		boolean fieldsInFrontNotCrossed = true;
		for (int i = fieldToCrossIndex + 1; i < rowToCross.getFields().size(); i++) {
			// Pr�fe, ob Feld bereits abgekreuzt
			if (rowToCross.getFields().get(i).isCrossed()) {
				fieldsInFrontNotCrossed = false;
			}
		}

		if (fieldToCrossIndex == -1) {
			throw new IllegalArgumentException("Nummer nicht gefunden: " + numberToCross);
		}

		if (!fieldsInFrontNotCrossed) {
			throw new IllegalAccessError("Es sind bereits Felder abgekreuzt.");
		}
	}

	private void checkIfCrossedFieldWasLastInRow(Row rowOfField, Field crossedField) {
		/*
		 * Falls das angekreuzte Feld das letzte Feld in der Reihe war und gen�gend
		 * Felder in der Reihe angekreuzt sind, so wird auch das Zusatzfeld angekreuzt
		 */
		if (isLastFieldOfRow(crossedField, rowOfField) && areEnoughFieldsCrossed(rowOfField)) {

			// Zusatzfeld ankreuzen
			rowOfField.getFields().get(rowOfField.getFields().size() - 1).setCrossed(true);
			// Reihe schlie�en
			rowClosedSupplier.closeRow(rowOfField.getColor());

			// Listener benachrichtigen
			for (GameBoardListener l : listeners) {
				l.rowFinished(rowOfField.getColor());
			}
		}

	}

	/**
	 * Liefert einen {@link Boolean} der angibt, ob in der gegebenen Reihe
	 * {@code rowToCheck} bereits gen�gend Felder angekreuzt sind, um eine Reihe
	 * abzuschlie�en.<br>
	 * Falls bereits {@value #MIN_FIELDS_CROSSED_TO_CLOSE_ROW} oder mehr Felder
	 * angekreuzt sind, so liefert diese Methode {@code true}; andernfalls
	 * {@code false}.
	 * 
	 * @param rowToCheck Reihe, dessen Anzahl an angekreuzten Feldern gepr�ft werden
	 *                   soll
	 * @return true, falls {@value #MIN_FIELDS_CROSSED_TO_CLOSE_ROW} oder mehr
	 *         Felder in der gegebenen Reihe {@code rowToCheck} angekreuzt sind;
	 *         andernfalls false
	 */
	private boolean areEnoughFieldsCrossed(Row rowToCheck) {
		return rowToCheck.getFields().stream() //
				.filter(Field::isCrossed) //
				.count() >= MIN_FIELDS_CROSSED_TO_CLOSE_ROW;
	}

	/**
	 * Liefert einen {@link Boolean} der angibt, ob das gegebene Feld {@code field}
	 * das letzte Feld in der gegebenen Reihe {@code row} ist.
	 * 
	 * @param field Feld, welches untersucht werden soll
	 * @param row   Reihe des Feldes {@code field}
	 * @return true, falls das gegebene Feld {@code field} das letzte Feld in der
	 *         Reihe {@code row} ist; false falls nicht
	 */
	private boolean isLastFieldOfRow(Field field, Row row) {
		return (row.getOrder().equals(Order.ASC) && field.getValue() == Row.ASC_LAST_VALUE)
				|| row.getOrder().equals(Order.DESC) && field.getValue() == Row.DESC_LAST_VALUE;
	}

	/**
	 * Aktualisiert den Score dieses Spielfeldes. <br>
	 * Hierbei wird der Score jeder Reihe und der Fehlw�rfe aktualisiert.
	 */
	private void updateScore() {
		// Update score field of all rows
		rows.entrySet().forEach(e -> userScore.setScoreOfRow(e.getKey(), getAmountCrossedFieldsOfRow(e.getValue())));
		userScore.setScoreMisses(AMOUNT_MISSES - remainingMisses);
	}

	/**
	 * Z�hlt die Anzahl der angekreuzten Felder in der gegebenen Reihe {@code row}
	 * und liefert diesen Wert zur�ck.
	 * 
	 * @param row Reihe, in der die angekreuzten Felder gez�hlt werden sollen
	 * @return Anzahl der angekreuzten Felder in der gegebenen Reihe {@code row}
	 */
	private int getAmountCrossedFieldsOfRow(Row row) {
		return (int) row.getFields().stream().filter(Field::isCrossed).count();
	}

	/**
	 * @return Liefert die {@link RowsClosedSupplier}, der diesem Spielfeld
	 *         zugewiesen wurde
	 */
	public RowsClosedSupplier getRowClosedSupplier() {
		return rowClosedSupplier;
	}

	/**
	 * @return Liefert den Score dieses Spielfeldes
	 */
	public UserScore getScore() {
		return userScore;
	}

}
