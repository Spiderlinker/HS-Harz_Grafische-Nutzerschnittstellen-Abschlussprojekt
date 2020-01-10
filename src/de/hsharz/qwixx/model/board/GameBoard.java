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

public class GameBoard {

	private static final int MIN_FIELDS_CROSSED_TO_CLOSE_ROW = 6;
	private static final int AMOUNT_MISSES = 4;

	private RowsClosedSupplier rowClosedSupplier;
	private Map<DiceColor, Row> rows = new EnumMap<>(DiceColor.class);

	private int remainingMisses = AMOUNT_MISSES;

	private List<GameBoardListener> listeners = new ArrayList<>();

	private UserScore userScore;

	public GameBoard() {
		userScore = new UserScore();

		createRows();
	}

	private void createRows() {
		rows.put(DiceColor.RED, new Row(DiceColor.RED, Order.ASC));
		rows.put(DiceColor.YELLOW, new Row(DiceColor.YELLOW, Order.ASC));
		rows.put(DiceColor.GREEN, new Row(DiceColor.GREEN, Order.DESC));
		rows.put(DiceColor.BLUE, new Row(DiceColor.BLUE, Order.DESC));
	}

	public void addListener(GameBoardListener l) {
		this.listeners.add(l);
	}

	public void removeListener(GameBoardListener l) {
		this.listeners.remove(l);
	}

	public void setRowClosedSupplier(RowsClosedSupplier supplier) {
		this.rowClosedSupplier = supplier;
	}

	public Map<DiceColor, Row> getRows() {
		return rows;
	}

	public Row getRow(DiceColor color) {
		return rows.get(color);
	}

	public int getRemainingMisses() {
		return remainingMisses;
	}

	public void crossMiss() {
		if (remainingMisses <= 0) {
			throw new IllegalAccessError("No remaining misses to cross! Remaining: " + remainingMisses);
		}

		remainingMisses--;
		updateScore();

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
			// Prüfe, ob Feld bereits abgekreuzt
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

		if (!areEnoughFieldsCrossed(rowOfField)) {
			return;
		}

		if ((rowOfField.getOrder().equals(Order.ASC) && crossedField.getValue() == Row.ASC_LAST_VALUE)
				|| rowOfField.getOrder().equals(Order.DESC) && crossedField.getValue() == Row.DESC_LAST_VALUE) {

			rowOfField.getFields().get(rowOfField.getFields().size() - 1).setCrossed(true); // cross last field
			rowClosedSupplier.closeRow(rowOfField.getColor());

			for (GameBoardListener l : listeners) {
				l.rowFinished(rowOfField.getColor());
			}
		}

	}

	private boolean areEnoughFieldsCrossed(Row rowToCheck) {
		return rowToCheck.getFields().stream() //
				.filter(Field::isCrossed) //
				.count() >= MIN_FIELDS_CROSSED_TO_CLOSE_ROW;
	}

	private void updateScore() {
		// Update score field of all rows
		rows.entrySet().forEach(e -> userScore.setScoreOfRow(e.getKey(), getAmountCrossedFieldsOfRow(e.getValue())));
		userScore.setScoreMisses(AMOUNT_MISSES - remainingMisses);
	}

	private int getAmountCrossedFieldsOfRow(Row row) {
		return (int) row.getFields().stream().filter(Field::isCrossed).count();
	}

	public RowsClosedSupplier getRowClosedSupplier() {
		return rowClosedSupplier;
	}

	public UserScore getScore() {
		return userScore;
	}

}
