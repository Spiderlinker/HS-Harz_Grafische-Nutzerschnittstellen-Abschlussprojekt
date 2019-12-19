package de.hsharz.qwixx.ui;

import java.util.Objects;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class StartGameScreen extends AbstractPane<VBox> {

	private Label lblCreateGame;
	private Label lblChoosePlayer;
	private Button btnBack;
	private Button btnPlay;

	private TextField textName;
	private ChoiceBox<Integer> boxChoosePlayer;

	private Pane previousPane;

	public StartGameScreen(Pane previousPane) {
		super(new VBox());

		this.previousPane = Objects.requireNonNull(previousPane);

		createWidgets();
		setupInteractions();
		addWidgets();
	}

	private void createWidgets() {
		root.setPadding(new Insets(20));
		root.setSpacing(10);
		root.setStyle("-fx-background-color: white;");

		btnBack = new Button("Zurück");
		btnPlay = new Button("Spiel starten");

		textName = new TextField();
		textName.setPromptText("Wie heißt du?");

		boxChoosePlayer = new ChoiceBox<>(FXCollections.observableArrayList(1, 2, 3, 4));

		Slider slider = new Slider(1, 4, 2);
		slider.setSnapToTicks(true);
		slider.setMinorTickCount(0);
		slider.setMajorTickUnit(1);
		slider.setShowTickLabels(true);
		slider.setShowTickMarks(true);
		root.getChildren().add(slider);

		lblCreateGame = new Label("Neues Spiel erstellen");
		lblChoosePlayer = new Label("Anzahl der Computergegner: ");

	}

	private void setupInteractions() {

		root.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
			if (e.getCode() == KeyCode.ESCAPE) {
				showPreviousPane();
			}
		});

		btnBack.setOnAction(e -> showPreviousPane());
	}

	private void addWidgets() {
		root.getChildren().add(btnBack);
		root.getChildren().add(lblCreateGame);
		root.getChildren().add(textName);
		root.getChildren().add(lblChoosePlayer);
		root.getChildren().add(boxChoosePlayer);
		root.getChildren().add(btnPlay);
	}

	private void showPreviousPane() {
		Scene scene = getPane().getScene();
		StackPane parentContainer = (StackPane) scene.getRoot();

		parentContainer.getChildren().add(0, previousPane);

		Timeline timeline = new Timeline();
		KeyValue keyValue = new KeyValue(getPane().translateXProperty(), scene.getWidth(), Interpolator.EASE_IN);
		KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.3), keyValue);
		timeline.getKeyFrames().add(keyFrame);
		timeline.setOnFinished(e -> parentContainer.getChildren().remove(getPane()));
		timeline.play();

	}

}
