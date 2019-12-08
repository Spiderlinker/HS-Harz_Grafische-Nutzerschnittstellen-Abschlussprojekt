package de.hsharz.qwixx.ui;

import de.hsharz.qwixx.model.dice.DiceColor;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class UserScore {

	private static final int FONT_SIZE_OPERATOR = 32;
	
	private HBox root;

	private Label lblScoreRedRow;
	private Label lblScoreYellowRow;
	private Label lblScoreGreenRow;
	private Label lblScoreBlueRow;
	private Label lblScoreMisses;

	private Label lblScoreResult;

	public UserScore() {

		createWidgets();
//		setupInteractions();
		addWidgets();
	}

	private void createWidgets() {

		root = new HBox(5);
		root.setPadding(new Insets(10, 20, 20, 20));
		root.setAlignment(Pos.CENTER);
		root.setStyle("-fx-background-color: #BDBDBD;");

		lblScoreRedRow = getBorderLabel("100", DiceColor.RED.getAsHex());
		lblScoreYellowRow = getBorderLabel("100", DiceColor.YELLOW.getAsHex());
		lblScoreGreenRow = getBorderLabel("100", DiceColor.GREEN.getAsHex());
		lblScoreBlueRow = getBorderLabel("100", DiceColor.BLUE.getAsHex());
		lblScoreMisses = getBorderLabel("100", "#999999");

		lblScoreResult = getBorderLabel("100", "#000000");
		lblScoreResult.setPrefWidth(160);

	}

	private void addWidgets() {

		root.getChildren().add(getSimpleLabel("Ergebnis", 18));
		root.getChildren().add(lblScoreRedRow);
		root.getChildren().add(getSimpleLabel("+", FONT_SIZE_OPERATOR));
		root.getChildren().add(lblScoreYellowRow);
		root.getChildren().add(getSimpleLabel("+", FONT_SIZE_OPERATOR));
		root.getChildren().add(lblScoreGreenRow);
		root.getChildren().add(getSimpleLabel("+", FONT_SIZE_OPERATOR));
		root.getChildren().add(lblScoreBlueRow);
		root.getChildren().add(getSimpleLabel("-", FONT_SIZE_OPERATOR));
		root.getChildren().add(lblScoreMisses);
		root.getChildren().add(getSimpleLabel("=", FONT_SIZE_OPERATOR));
		root.getChildren().add(lblScoreResult);

	}

	private Label getSimpleLabel(String text, int fontsize) {
		Label lbl = new Label(text);
		lbl.setStyle("-fx-font-size: " + fontsize + "px; -fx-font-weight: bold");
		return lbl;
	}

	public Label getBorderLabel(String text, String color) {
		Label lbl = new Label(text);
		lbl.setPrefSize(60, 40);

		lbl.setStyle("-fx-font-size: 16px; -fx-alignment: center; -fx-border-color: " + color
				+ "; -fx-border-radius: 10px; -fx-border-width: 3px; -fx-background-color: white; -fx-background-radius: 11px; ");

		return lbl;
	}

	public Pane getPane() {
		return root;
	}

}
