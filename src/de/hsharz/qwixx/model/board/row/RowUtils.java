package de.hsharz.qwixx.model.board.row;

public class RowUtils {

	public static final int NO_FIELD_CROSSED = -1;

	private RowUtils() {
		// Utility class
	}

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

	public static int getLastCrossedValue(Row row) {
		int index = getLastCrossedIndex(row);
		return index == NO_FIELD_CROSSED ? index : row.getFields().get(index).getValue();
	}

	public static int getLastCrossedIndex(Row row) {
		for (int i = row.getFields().size() - 1; i >= 0; i--) {
			if (row.getFields().get(i).isCrossed()) {
				return i;
			}
		}
		return NO_FIELD_CROSSED;
	}

}
