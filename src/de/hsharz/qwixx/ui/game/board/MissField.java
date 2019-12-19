package de.hsharz.qwixx.ui.game.board;

import java.io.File;

import de.hsharz.qwixx.ui.AbstractPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class MissField extends AbstractPane<StackPane> {

	private ImageView imgView;

	private boolean disabled = false;
	private boolean selected = false;

	public MissField() {
		super(new StackPane());

		createWidgets();
		setupBindings();
	}

	private void createWidgets() {
		root.setPrefSize(30, 40);
		root.setStyle("-fx-border-color: #999999; -fx-border-radius: 10px; -fx-border-width: 2px; "
				+ "-fx-background-color: white; -fx-background-radius: 11px;");

		imgView = new ImageView(new File("images/cross_klein.png").toURI().toString());
		imgView.setVisible(false);
		imgView.setMouseTransparent(true);

		root.getChildren().add(imgView);
	}

	private void setupBindings() {
		root.setOnMouseEntered(event -> {
			if (!isDisabled() && !isSelected()) {
				imgView.setOpacity(0.6);
				imgView.setVisible(true);
			}
		});
		root.setOnMouseExited(event -> {
			if (!isDisabled() && !isSelected()) {
				imgView.setVisible(false);
			}
		});

		root.setOnMousePressed(event -> {
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
			root.setOpacity(0.6);
		} else {
			root.setOpacity(1);
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

}
