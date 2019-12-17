package de.hsharz.qwixx.ui.board.row.field;

import java.io.File;

import de.hsharz.qwixx.model.dice.DiceColor;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class RowEndFieldUI {

	private DiceColor color;

	private StackPane root;
	private ImageView imgLock;
	private ImageView imgCross;

	public RowEndFieldUI(DiceColor color) {
		this.color = color;

		createWidgets();
		addWidgets();
	}

	private void createWidgets() {
		root = new StackPane();
		root.setScaleX(0.7);
		root.setScaleY(0.7);
		root.setScaleZ(0.7);
		root.setStyle(
				"-fx-background-color: white; -fx-background-radius: 100%; -fx-border-color: black; -fx-border-width: 1px; -fx-border-radius: 100%; ");
		root.setOpacity(0.8);

		String img = "images/lock_" + color.toString().toLowerCase() + "_klein.png";

		imgLock = new ImageView(new File(img).toURI().toString());
		imgLock.setRotate(20);

		imgCross = new ImageView(new File("images/cross.png").toURI().toString());
		imgCross.setVisible(false);
	}

	private void addWidgets() {
		root.getChildren().add(imgLock);
		root.getChildren().add(imgCross);
	}

	public Pane getPane() {
		return root;
	}

	public void setCrossed(boolean crossed) {
		imgCross.setVisible(crossed);
	}

	public boolean isCrossed() {
		return imgCross.isVisible();
	}

}
