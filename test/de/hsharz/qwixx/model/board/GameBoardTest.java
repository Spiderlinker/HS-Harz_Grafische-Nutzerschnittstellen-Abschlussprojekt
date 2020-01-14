package de.hsharz.qwixx.model.board;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import de.hsharz.qwixx.model.board.row.Row;
import de.hsharz.qwixx.model.board.row.RowsClosedSupplier;
import de.hsharz.qwixx.model.dice.DiceColor;
import de.hsharz.qwixx.model.dice.DicePair;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

class GameBoardTest {

	@ParameterizedTest
	@EnumSource(value = DiceColor.class, names = { "RED", "YELLOW" })
	void testInvalidCrossedRowAsc(DiceColor colorToTest) {
		GameBoard board = new GameBoard();
		board.setRowClosedSupplier(new RowsClosedSupplier() {
			@Override
			public boolean isRowClosed(DiceColor rowColor) {
				return false;
			}

			@Override
			public void closeRow(DiceColor color) {
			}
		});

		for (int v = Row.ASC_FIRST_VALUE; v <= Row.ASC_LAST_VALUE; v++) {
			board.crossField(new DicePair(colorToTest, v));
			for (int i = v - 1; i >= Row.ASC_FIRST_VALUE; i--) {
				final int valueToCross = i;
				Assertions.assertThrows(IllegalAccessError.class,
						() -> board.crossField(new DicePair(colorToTest, valueToCross)));
			}
		}

	}

	@ParameterizedTest
	@EnumSource(value = DiceColor.class, names = { "GREEN", "BLUE" })
	void testInvalidCrossedRowDesc(DiceColor colorToTest) {
		GameBoard board = new GameBoard();
		board.setRowClosedSupplier(new RowsClosedSupplier() {
			@Override
			public boolean isRowClosed(DiceColor rowColor) {
				return false;
			}

			@Override
			public void closeRow(DiceColor color) {
			}
		});

		for (int v = Row.DESC_FIRST_VALUE; v >= Row.DESC_LAST_VALUE; v--) {
			board.crossField(new DicePair(colorToTest, v));
			for (int i = v + 1; i <= Row.DESC_FIRST_VALUE; i++) {
				final int valueToCross = i;
				Assertions.assertThrows(IllegalAccessError.class,
						() -> board.crossField(new DicePair(colorToTest, valueToCross)));
			}
		}
	}

	@ParameterizedTest
	@EnumSource(value = DiceColor.class, names = { "RED", "YELLOW", "GREEN", "BLUE" })
	void testCrossClosedRowException(DiceColor color) {
		GameBoard board = new GameBoard();
		board.setRowClosedSupplier(new RowsClosedSupplier() {

			@Override
			public boolean isRowClosed(DiceColor rowColor) {
				return true;
			}

			@Override
			public void closeRow(DiceColor color) {
			}
		});

		Assertions.assertThrows(IllegalAccessError.class, () -> board.crossField(new DicePair(color, 5)));
	}

	@Test
	void testInvalidColor() {
		GameBoard board = new GameBoard();
		Assertions.assertThrows(IllegalArgumentException.class,
				() -> board.crossField(new DicePair(DiceColor.WHITE, 5)));
	}

	@ParameterizedTest
	@EnumSource(value = DiceColor.class, names = { "RED", "YELLOW", "GREEN", "BLUE" })
	void testCrossNumberNotFOund(DiceColor color) {
		GameBoard board = new GameBoard();
		board.setRowClosedSupplier(new RowsClosedSupplier() {

			@Override
			public boolean isRowClosed(DiceColor rowColor) {
				return false;
			}

			@Override
			public void closeRow(DiceColor color) {
			}
		});

		Assertions.assertThrows(IllegalArgumentException.class,
				() -> board.crossField(new DicePair(color, Row.ASC_FIRST_VALUE - 1)));
		Assertions.assertThrows(IllegalArgumentException.class,
				() -> board.crossField(new DicePair(color, Row.ASC_LAST_VALUE + 1)));
	}

	@ParameterizedTest
	@EnumSource(value = DiceColor.class, names = { "RED", "YELLOW" })
	void testFinishRowAsc(DiceColor colorToTest) {

		GameBoard board = new GameBoard();

		BooleanProperty rowClosed = new SimpleBooleanProperty(false);
		board.setRowClosedSupplier(new RowsClosedSupplier() {

			@Override
			public boolean isRowClosed(DiceColor rowColor) {
				return false;
			}

			@Override
			public void closeRow(DiceColor color) {
				System.out.println("Row closed");
				rowClosed.set(colorToTest.equals(color));
			}
		});

		board.crossField(new DicePair(colorToTest, 2));
		board.crossField(new DicePair(colorToTest, 3));
		board.crossField(new DicePair(colorToTest, 4));
		board.crossField(new DicePair(colorToTest, 5));
		board.crossField(new DicePair(colorToTest, 6));
		board.crossField(new DicePair(colorToTest, 12));

		Assertions.assertTrue(rowClosed.get());
	}

	@ParameterizedTest
	@EnumSource(value = DiceColor.class, names = { "GREEN", "BLUE" })
	void testFinishRowDesc(DiceColor colorToTest) {

		GameBoard board = new GameBoard();
		BooleanProperty rowClosed = new SimpleBooleanProperty(false);
		board.setRowClosedSupplier(new RowsClosedSupplier() {

			@Override
			public boolean isRowClosed(DiceColor rowColor) {
				return false;
			}

			@Override
			public void closeRow(DiceColor color) {
				System.out.println("Row closed");
				rowClosed.set(colorToTest.equals(color));
			}
		});

		board.crossField(new DicePair(colorToTest, 12));
		board.crossField(new DicePair(colorToTest, 6));
		board.crossField(new DicePair(colorToTest, 5));
		board.crossField(new DicePair(colorToTest, 4));
		board.crossField(new DicePair(colorToTest, 3));
		board.crossField(new DicePair(colorToTest, 2));

		Assertions.assertTrue(rowClosed.get());
	}

	@Test
	void testCrossMiss() {
		GameBoard board = new GameBoard();
		for (int i = 4; i > 0; i--) {
			Assertions.assertEquals(i, board.getRemainingMisses());
			board.crossMiss();
		}
	}

	@Test
	void testCrossTooManyMisses() {
		GameBoard board = new GameBoard();
		for (int i = 4; i > 0; i--) {
			board.crossMiss();
		}

		Assertions.assertThrows(IllegalStateException.class, board::crossMiss);
	}

	@Test
	void testRowClosedSupplier() {
		GameBoard board = new GameBoard();
		Assertions.assertNull(board.getRowClosedSupplier());

		RowsClosedSupplier testSupplier = new RowsClosedSupplier() {
			@Override
			public boolean isRowClosed(DiceColor rowColor) {
				return false;
			}

			@Override
			public void closeRow(DiceColor color) {
			}
		};

		board.setRowClosedSupplier(testSupplier);
		Assertions.assertEquals(testSupplier, board.getRowClosedSupplier());
	}

}
