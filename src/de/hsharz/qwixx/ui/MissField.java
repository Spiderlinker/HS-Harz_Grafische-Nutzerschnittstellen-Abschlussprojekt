package de.hsharz.qwixx.ui;

import java.io.File;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class MissField {

	private StackPane pane;
	private ImageView imgView;

	private boolean disabled = false;
	private boolean selected = false;

	public MissField() {
		createWidgets();
		setupBindings();
	}

	private void createWidgets() {
		pane = new StackPane();
		pane.setPrefSize(30, 40);
		pane.setStyle("-fx-border-color: #999999; -fx-border-radius: 10px; -fx-border-width: 2px; "
				+ "-fx-background-color: white; -fx-background-radius: 11px;");

		imgView = new ImageView(new File("images/cross_klein.png").toURI().toString());
		imgView.setVisible(false);
		imgView.setMouseTransparent(true);

		pane.getChildren().add(imgView);
	}

	private void setupBindings() {
		pane.setOnMouseEntered(event -> {
			if (!isDisabled() && !isSelected()) {
				imgView.setOpacity(0.6);
				imgView.setVisible(true);
			}
		});
		pane.setOnMouseExited(event -> {
			if (!isDisabled() && !isSelected()) {
				imgView.setVisible(false);
			}
		});

		pane.setOnMousePressed(event -> {
			if (!isDisabled()) {
				selected = !selected;
				imgView.setOpacity(1);
				imgView.setVisible(selected);
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

	public void setSelected(boolean selected) {
		this.selected = selected;
		imgView.setVisible(selected);
	}

	public boolean isSelected() {
		return selected;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public Pane getPane() {
		return this.pane;
	}

}
