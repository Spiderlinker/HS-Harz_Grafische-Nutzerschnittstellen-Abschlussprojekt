package de.hsharz.qwixx.ui.game.board.row.field;

import de.hsharz.qwixx.model.dice.DiceColor;
import de.hsharz.qwixx.ui.AbstractPane;
import javafx.application.Platform;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

public class NumberFieldUI extends AbstractPane<StackPane> {

	private int value;
	private String color;

	private ToggleButton btn;
	private ImageView imgView;

	private boolean disabled = false;

	public NumberFieldUI(int value, DiceColor textColor) {
		super(new StackPane());

		this.value = value;
		this.color = textColor.getAsHex();

		createWidgets();
		setupBindings();
	}

	private void createWidgets() {
		btn = new ToggleButton(Integer.toString(value));
		btn.setStyle("-fx-background-color: white; -fx-text-fill: " + color + "; -fx-font-size: 18pt;");
		btn.setOpacity(0.85);
		btn.setMinWidth(Region.USE_PREF_SIZE);
		btn.setMaxWidth(Double.MAX_VALUE);
		btn.setMouseTransparent(true);

		imgView = new ImageView(getClass().getResource("/images/cross.png").toString());
		imgView.setScaleX(0.8);
		imgView.setScaleY(0.8);
		imgView.setScaleZ(0.8);
		imgView.setVisible(false);
		imgView.setMouseTransparent(true);

		root.getChildren().add(btn);
		root.getChildren().add(imgView);
	}

	private void setupBindings() {
		root.setOnMouseEntered(event -> {
			if (!disabled && !btn.isSelected()) {
				imgView.setOpacity(0.6);
				imgView.setVisible(true);
			}
		});
		root.setOnMouseExited(event -> {
			if (!btn.isSelected()) {
				imgView.setVisible(false);
			}
		});

		root.setOnMousePressed(event -> {
			if (!disabled && MouseButton.PRIMARY == event.getButton()) {
				setSelected(!btn.isSelected());
				event.consume();
			}
		});

	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
		Platform.runLater(() -> root.setOpacity(disabled ? 0.6 : 1));
	}

	public boolean isDiabled() {
		return disabled;
	}

	public void setSelected(boolean selected) {
		Platform.runLater(() -> {
			btn.setSelected(selected);
			imgView.setOpacity(1);
			imgView.setVisible(btn.isSelected());
		});
	}

	public ToggleButton getButton() {
		return btn;
	}

	public int getValue() {
		return value;
	}

}
