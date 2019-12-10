package de.hsharz.qwixx.ui.row;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hsharz.qwixx.model.board.row.Row;
import de.hsharz.qwixx.model.board.row.RowEndField;
import de.hsharz.qwixx.model.board.row.field.Field;
import de.hsharz.qwixx.ui.CrossButton;
import de.hsharz.qwixx.ui.FieldCrossedListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.shape.Polygon;

public class RowUI {

	private Row row;

	private StackPane root;
	private TilePane rowPane;
	private List<CrossButton> buttons = new ArrayList<>();
	private RowEndBox rowEnd;

	private Polygon triangle;

	private List<FieldCrossedListener> listeners = new ArrayList<>();

	public RowUI(Row row) {
		this.row = Objects.requireNonNull(row);

		createWidgets();
		setupInteractions();
		addWidgets();
	}

	public void addFieldCrossedListener(FieldCrossedListener l) {
		this.listeners.add(l);
	}

	private void createWidgets() {
		root = new StackPane();
		root.setStyle("-fx-background-color: " + row.getColor().getAsHex() + ";");
		root.setPadding(new Insets(5, 10, 5, 0));

		rowPane = new TilePane(2, 2);

		for (Field field : row.getFields()) {
			if (field.getValue() != RowEndField.ROW_END_FIELD_VALUE) {
				buttons.add(new CrossButton(field.getValue(), row.getColor()));
			}
		}

		rowEnd = new RowEndBox(row.getColor());

		triangle = new Polygon();
		triangle.getPoints().addAll(new Double[] { //
				0.0, 10.0, // Startpunkt links oben
				20.0, 20.0, // Spitze rechts mitte
				0.0, 30.0 // Endpunkt links unten
		});
	}

	private void setupInteractions() {
		buttons.stream() //
				.forEach(button -> button.getButton().selectedProperty()
						.addListener((observable, oldValue, newValue) -> {
							if (newValue.booleanValue()) {
								for (FieldCrossedListener l : listeners) {
									l.fieldCrossed(RowUI.this, button);
								}
							}
						}));

	}

	private void addWidgets() {

		for (CrossButton btn : buttons) {
			rowPane.getChildren().add(btn.getPane());
		}

		rowPane.getChildren().add(rowEnd.getPane());

		root.getChildren().add(rowPane);
		root.getChildren().add(triangle);

		StackPane.setMargin(rowPane, new Insets(0, 0, 0, 30));
		StackPane.setAlignment(triangle, Pos.CENTER_LEFT);
	}

	public Row getRow() {
		return row;
	}

	public RowEndBox getRowEnd() {
		return rowEnd;
	}
	
	public List<CrossButton> getButtons() {
		return buttons;
	}

	public Pane getPane() {
		return root;
	}

}
