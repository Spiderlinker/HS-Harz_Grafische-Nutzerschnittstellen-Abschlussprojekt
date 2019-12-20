package de.hsharz.qwixx.ui;

import java.util.Objects;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class StartScreen extends AbstractPane<VBox> {

	private Stage stage;

	private Label lblQwixx;
	private Separator separator;
	private Button btnPlay;
	private Button btnStatistics;
	private Button btnExit;

	private StartGameScreen startGameScreen;

	public StartScreen(Stage stage) {
		super(new VBox());

		this.stage = Objects.requireNonNull(stage);

		createWidgets();
		setupInteractions();
		addWidgets();

	}

	private void createWidgets() {
		root.setPadding(new Insets(50));
		root.setSpacing(20);
		root.setAlignment(Pos.CENTER);
		root.setStyle("-fx-background-color: white;");

		lblQwixx = new Label("Qwixx");
		lblQwixx.setStyle("-fx-font-size: 40pt;");
		lblQwixx.setAlignment(Pos.CENTER);

		separator = new Separator(Orientation.HORIZONTAL);

		btnPlay = new Button("Spiel starten");
		btnPlay.setStyle("-fx-font-size: 16pt;");
		btnPlay.setMaxWidth(Double.MAX_VALUE);

		btnStatistics = new Button("Statistik");
		btnStatistics.setStyle("-fx-font-size: 16pt;");
		btnStatistics.setMaxWidth(Double.MAX_VALUE);

		btnExit = new Button("Verlassen");
		btnExit.setStyle("-fx-font-size: 16pt;");
		btnExit.setMaxWidth(Double.MAX_VALUE);
	}

	private void setupInteractions() {
		btnPlay.setOnAction(e -> showStartGameScreen());
		btnExit.setOnAction(e -> Platform.exit());
	}

	private void addWidgets() {
		root.getChildren().add(lblQwixx);
		root.getChildren().add(separator);
		root.getChildren().add(getSpacer());
		root.getChildren().add(btnPlay);
		root.getChildren().add(btnStatistics);
		root.getChildren().add(getSpacer());
		root.getChildren().add(btnExit);

		VBox.setVgrow(separator, Priority.ALWAYS);
	}

	private Node getSpacer() {
		Region spacer = new Region();
		VBox.setVgrow(spacer, Priority.ALWAYS);
		return spacer;
	}

	private void showStartGameScreen() {
		if (startGameScreen == null) {
			startGameScreen = new StartGameScreen(stage, getPane());
		}

		Scene scene = getPane().getScene();
		Pane paneToShow = startGameScreen.getPane();

		paneToShow.translateXProperty().set(scene.getWidth());

		StackPane parentContainer = (StackPane) scene.getRoot();
		parentContainer.getChildren().add(paneToShow);

		Timeline timeline = new Timeline();
		KeyValue keyValue = new KeyValue(paneToShow.translateXProperty(), 0, Interpolator.EASE_IN);
		KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.3), keyValue);
		timeline.getKeyFrames().add(keyFrame);
		timeline.setOnFinished(e -> parentContainer.getChildren().remove(getPane()));
		timeline.play();
	}

}
