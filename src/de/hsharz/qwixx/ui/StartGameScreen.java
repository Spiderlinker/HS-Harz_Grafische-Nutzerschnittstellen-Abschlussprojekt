package de.hsharz.qwixx.ui;

import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

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
import de.hsharz.qwixx.ui.game.GameStage;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Separator;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
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
		root.setStyle("-fx-background-color: #ffffff;");

		btnBack = new JFXButton("", new ImageView(getClass().getResource("/images/arrow_left.png").toString()));
		btnBack.setFocusTraversable(false);

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
			if (e.getCode() == KeyCode.ENTER) {
				btnPlay.fire();
			} else if (e.getCode() == KeyCode.ESCAPE) {
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

		JFXDialog loadingDialog = showLoadingScreen();

		ObservableList<Screen> screensForRectangle = Screen.getScreensForRectangle(this.stage.getX(), this.stage.getY(),
				this.stage.getWidth(), this.stage.getHeight());

		if (screensForRectangle.isEmpty()) {
			System.err.println("Kein Display gefunden, auf dem die Stage angezeigt wird!");
			return;
		}

		Screen screen = screensForRectangle.get(0);

		Task<GameStage> createUITask = new Task<GameStage>() {
			@Override
			protected GameStage call() throws Exception {
				Game game = createNewGame();
				return new GameStage(game, screen);
			}
		};

		createUITask.setOnRunning(e -> loadingDialog.show());
		createUITask.setOnSucceeded(e -> {
			loadingDialog.close();
			try {
				GameStage gameStage = createUITask.get();
				gameStage.show();
				gameStage.getGame().startGame();
			} catch (InterruptedException | ExecutionException e1) {
				e1.printStackTrace();
			}
		});

		new Thread(createUITask).start();
	}

	private JFXDialog showLoadingScreen() {
		JFXDialog dialog = new JFXDialog();
		dialog.setDialogContainer((StackPane) stage.getScene().getRoot());
		dialog.setContent(new ProgressIndicator(-1));
		return dialog;
	}

	private Game createNewGame() {
		Game game = new Game();

		addHumanPlayer(game);
		addComputerPlayer(game);

		return game;
	}

	private void addHumanPlayer(Game game) {
		addPlayer(game, board -> new Human(textName.getText(), board));
	}

	private void addComputerPlayer(Game game) {
		for (int i = 0; i < boxChoosePlayer.getSelectionModel().getSelectedItem(); i++) {
			String name = "Computer #" + (i + 1);
			addPlayer(game, board -> new Computer(name, board));
		}
	}

	private void addPlayer(Game game, Function<GameBoard, IPlayer> playerSupplier) {
		GameBoard board = new GameBoard();
		board.setRowClosedSupplier(game);
		IPlayer player = playerSupplier.apply(board);
		game.addPlayer(player);
	}

}
