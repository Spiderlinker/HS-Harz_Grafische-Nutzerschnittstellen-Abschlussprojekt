package de.hsharz.qwixx.ui;

import java.util.Objects;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;

import de.hsharz.qwixx.model.Game;
import de.hsharz.qwixx.model.board.GameBoard;
import de.hsharz.qwixx.model.player.Computer;
import de.hsharz.qwixx.model.player.Human;
import de.hsharz.qwixx.model.player.IPlayer;
import de.hsharz.qwixx.ui.game.GameUI;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Separator;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

public class StartGameScreen extends AbstractPane<GridPane> {

	private Stage stage;

	private Label lblCreateGame;
	private Label lblChoosePlayer;
	private JFXButton btnBack;
	private JFXButton btnPlay;

	private JFXTextField textName;
	private JFXComboBox<Integer> boxChoosePlayer;

	private Pane previousPane;

	public StartGameScreen(Stage stage, Pane previousPane) {
		super(new GridPane());

		this.stage = Objects.requireNonNull(stage);
		this.previousPane = Objects.requireNonNull(previousPane);

		createWidgets();
		setupInteractions();
		addWidgets();
	}

	private void createWidgets() {
		root.setPadding(new Insets(50));
		root.setHgap(10);
		root.setVgap(20);
		root.setStyle("-fx-background-color: white;");

		btnBack = new JFXButton("Zurück");
		btnBack.setStyle("-fx-border: none;");

		btnPlay = new JFXButton("Spiel starten");
		btnPlay.setStyle("-fx-font-size: 16pt;");
		btnPlay.setMaxWidth(Double.MAX_VALUE);

		textName = new JFXTextField();
		textName.setPromptText("Wie heißt du?");
		textName.setStyle("-fx-font-size: 16pt;");

		RequiredFieldValidator validator = new RequiredFieldValidator("Bitte gib deinen Namen ein");
		textName.getValidators().add(validator);
		textName.focusedProperty().addListener((o, oldValue, newValue) -> {
			if (!newValue.booleanValue()) {
				textName.validate();
			}
		});

		boxChoosePlayer = new JFXComboBox<>(FXCollections.observableArrayList(1, 2, 3, 4));
		boxChoosePlayer.getSelectionModel().select(2);
		boxChoosePlayer.setStyle("-fx-font-size: 16pt;");

		lblCreateGame = new Label("Spiel erstellen");
		lblCreateGame.setStyle("-fx-font-size: 50pt; -fx-font-family: Gabriola; ");
		lblChoosePlayer = new Label("Anzahl deiner Mitspieler: ");
		lblChoosePlayer.setStyle("-fx-font-size: 16pt;");

	}

	private void setupInteractions() {

		btnPlay.setOnAction(e -> startNewGame());

		root.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
			if (e.getCode() == KeyCode.ESCAPE) {
				showPreviousPane();
			}
		});

		btnBack.setOnAction(e -> showPreviousPane());
	}

	private void addWidgets() {
		root.setAlignment(Pos.CENTER);

		root.add(lblCreateGame, 0, 0, 2, 1);
		root.add(new Separator(Orientation.HORIZONTAL), 0, 1, 2, 1);
		root.add(btnBack, 0, 2);
		root.add(getSpacer(), 0, 3);
		root.add(textName, 0, 4, 2, 1);
		root.add(lblChoosePlayer, 0, 5);
		root.add(boxChoosePlayer, 1, 5);
		root.add(getSpacer(), 0, 6);
		root.add(btnPlay, 0, 7, 2, 1);

		GridPane.setMargin(textName, new Insets(0, 0, 20, 0));

		GridPane.setHalignment(lblCreateGame, HPos.CENTER);
		GridPane.setHgrow(lblCreateGame, Priority.ALWAYS);
		GridPane.setHgrow(textName, Priority.ALWAYS);
		GridPane.setHgrow(lblCreateGame, Priority.ALWAYS);
		GridPane.setHgrow(btnPlay, Priority.ALWAYS);
	}

	private Node getSpacer() {
		Region spacer = new Region();
		GridPane.setVgrow(spacer, Priority.ALWAYS);
		return spacer;
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

	private void startNewGame() {

		if (textName.getText().trim().isEmpty()) {
			textName.requestFocus();
			return;
		}

		JFXDialog loadingDialog = new JFXDialog();
		loadingDialog.setContent(new ProgressIndicator(-1));
		loadingDialog.show((StackPane) stage.getScene().getRoot());

		new Thread(() -> {
			Game game = new Game();

			{
				GameBoard board = new GameBoard();
				board.setRowClosedSupplier(game);
				IPlayer player = new Human(textName.getText(), board);
				game.addPlayer(player);
			}

			for (int i = 0; i < boxChoosePlayer.getSelectionModel().getSelectedItem(); i++) {
				GameBoard board = new GameBoard();
				board.setRowClosedSupplier(game);

				IPlayer player = new Computer("Computer #" + (i + 1), board);
				game.addPlayer(player);
			}

			GameUI gameUI = new GameUI(game);
			GridPane pane = gameUI.getPane();

			Scene scene = new Scene(pane);
			pane.applyCss();
			pane.layout();

			ObservableList<Screen> screensForRectangle = Screen.getScreensForRectangle(this.stage.getX(),
					this.stage.getY(), this.stage.getWidth(), this.stage.getHeight());
			Screen screen = screensForRectangle.get(0);

			double scaleX = screen.getBounds().getWidth() / pane.getBoundsInLocal().getWidth();
			double scaleY = screen.getBounds().getHeight() / pane.getBoundsInLocal().getHeight();
			double scale = Math.min(scaleX, scaleY);

			System.out.println("Scaling to: " + scale);

			pane.setScaleX(scale);
			pane.setScaleY(scale);

			Platform.runLater(() -> {
				Stage stage = new Stage();
				stage.setScene(scene);
				stage.setFullScreen(true);
				stage.setFullScreenExitHint("Drücke 'Escape', um das Spiel zu verlassen");
				stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
				stage.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
					Alert gameExitAlert = new Alert(AlertType.CONFIRMATION, null,
							ButtonType.YES, ButtonType.NO);
					gameExitAlert.initOwner(stage);
					gameExitAlert.setHeaderText("Möchtest du das Spiel wirklich verlassen?");
					gameExitAlert.setTitle("Spiel beenden?");
					gameExitAlert.showAndWait();

					if (ButtonType.YES == gameExitAlert.getResult()) {
						game.stopGame();
						stage.hide();
					}
				});
				loadingDialog.close();
				stage.show();
			});

			new Thread(game::startGame).start();
		}).start();

	}

}
