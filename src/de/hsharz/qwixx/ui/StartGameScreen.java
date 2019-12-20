package de.hsharz.qwixx.ui;

import java.util.Objects;

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
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class StartGameScreen extends AbstractPane<GridPane> {

	private Stage stage;

	private Label lblCreateGame;
	private Label lblChoosePlayer;
	private Button btnBack;
	private Button btnPlay;

	private TextField textName;
	private ChoiceBox<Integer> boxChoosePlayer;

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

		btnBack = new Button("Zurück");
		btnBack.setStyle("-fx-border: none;");

		btnPlay = new Button("Spiel starten");
		btnPlay.setStyle("-fx-font-size: 16pt;");
		btnPlay.setMaxWidth(Double.MAX_VALUE);

		textName = new TextField();
		textName.setPromptText("Wie heißt du?");
		textName.setStyle("-fx-font-size: 16pt;");

		boxChoosePlayer = new ChoiceBox<>(FXCollections.observableArrayList(1, 2, 3, 4));
		boxChoosePlayer.getSelectionModel().select(2);
		boxChoosePlayer.setStyle("-fx-font-size: 16pt;");

		lblCreateGame = new Label("Spiel erstellen");
		lblCreateGame.setStyle("-fx-font-size: 32pt;");
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

		stage.getScene().setRoot(new ScrollPane(pane));
		stage.setFullScreen(true);

		double scaleX = stage.getWidth() / pane.getWidth();
		double scaleY = stage.getHeight() / pane.getHeight();
		double scale = Math.min(scaleX, scaleY);
		
		pane.setScaleX(scale);
		pane.setScaleY(scale);
		stage.getScene().setRoot(new BorderPane(new Group(pane)));

		new Thread(game::startGame).start();
	}

}
