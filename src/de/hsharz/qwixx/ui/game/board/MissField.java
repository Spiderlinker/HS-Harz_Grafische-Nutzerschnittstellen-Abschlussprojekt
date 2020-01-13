package de.hsharz.qwixx.ui.game.board;

import java.util.HashSet;
import java.util.Set;

import de.hsharz.qwixx.ui.AbstractPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class MissField extends AbstractPane<StackPane> {

	private ImageView imgView;

	private boolean disabled = false;
	private boolean selected = false;

	private Set<MissFieldListener> listeners = new HashSet<>();

	public MissField() {
		super(new StackPane());

		createWidgets();
		setupBindings();
	}

	public void addListener(MissFieldListener l) {
		this.listeners.add(l);
	}

	public void removeListener(MissFieldListener l) {
		this.listeners.remove(l);
	}

	private void createWidgets() {
		root.setPrefSize(30, 40);
		root.setStyle("-fx-border-color: #999999; -fx-border-radius: 10px; -fx-border-width: 2px; "
				+ "-fx-background-color: white; -fx-background-radius: 11px;");

		imgView = new ImageView(getClass().getResource("/images/cross_klein.png").toString());
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
				listeners.forEach(MissFieldListener::userCrossedMiss);
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
