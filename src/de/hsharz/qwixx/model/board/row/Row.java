package de.hsharz.qwixx.model.board.row;

import java.util.LinkedList;
import java.util.List;

import de.hsharz.qwixx.model.board.row.field.Field;
import de.hsharz.qwixx.model.board.row.field.NumberField;
import de.hsharz.qwixx.model.board.row.field.RowEndField;
import de.hsharz.qwixx.model.dice.DiceColor;

public class Row {

	public static final int ASC_FIRST_VALUE = 2;
	public static final int ASC_LAST_VALUE = 12;

	public static final int DESC_FIRST_VALUE = 12;
	public static final int DESC_LAST_VALUE = 2;

	private DiceColor color;
	private Order order;

	private List<Field> fields;

	public Row(DiceColor color, Order order) {
		this.color = color;
		this.order = order;

		initializeFields();
	}

	private void initializeFields() {
		fields = new LinkedList<>();
		if (order.equals(Order.ASC)) {
			for (int i = ASC_FIRST_VALUE; i <= ASC_LAST_VALUE; i++) {
				fields.add(new NumberField(i));
			}
		} else {
			for (int i = DESC_FIRST_VALUE; i >= DESC_LAST_VALUE; i--) {
				fields.add(new NumberField(i));
			}
		}
		fields.add(new RowEndField());
	}

	public DiceColor getColor() {
		return color;
	}

	public Order getOrder() {
		return order;
	}

	public List<Field> getFields() {
		return fields;
	}

}
