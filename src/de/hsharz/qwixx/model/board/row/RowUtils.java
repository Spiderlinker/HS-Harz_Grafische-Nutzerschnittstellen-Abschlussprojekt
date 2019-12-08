package de.hsharz.qwixx.model.board.row;

public class RowUtils {

	public static boolean isCrossedAfterValue(Row row, int value) {
		int indexOfValue = -1;
		for (int i = 0; i < row.getFields().size(); i++) {
			if (row.getFields().get(i).getValue() == value) {
				indexOfValue = i;
				break;
			}
		}
		for (int i = indexOfValue; i < row.getFields().size(); i++) {
			if (row.getFields().get(i).isCrossed()) {
				return true;
			}
		}
		return false;
	}

}
