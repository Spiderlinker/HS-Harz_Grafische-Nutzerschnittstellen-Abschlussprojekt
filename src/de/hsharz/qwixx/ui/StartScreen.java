package de.hsharz.qwixx.ui;

import java.util.Objects;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class StartScreen extends AbstractPane<VBox> {

	private Stage stage;

	private Label lblQwixx;
	private Separator separator;
	private JFXButton btnPlay;
	private JFXButton btnCredits;
	private JFXButton btnExit;

	private StartGameScreen startGameScreen;
	private JFXDialog creditsDialog;

	public StartScreen(Stage stage) {
		super(new VBox());

		this.stage = Objects.requireNonNull(stage);

		createWidgets();
		setupInteractions();
		addWidgets();

	}

	private void createWidgets() {
		startGameScreen = new StartGameScreen(stage, getPane());
		new Scene(startGameScreen.getPane());
		startGameScreen.getPane().applyCss();
		startGameScreen.getPane().layout();

		root.setPadding(new Insets(50));
		root.setSpacing(20);
		root.setAlignment(Pos.CENTER);
		root.setStyle("-fx-background-color: linear-gradient(to bottom, #363542, #3095DD);");

		lblQwixx = new Label("Qwixx");
		lblQwixx.setStyle("-fx-font-size: 72pt; -fx-font-family: Gabriola; -fx-text-fill: white;");
		lblQwixx.setAlignment(Pos.CENTER);

		separator = new Separator(Orientation.HORIZONTAL);

		btnPlay = new JFXButton("Spiel starten");
		btnPlay.setStyle("-fx-font-size: 16pt; -fx-text-fill: white;");
		btnPlay.setMaxWidth(Double.MAX_VALUE);

		btnCredits = new JFXButton("Credits");
		btnCredits.setStyle("-fx-font-size: 16pt; -fx-text-fill: white;");
		btnCredits.setMaxWidth(Double.MAX_VALUE);

		btnExit = new JFXButton("Verlassen");
		btnExit.setStyle("-fx-font-size: 16pt; -fx-text-fill: white;");
		btnExit.setMaxWidth(Double.MAX_VALUE);
	}

	private void setupInteractions() {
		btnPlay.setOnAction(e -> showStartGameScreen());
		btnCredits.setOnAction(e -> showCredits());
		btnExit.setOnAction(e -> Platform.exit());
	}

	private void addWidgets() {
		root.getChildren().add(lblQwixx);
		root.getChildren().add(separator);
		root.getChildren().add(UiUtils.getVBoxSpacer());
		root.getChildren().add(btnPlay);
		root.getChildren().add(btnCredits);
		root.getChildren().add(UiUtils.getVBoxSpacer());
		root.getChildren().add(btnExit);

		VBox.setVgrow(separator, Priority.ALWAYS);
	}

	private void showStartGameScreen() {
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

	private void showCredits() {
		if (creditsDialog == null) {
			creditsDialog = new JFXDialog();
			creditsDialog.setContent(new AboutDialog().getPane());
		}
		creditsDialog.show((StackPane) getPane().getScene().getRoot());
	}
}
