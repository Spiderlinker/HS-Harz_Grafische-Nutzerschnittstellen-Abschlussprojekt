package de.hsharz.qwixx.ui;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class BreakLabel {

	private VBox root;

	private String topText;
	private String bottomText;

	private Label lblTop;
	private Label lblBottom;

	private Separator separator;

	public BreakLabel(String topText, String bottomText) {
		this.topText = topText;
		this.bottomText = bottomText;

		createWidgets();
		addWidgets();
	}

	private void createWidgets() {
		root = new VBox();
		root.setStyle(
				"-fx-border-color: #999999; -fx-border-radius: 10px; -fx-border-width: 2px; -fx-background-color: white; -fx-background-radius: 11px; ");
		root.setPadding(new Insets(3, 8, 3, 8));
		root.setAlignment(Pos.CENTER);

		lblTop = getSimpleLabel(topText, 16);
		lblBottom = getSimpleLabel(bottomText, 16);

		separator = new Separator(Orientation.HORIZONTAL);
	}

	private void addWidgets() {
		root.getChildren().add(lblTop);
		root.getChildren().add(separator);
		root.getChildren().add(lblBottom);
	}

	private Label getSimpleLabel(String text, int fontsize) {
		Label lbl = new Label(text);
		lbl.setStyle("-fx-font-size: " + fontsize + "px; -fx-font-weight: bold");
		return lbl;
	}

	public Pane getPane() {
		return root;
	}

}
