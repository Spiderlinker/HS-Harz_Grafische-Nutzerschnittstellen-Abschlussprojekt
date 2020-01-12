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
 * Diese Klasse repräsentieirt das Spielfeld bzw. den Spielblock eines einzelnen
 * Spielers. <br>
 * Ein Spielblock beinhaltet 4 Reihen ({@link Row}), die Anzahl der
 * verbleibenden Fehlwürfe und den Score.
 * <p>
 * Das GameBoard stellt zudem Methoden zur Registrierung von GameBoardListener
 * für dieses GameBoard zur Verfügung. Die über diese Methoden registrierten
 * Listener werden über angekreuzte Felder, abgeschlossene Reihen und
 * angekreuzte Fehlwürfel benachrichtigt.
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

	/** Anzahl der möglichen Fehlwürfe */
	public static final int AMOUNT_MISSES = 4;

	/**
	 * Supplier über den Reihen abgeschlossen werden können bzw. dies geprüft werden
	 * kann
	 */
	private RowsClosedSupplier rowClosedSupplier;
	/** Map mit Reihen auf diesem Spielfeld (<Reihenfarbe, Reihe>) */
	private Map<DiceColor, Row> rows = new EnumMap<>(DiceColor.class);

	/** Anzahl der verbleibenden Fehlwürfe */
	private int remainingMisses = AMOUNT_MISSES;
	/** Punktezahl dieses Spielfeldes */
	private UserScore userScore;

	/** Liste mit registrierten GameBoardListener */
	private List<GameBoardListener> listeners = new ArrayList<>();

	/**
	 * Erzeugt ein neues Spielfeld.
	 * <p>
	 * Damit dieses Spielfeld verwendet werden kann, muss noch über die Methode
	 * {@link #setRowClosedSupplier(RowsClosedSupplier)} ein
	 * {@link RowsClosedSupplier} gesetzt werden. Ansonsten kann es zu einem
	 * fehlerhaften Verhalten bzw. Fehlermeldungen führen.
	 */
	public GameBoard() {
		userScore = new UserScore();
		createRows();
	}

	private void createRows() {
		// Für die Farben RED, YELLOW, GREEN und BLUE Reihen erstellen
		rows.put(DiceColor.RED, new Row(DiceColor.RED, Order.ASC));
		rows.put(DiceColor.YELLOW, new Row(DiceColor.YELLOW, Order.ASC));
		rows.put(DiceColor.GREEN, new Row(DiceColor.GREEN, Order.DESC));
		rows.put(DiceColor.BLUE, new Row(DiceColor.BLUE, Order.DESC));
	}

	/**
	 * Der gegebene {@link GameBoardListener} wird auf dieses Spielfeld registriert
	 * und wird zukünftig über alle Veränderungen auf diesem Spielfeld
	 * benachrichtigt.
	 * 
	 * @param l GameBoardListener der auf dieses Spielfeld registriert werden soll
	 */
	public void addListener(GameBoardListener l) {
		this.listeners.add(l);
	}

	/**
	 * Der gegebene {@link GameBoardListener} wird von den auf diesem Spielfeld
	 * registrierte Listener entfernt und zukünftig nicht weiter über Veränderungen
	 * auf diesem Spielfeld benachrichtigt.
	 * 
	 * @param l GameBoardListener der nicht länger auf dieses Spielfeld registriert
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
	 * Beachte, dass ein {@link RowsClosedSupplier} für ein funktionierendes
	 * Spielfeld benötigt ist.
	 * 
	 * @param supplier Supplier zum Abschließen von Reihen bzw. der Abfrage über
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
	 * {@code null} zurückgeliefert.
	 * 
	 * @param color Farbe der gesuchten Reihe
	 * @return Reihe mit der gegebenen Farbe {@code color} oder null, falls keine
	 *         Reihe gefunden werden konnte
	 */
	public Row getRow(DiceColor color) {
		return rows.get(color);
	}

	/**
	 * @return Liefert die Anzahl der verbleibenden Fehlwürfe
	 *         ({@link #AMOUNT_MISSES} - bereits verwendete Fehlwürfe)
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
	 * Nach Verringern der verbleibenden Fehlwürfe werden alle registrierten
	 * {@link GameBoardListener} über diesen Vorgang benachrichtigt.
	 */
	public void crossMiss() {
		if (remainingMisses <= 0) {
			throw new IllegalStateException("No remaining misses to cross! Remaining: " + remainingMisses);
		}

		remainingMisses--;
		updateScore(); // Score aktualisieren (-5 Punkte für jeden Fehlwurf)

		for (GameBoardListener l : listeners) {
			l.missCrossed();
		}
	}

	/**
	 * Diese Methode kreuzt das gegebene Würfelpaar {@code pair} auf diesem
	 * Spielfeld an. <br>
	 * Das Würfelpaar wird vor dem Ankreuzen noch auf Richtigkeit geprüft. Sollte
	 * das gegebene Paar nicht gültig sein, so wird eine
	 * {@link IllegalArgumentException} mit der entsprechenden Fehlermeldung
	 * geworfen.
	 * <p>
	 * <ul>
	 * <li>Falls das gegebene Würfelpaar {@code null} sein sollte, so wird eine
	 * {@link NullPointerException} geworfen</li>
	 * <li>{@link DicePair#EMPTY} wird ignoriert und die Methode macht nichts.</li>
	 * <li>{@link DicePair#MISS} wird verarbeitet und ein Fehlwurf wird
	 * angekreuzt</li>
	 * </ul>
	 * 
	 * @param pair Würfelpaar (nicht null), das angekreuzt werden soll
	 */
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

	/**
	 * Diese Methode prüft die gegebene Zahl {@code numberToCross}, die in der Reihe
	 * {@code rowToCross} angekreuzt werden soll. <br>
	 * Falls die gegebene Zahl nicht angekreuzt werden darf, so wird eine
	 * {@link IllegalArgumentException} bzw. ein {@link IllegalAccessError}
	 * geworfen.
	 * 
	 * @param rowToCross    Reihe, in der die Zahl {@code numberToCross} angekreuzt
	 *                      werden soll
	 * @param numberToCross Zahl, di in der Reihe {@code rowToCross} angekreuzt
	 *                      werden soll
	 */
	private void validateCross(Row rowToCross, int numberToCross) {

		/*
		 * Das gegebene Feld auf Fehler prüfen. Falls Fehler gefunden wird, dann wird
		 * eine Exception geworfen und diese Methode läuft nicht weiter
		 */
		int fieldToCrossIndex = getFieldToCrossIndex(rowToCross, numberToCross);
		checkCrossForValidity(rowToCross, numberToCross, fieldToCrossIndex);

		// Es wurde kein Fehler gefunden, das Feld kann angekreuzt werden
		Field fieldToCross = rowToCross.getFields().get(fieldToCrossIndex);
		fieldToCross.setCrossed(true);

		// Prüfen, ob auch noch das Zusatzfeld angekreuzt werden und die Reihe
		// geschlossen werden muss
		checkIfCrossedFieldWasLastInRow(rowToCross, fieldToCross);

		// Score aktualisieren
		updateScore();

		for (GameBoardListener listener : listeners) {
			listener.fieldCrossed(rowToCross, fieldToCross);
		}
	}

	/**
	 * Liefert den Index des Feldes in der Reihe {@rowToCross}, welches den Wert der
	 * gegebenen Zahl {@code numberToCross} besitzt. <br>
	 * Zu der gegebenen Zahl wird also in der gegebenen Reihe nach dem
	 * entsprechenden Feld gesucht, welches die Zahl in dieser Reihe repräsentiert.
	 * 
	 * @param rowToCross    Reihe der gegebenen Zahl, in der nach dem entsprechendem
	 *                      Feld gesucht werden soll
	 * @param numberToCross Zahl, zu der der Index des passenden Feldes in der
	 *                      gegebenen Reihe gesucht werden soll
	 * @return Index des Feldes, das zu der gegebenen Zahl in der gegebenen Reihe
	 *         gehört
	 */
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

	/**
	 * Prüft die gegebene Zahl auf Gültigkeit und ob diese in der gegebenen Reihe
	 * angekreuzt werden darf. Falls die Zahl bzw. das entsprechende Feld nicht
	 * angekreuzt werden darf, so wird ein {@link IllegalAccessError} bzw. eine
	 * {@link IllegalArgumentException} geworfen.
	 * 
	 * @param rowToCross        Reihe, in der die gegebene Zahl bzw. das
	 *                          entsprechende Feld angekreuzt werden soll
	 * @param numberToCross     Zahl, die angekreuzt werden soll
	 * @param fieldToCrossIndex Feld der gegebenen Zahl, die angekreuzt werden soll
	 */
	public void checkCrossForValidity(Row rowToCross, int numberToCross, int fieldToCrossIndex) {
		// Falls die Reihe, in der das gegebene Feld angekreuzt werden soll, geschlossen
		// ist, so darf das Feld nicht angekreuzt werden -> nicht zulässig
		if (rowClosedSupplier.isRowClosed(rowToCross.getColor())) {
			throw new IllegalAccessError("Row already closed: " + rowToCross.getColor());
		}

		// Falls gegebener Index == -1 ist, dann existiert die Nummer nicht
		// -> nicht zulässig
		if (fieldToCrossIndex == -1) {
			throw new IllegalArgumentException("Nummer nicht gefunden: " + numberToCross);
		}

		// Prüfe, ob bereits Felder nach dem gegebenen Feld angekreuzt wurden
		// Falls bereits Felder nach dem gegebenen Feld angekreuzt sind, dann
		// ist das gegebene Feld nicht zulässig
		for (int i = fieldToCrossIndex + 1; i < rowToCross.getFields().size(); i++) {
			// Prüfe, ob Feld bereits abgekreuzt
			if (rowToCross.getFields().get(i).isCrossed()) {
				throw new IllegalAccessError("Es sind bereits Felder nach dem Zahl " + numberToCross + " angekreuzt.");
			}
		}
	}

	/**
	 * Prüft, ob das angekreuzte Feld {@code crossedField} das letzte Feld in der
	 * Reihe {@code rowOfField} war. Falls es das letzte Feld war und genügend
	 * Felder in der Reihe angekreuzt sind, so wird auch das Zusatzfeld angekreuzt
	 * und die Reihe geschlossen.
	 * 
	 * @param rowOfField   Reihe des angekreuzten Feldes {@code crossedField}
	 * @param crossedField angekreuztes Feld, das geprüft werden soll
	 */
	private void checkIfCrossedFieldWasLastInRow(Row rowOfField, Field crossedField) {
		/*
		 * Falls das angekreuzte Feld das letzte Feld in der Reihe war und genügend
		 * Felder in der Reihe angekreuzt sind, so wird auch das Zusatzfeld angekreuzt
		 */
		if (isLastFieldOfRow(crossedField, rowOfField) && areEnoughFieldsCrossed(rowOfField)) {

			// Zusatzfeld ankreuzen
			rowOfField.getFields().get(rowOfField.getFields().size() - 1).setCrossed(true);
			// Reihe schließen
			rowClosedSupplier.closeRow(rowOfField.getColor());

			// Listener benachrichtigen
			for (GameBoardListener l : listeners) {
				l.rowFinished(rowOfField.getColor());
			}
		}

	}

	/**
	 * Liefert einen {@link Boolean} der angibt, ob in der gegebenen Reihe
	 * {@code rowToCheck} bereits genügend Felder angekreuzt sind, um eine Reihe
	 * abzuschließen.<br>
	 * Falls bereits {@value #MIN_FIELDS_CROSSED_TO_CLOSE_ROW} oder mehr Felder
	 * angekreuzt sind, so liefert diese Methode {@code true}; andernfalls
	 * {@code false}.
	 * 
	 * @param rowToCheck Reihe, dessen Anzahl an angekreuzten Feldern geprüft werden
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
	 * Hierbei wird der Score jeder Reihe und der Fehlwürfe aktualisiert.
	 */
	private void updateScore() {
		// Update score field of all rows
		rows.entrySet().forEach(e -> userScore.setScoreOfRow(e.getKey(), getAmountCrossedFieldsOfRow(e.getValue())));
		userScore.setScoreMisses(AMOUNT_MISSES - remainingMisses);
	}

	/**
	 * Zählt die Anzahl der angekreuzten Felder in der gegebenen Reihe {@code row}
	 * und liefert diesen Wert zurück.
	 * 
	 * @param row Reihe, in der die angekreuzten Felder gezählt werden sollen
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
