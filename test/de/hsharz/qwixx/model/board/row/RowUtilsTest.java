package de.hsharz.qwixx.model.board.row;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.hsharz.qwixx.model.board.row.field.Field;
import de.hsharz.qwixx.model.dice.DiceColor;

class RowUtilsTest {

	@Test
	void testGetLastCrossedIndex() {
		Row row = new Row(DiceColor.RED, Order.ASC);
		Assertions.assertEquals(-1, RowUtils.getLastCrossedIndex(row));

		for (int i = 0; i < row.getFields().size(); i++) {
			row.getFields().get(i).setCrossed(true);
			Assertions.assertEquals(i, RowUtils.getLastCrossedIndex(row));
		}
	}

	@Test
	void testGetLastCrossedValue() {
		Row row = new Row(DiceColor.RED, Order.ASC);
		Assertions.assertEquals(-1, RowUtils.getLastCrossedValue(row));

		for (Field f : row.getFields()) {
			f.setCrossed(true);
			Assertions.assertEquals(f.getValue(), RowUtils.getLastCrossedValue(row));
		}
	}

	@Test
	void testIsCrossedAfterValueAsc() {
		Row row = new Row(DiceColor.RED, Order.ASC);

		for (int i = 0; i < row.getFields().size() - 1; i++) {
			Assertions.assertFalse(RowUtils.isCrossedAfterValue(row, i + Row.ASC_FIRST_VALUE));
			row.getFields().get(i).setCrossed(true);
			Assertions.assertTrue(RowUtils.isCrossedAfterValue(row, i + Row.ASC_FIRST_VALUE));
		}
	}

	@Test
	void testIsCrossedAfterValueDesc() {
		Row row = new Row(DiceColor.RED, Order.DESC);

		for (int i = 0; i < row.getFields().size() - 1; i++) {
			Assertions.assertFalse(RowUtils.isCrossedAfterValue(row, Row.DESC_FIRST_VALUE - i));
			row.getFields().get(i).setCrossed(true);
			Assertions.assertTrue(RowUtils.isCrossedAfterValue(row, Row.DESC_FIRST_VALUE - i));
		}
	}

}
