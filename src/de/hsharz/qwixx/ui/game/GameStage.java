package de.hsharz.qwixx.ui.game;

import java.util.Objects;

import com.jfoenix.controls.JFXButton;

import de.hsharz.qwixx.model.Game;
import de.hsharz.qwixx.ui.AbstractPane;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class GameStage extends AbstractPane<StackPane> {

	private Stage stage;
	private Scene scene;
	private Screen screen;

	private Game game;
	private GameUI gameUI;

	private HBox boxButtons;
	private JFXButton btnExit;
	private JFXButton btnHelp;

	public GameStage(Game game, Screen screen) {
		super(new StackPane());
		this.game = Objects.requireNonNull(game);
		this.screen = Objects.requireNonNull(screen);

		gameUI = new GameUI(game);

		Platform.runLater(() -> {
			createWidgets();
			setupInteractions();
			addWidgets();

			scaleGameUI();
		});
	}

	private void createWidgets() {
		scene = new Scene(root);

		stage = new Stage();
		stage.setScene(scene);
		stage.setFullScreenExitHint("Drücke 'Escape', um das Spiel zu verlassen");
		stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
		stage.setFullScreen(true);

		boxButtons = new HBox();
		boxButtons.setPadding(new Insets(10));
		boxButtons.setMouseTransparent(true);

		btnExit = new JFXButton("Spiel beenden (Esc)");
		btnExit.setStyle("-fx-background-color: white;");
		btnHelp = new JFXButton("Hilfe");
		btnHelp.setStyle("-fx-background-color: white;");
	}

	private void setupInteractions() {
		stage.addEventHandler(KeyEvent.KEY_PRESSED, e -> showExitGameScreen());
		btnExit.setOnAction(e -> showExitGameScreen());

	}

	private void addWidgets() {
		root.getChildren().add(gameUI.getPane());

		Region region = new Region();
		HBox.setHgrow(region, Priority.ALWAYS);

		boxButtons.getChildren().add(btnHelp);
		boxButtons.getChildren().add(region);
		boxButtons.getChildren().add(btnExit);

		root.getChildren().add(boxButtons);
	}

	private void scaleGameUI() {
		Pane gameUIPane = gameUI.getPane();
		Pane dicePane = gameUI.getDicePane().getPane();

		gameUIPane.applyCss();
		gameUIPane.layout();

		System.out.println(screen.getBounds());
		System.out.println(gameUIPane.getBoundsInLocal());
		System.out.println(dicePane.getBoundsInLocal());

		double scaleWidth = (screen.getBounds().getWidth()) / (gameUIPane.getBoundsInLocal().getWidth());
		double scaleHeight = (screen.getBounds().getHeight()) / (gameUIPane.getBoundsInLocal().getHeight());
		double scale = Math.min(scaleWidth, scaleHeight);

		System.out.println("Scale height: " + scaleHeight);
		System.out.println("Scale width: " + scaleWidth);

		gameUI.scaleGameUI(scale);
	}

	private void showExitGameScreen() {
		Alert gameExitAlert = new Alert(AlertType.CONFIRMATION, null, ButtonType.YES, ButtonType.NO);
		gameExitAlert.initOwner(stage);
		gameExitAlert.setHeaderText("Möchtest du das Spiel wirklich verlassen?");
		gameExitAlert.setTitle("Spiel beenden?");
		gameExitAlert.showAndWait();

		if (ButtonType.YES == gameExitAlert.getResult()) {
			game.stopGame();
			stage.hide();
		}
	}

	public void show() {
		this.stage.show();
	}

	public void hide() {
		this.stage.hide();
	}

	public Game getGame() {
		return game;
	}

}
