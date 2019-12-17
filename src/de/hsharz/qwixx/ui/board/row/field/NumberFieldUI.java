package de.hsharz.qwixx.ui;

import java.io.File;

import de.hsharz.qwixx.model.dice.DiceColor;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

public class CrossButton {

	private int value;
	private String color;

	private StackPane pane;
	private ToggleButton btn;
	private ImageView imgView;

	private boolean disabled = false;
	private boolean locked = false;

	public CrossButton(int value, DiceColor textColor) {
		this.value = value;
		this.color = textColor.getAsHex();

		createWidgets();
		setupBindings();
	}

	private void createWidgets() {
		pane = new StackPane();

		btn = new ToggleButton(Integer.toString(value));
		btn.setStyle("-fx-background-color: white; -fx-text-fill: " + color + "; -fx-font-size: 18pt;");
		btn.setOpacity(0.85);
		btn.setMinWidth(Region.USE_PREF_SIZE);
		btn.setMaxWidth(Double.MAX_VALUE);
		btn.setMouseTransparent(true);

		imgView = new ImageView(new File("images/cross.png").toURI().toString());
		imgView.setScaleX(0.8);
		imgView.setScaleY(0.8);
		imgView.setScaleZ(0.8);
		imgView.setVisible(false);
		imgView.setMouseTransparent(true);

		pane.getChildren().add(btn);
		pane.getChildren().add(imgView);
	}

	private void setupBindings() {
		pane.setOnMouseEntered(event -> {
			if (!locked && !disabled && !btn.isSelected()) {
				imgView.setOpacity(0.6);
				imgView.setVisible(true);
			}
		});
		pane.setOnMouseExited(event -> {
			if (!locked && !btn.isSelected()) {
				imgView.setVisible(false);
			}
		});

		pane.setOnMousePressed(event -> {
			if (!locked && !disabled && MouseButton.PRIMARY == event.getButton()) {
				btn.setSelected(!btn.isSelected());
				imgView.setOpacity(1);
				imgView.setVisible(btn.isSelected());
				event.consume();
			}
		});

	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
		if (disabled) {
			pane.setOpacity(0.6);
		} else {
			pane.setOpacity(1);
		}
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public boolean isLocked() {
		return isLocked();
	}

	public boolean isDiabled() {
		return disabled;
	}

	public ToggleButton getButton() {
		return this.btn;
	}
	
	public void showCrossImage() {
		imgView.setOpacity(1);
		imgView.setVisible(true);
	}
	
	public Pane getPane() {
		return this.pane;
	}

	public int getValue() {
		return value;
	}

}
