package de.hsharz.qwixx.ui.game.board.row.field;

import de.hsharz.qwixx.model.dice.DiceColor;
import de.hsharz.qwixx.ui.AbstractPane;
import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class RowEndFieldUI extends AbstractPane<StackPane> {

	private DiceColor color;
	private ImageView imgLock;
	private ImageView imgCross;

	public RowEndFieldUI(DiceColor color) {
		super(new StackPane());

		this.color = color;

		createWidgets();
		addWidgets();
	}

	private void createWidgets() {
		root.setScaleX(0.7);
		root.setScaleY(0.7);
		root.setScaleZ(0.7);
		root.setStyle("-fx-background-color: white; -fx-background-radius: 100%; "
				+ "-fx-border-color: black; -fx-border-width: 1px; -fx-border-radius: 100%; ");
		root.setOpacity(0.8);

		String img = "/images/lock_" + color.toString().toLowerCase() + "_klein.png";
		imgLock = new ImageView(getClass().getResource(img).toString());
		imgLock.setRotate(20);

		imgCross = new ImageView(getClass().getResource("/images/cross.png").toString());
		imgCross.setVisible(false);
	}

	private void addWidgets() {
		root.getChildren().add(imgLock);
		root.getChildren().add(imgCross);
	}

	public void setCrossed(boolean crossed) {
		Platform.runLater(() -> {
			imgCross.setVisible(crossed);
			root.setOpacity(crossed ? 0.6 : 1);
		});
	}

	public boolean isCrossed() {
		return imgCross.isVisible();
	}

}
