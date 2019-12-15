package de.hsharz.qwixx.model.board;

import java.util.ArrayList;
import java.util.List;

import de.hsharz.qwixx.model.board.row.Order;
import de.hsharz.qwixx.model.board.row.Row;
import de.hsharz.qwixx.model.board.row.RowUtils;
import de.hsharz.qwixx.model.board.row.RowsClosedSupplier;
import de.hsharz.qwixx.model.board.row.field.Field;
import de.hsharz.qwixx.model.dice.DiceColor;

public class GameBoard {

	private static final int MIN_FIELDS_CROSSED_TO_CLOSE_ROW = 6;
	private static final int AMOUNT_MISSES = 4;

	private RowsClosedSupplier rowClosedSupplier;

	private Row redRow = new Row(DiceColor.RED, Order.ASC);
	private Row yellowRow = new Row(DiceColor.YELLOW, Order.ASC);
	private Row greenRow = new Row(DiceColor.GREEN, Order.DESC);
	private Row blueRow = new Row(DiceColor.BLUE, Order.DESC);

	private int remainingMisses = AMOUNT_MISSES;

	private List<GameBoardListener> listeners = new ArrayList<>();

	private UserScore userScore;

	public GameBoard() {
		userScore = new UserScore();
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

	public Row getRedRow() {
		return redRow;
	}

	public Row getYellowRow() {
		return yellowRow;
	}

	public Row getGreenRow() {
		return greenRow;
	}

	public Row getBlueRow() {
		return blueRow;
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

	public void crossField(DiceColor colorToCross, int numberToCross) {
		System.out.println("Should cross " + colorToCross + " : " + numberToCross);
		switch (colorToCross) {
		case RED:
			validateCross(redRow, numberToCross);
			break;
		case YELLOW:
			validateCross(yellowRow, numberToCross);
			break;
		case GREEN:
			validateCross(greenRow, numberToCross);
			break;
		case BLUE:
			validateCross(blueRow, numberToCross);
			break;
		default:
			throw new IllegalArgumentException("No valid color: " + colorToCross);
		}
	}

	private void validateCross(Row rowToCross, int numberToCross) {

		if (rowClosedSupplier.isRowClosed(rowToCross.getColor())) {
			throw new IllegalAccessError("Row already closed: " + rowToCross.getColor());
		}

		int fieldToCrossIndex = -1;
		for (int i = 0; i < rowToCross.getFields().size(); i++) {
			Field field = rowToCross.getFields().get(i);
			if (field.getValue() == numberToCross) {
				// Ab der gefundenen Nummer wird nun nach Feldern gesucht, die bereits
				// abgekreuzt sind
				fieldToCrossIndex = i;
			}
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

		Field fieldToCross = rowToCross.getFields().get(fieldToCrossIndex);
		fieldToCross.setCrossed(true);

		checkIfCrossedFieldWasLastInRow(rowToCross, fieldToCross);
		updateScore();

		for (GameBoardListener listener : listeners) {
			listener.fieldCrossed(rowToCross, fieldToCross);
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
		userScore.setScoreRedRow((int) redRow.getFields().stream().filter(Field::isCrossed).count());
		userScore.setScoreYellowRow((int) yellowRow.getFields().stream().filter(Field::isCrossed).count());
		userScore.setScoreGreenRow((int) greenRow.getFields().stream().filter(Field::isCrossed).count());
		userScore.setScoreBlueRow((int) blueRow.getFields().stream().filter(Field::isCrossed).count());

		userScore.setScoreMisses(AMOUNT_MISSES - remainingMisses);
	}

	public UserScore getScore() {
		return userScore;
	}

}
