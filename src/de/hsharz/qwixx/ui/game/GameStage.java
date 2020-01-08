package de.hsharz.qwixx.ui.game;

import java.util.List;
import java.util.Objects;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialog.DialogTransition;

import de.hsharz.qwixx.model.Game;
import de.hsharz.qwixx.model.GameListener;
import de.hsharz.qwixx.model.player.IPlayer;
import de.hsharz.qwixx.ui.AbstractPane;
import de.hsharz.qwixx.ui.notification.Notification;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class GameStage extends AbstractPane<StackPane> implements GameListener {

	private Stage stage;
	private Scene scene;
	private Screen screen;

	private Game game;
	private GameUI gameUI;

	private HBox boxButtons;
	private JFXButton btnExit;
	private JFXButton btnHelp;

	private Notification notification;

	public GameStage(Game game, Screen screen) {
		super(new StackPane());
		this.game = Objects.requireNonNull(game);
		this.screen = Objects.requireNonNull(screen);

		this.game.addGameListener(this);

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
		boxButtons.setMaxHeight(50);
//		boxButtons.setMouseTransparent(true);

		btnExit = new JFXButton("Spiel beenden (Esc)");
		btnExit.setStyle("-fx-background-color: white;");
		btnHelp = new JFXButton("Hilfe");
		btnHelp.setStyle("-fx-background-color: white;");

		notification = new Notification();
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
		root.getChildren().add(notification.getPane());

		StackPane.setAlignment(boxButtons, Pos.TOP_CENTER);
		StackPane.setAlignment(notification.getPane(), Pos.BOTTOM_LEFT);
		StackPane.setMargin(notification.getPane(), new Insets(50));
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

	@Override
	public void nextPlayersTurn(IPlayer nextPlayer) {
		notification.show("Der nächste Spieler ist am Zug: " + nextPlayer.getName());
	}

	@Override
	public void invalidDiceChoiceMade(IPlayer player, String msg) {
		notification.show(
				"Deine gesetzten Kreuze sind nicht erlaubt! Bitte konzentriere dich und versuche es noch einmal!");
	}

	@Override
	public void gameOver() {
		showGameOverScreen();
	}

	private void showGameOverScreen() {
		List<IPlayer> winningPlayer = game.getWinningPlayer();
		BorderPane pane = new BorderPane();
		pane.setCenter(new Label(winningPlayer.toString()));
		Button btn = new Button("Beenden");
		pane.setBottom(btn);
		JFXDialog dialog = new JFXDialog(getPane(), pane, DialogTransition.CENTER);

		btn.setOnAction(e -> dialog.close());

		dialog.setOverlayClose(false);
		dialog.setOnDialogClosed(e -> stage.hide());
		Platform.runLater(dialog::show);
	}

}
