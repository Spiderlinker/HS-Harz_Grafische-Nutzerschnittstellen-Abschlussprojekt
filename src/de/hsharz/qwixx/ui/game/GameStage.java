package de.hsharz.qwixx.ui.game;

import java.util.Objects;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialog.DialogTransition;

import de.hsharz.qwixx.model.Game;
import de.hsharz.qwixx.model.GameListener;
import de.hsharz.qwixx.model.player.Human;
import de.hsharz.qwixx.model.player.IPlayer;
import de.hsharz.qwixx.ui.AbstractPane;
import de.hsharz.qwixx.ui.notification.Notification;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
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
	private JFXButton btnControls;
	private JFXButton btnHelp;
	private JFXCheckBox btnShowNotifications;

	private GameOverDialog dialogGameOver;
	private JFXDialog dialogHelp;
	private JFXDialog dialogControls;
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
		boxButtons.setFillHeight(true);
		boxButtons.setMaxHeight(50);
		boxButtons.setSpacing(10);

		btnExit = new JFXButton("Spiel beenden (Esc)");
		btnExit.setStyle("-fx-background-color: rgba(255, 255, 255, 0.6); -fx-background-radius: 0px;");
		btnExit.setMaxHeight(Double.MAX_VALUE);
		btnHelp = new JFXButton("Hilfe");
		btnHelp.setStyle("-fx-background-color: rgba(255, 255, 255, 0.6); -fx-background-radius: 0px;");
		btnHelp.setMaxHeight(Double.MAX_VALUE);
		btnControls = new JFXButton("Steuerung");
		btnControls.setStyle("-fx-background-color: rgba(255, 255, 255, 0.6); -fx-background-radius: 0px;");
		btnControls.setMaxHeight(Double.MAX_VALUE);

		btnShowNotifications = new JFXCheckBox("Hinweise anzeigen");
		btnShowNotifications.setPadding(new Insets(5));
		btnShowNotifications.setStyle("-fx-background-color: rgba(255, 255, 255, 0.6); -fx-background-radius: 0px;");
		btnShowNotifications.setSelected(true);
		btnShowNotifications.setMaxHeight(Double.MAX_VALUE);

		notification = new Notification();
		dialogGameOver = new GameOverDialog(getPane());
		dialogHelp = createHelpDialog();
		dialogControls = createControlsDialog();
	}

	private JFXDialog createHelpDialog() {
		TextFlow text = new TextFlow();
		text.setPadding(new Insets(50));

		Text textGoalHeader = getBoldText("Spielziel\n");
		Text textGoal = getNormalText(
				"Ziel des Spiels ist es, über mehrere Runden verteilt so viele Kreuze wie möglich in den vier Zahlenreihen des eigenen Blocks einzutragen und so die meisten Punkte zu sammeln.\n\n");

		Text textAblaufHeader = getBoldText("Spielablauf\n");
		Text textAblauf = getNormalText(
				"Wer am Zug ist und somit der aktive Spieler ist, wirfst mit alle Würfeln genau einmal. Alle Spieler dürfen (nicht müssen!) nun die Summe der geworfenen weißen Würfeln in einer beliebigen Farbreihe auf Ihrem Spielblock ankreuzen.\n"
						+ "Der aktive Spieler darf nun zusätzlich die Augenzahl eines (beliebigen) weißen Würfels mit der Augenzahl eines (beliebigen) Farbwürfels addieren und die Summe in die Farbreihe (entsprechend des gewählten Farbwürfels) eintragen.\n\n"
						+ "Falls ein Spieler keine Zahl akreuzen will oder kann, so muss er ein Kreuz in der Spalte \"Fehlwürfe\" machen. Für nicht aktive Spieler gilt diese Regel nicht.\n\n");

		Text textHintHeader = getBoldText("Hinweise zum Abkreuzen von Feldern\n");
		Text textHint = getNormalText("– Alle Zahlen in jeder Reihe müssen von links nach rechts angekreuzt werden.\n"
				+ "– Es dürfen Zahlen ausgelassen werden.\n"
				+ "– Ausgelassene Ziffern dürfen nicht nachträglich angekreuzt werden.\n"
				+ "- Um eine Reihe beenden zu können (und somit das Schloss am Ende dieser Reihe anzukreuzen), müssen in der Reihe mindestens 6 Kreuze gesetzt worden sein, wobei eines hiervon die letze Zahl in der Reihe sein muss (12 oder 2). "
				+ "Falls ein Spieler mit weniger als 6 Kreuzen die letzte Zahl in der Reihe ankreuzt, so wird die Reihe von ihm nicht beendet und andere Spieler können weiterhin in dieser Reihe Kreuze setzen.\n\n");

		Text textFinishHeader = getBoldText("Spielende\n");
		Text textFinish = getNormalText(
				"Qwixx endet, sobald ein Spieler seinen vierten Fehlwurf angekreuzt hat oder die zweite Reihe beendet wurde.");

		text.getChildren().add(textGoalHeader);
		text.getChildren().add(textGoal);
		text.getChildren().add(textAblaufHeader);
		text.getChildren().add(textAblauf);

		text.getChildren().add(textHintHeader);
		text.getChildren().add(textHint);

		text.getChildren().add(textFinishHeader);
		text.getChildren().add(textFinish);

		return new JFXDialog(getPane(), text, DialogTransition.CENTER);
	}

	private JFXDialog createControlsDialog() {
		GridPane pane = new GridPane();
		pane.setPadding(new Insets(50));
		pane.setVgap(40);
		pane.setHgap(40);

		Text textSelectHeader = getBoldText("Feld ankreuzen:\t\t");
		Text textSelect = getBoldTextSmall("Linke Maustaste");

		Text textDismissHeader = getBoldText("Würfelpaar\nnicht verwenden:\t");
		Text textDismiss = getBoldTextSmall("Rechte Maustaste");
		Text textDismiss2 = getNormalText(" (auf deinem Spielbrett) oder ");
		ImageView imgNextButton = new ImageView(
				new Image(getClass().getResource("/images/arrow_right.png").toString()));
		imgNextButton.setFitHeight(20);
		imgNextButton.setFitWidth(20);
		Text textDismiss3 = getNormalTextSmall("\nFalls du sowohl die weißen Würfel nicht verwendest " //
				+ "als auch keinen Farbwürfel, so wird automatisch " //
				+ "ein Fehlwurf angekreuzt.");

		Text textExitHeader = getBoldText("Spiel verlassen:\t\t");
		Text textExit = getBoldTextSmall("Taste Esc");
		Text textExit2 = getNormalText(" (alternativ Schaltfläche oben rechts)");

		TextFlow flowDismiss = new TextFlow(textDismiss, textDismiss2, imgNextButton, textDismiss3);
		TextFlow flowExit = new TextFlow(textExit, textExit2);

		pane.add(textSelectHeader, 0, 0);
		pane.add(textSelect, 1, 0);
		pane.add(textDismissHeader, 0, 1);
		pane.add(flowDismiss, 1, 1);
		pane.add(textExitHeader, 0, 2);
		pane.add(flowExit, 1, 2);

		return new JFXDialog(getPane(), pane, DialogTransition.CENTER);
	}

	private void setupInteractions() {
		stage.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
			if (e.getCode() == KeyCode.ESCAPE) {

				if (dialogHelp.isVisible()) {
					dialogHelp.close();
					return;
				}

				if (dialogControls.isVisible()) {
					dialogControls.close();
					return;
				}

				showExitGameScreen();
			}
		});
		root.setOnMouseClicked(e -> {
			if (btnShowNotifications.isSelected() && MouseButton.SECONDARY == e.getButton()) {
				notification.show("Bitte klicke *über* deinem Spielblock die rechte Maustaste,\n"
						+ "um dieses Würfelpaar nicht zu verwenden.");
			}
		});
		btnExit.setOnAction(e -> showExitGameScreen());
		btnHelp.setOnAction(e -> dialogHelp.show());
		btnControls.setOnAction(e -> dialogControls.show());
		dialogGameOver.getDialog().setOnDialogClosed(e -> stage.hide());
	}

	private Text getBoldText(String text) {
		Text boldText = new Text(text);
		boldText.setStyle("-fx-font-weight: bold; -fx-font-size: 16pt;");
		return boldText;
	}

	private Text getBoldTextSmall(String text) {
		Text boldText = new Text(text);
		boldText.setStyle("-fx-font-weight: bold; -fx-font-size: 14pt;");
		return boldText;
	}

	private Text getNormalText(String text) {
		Text normalText = new Text(text);
		normalText.setStyle("-fx-font-size: 14pt;");
		return normalText;
	}

	private Text getNormalTextSmall(String text) {
		Text normalText = new Text(text);
		normalText.setStyle("-fx-font-size: 12pt;");
		return normalText;
	}

	private void addWidgets() {
		root.getChildren().add(gameUI.getPane());

		Region region = new Region();
		HBox.setHgrow(region, Priority.ALWAYS);

		boxButtons.getChildren().add(btnHelp);
		boxButtons.getChildren().add(btnControls);
		boxButtons.getChildren().add(btnShowNotifications);
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
		if (btnShowNotifications.isSelected()) {
			if (nextPlayer instanceof Human) {
				notification.show("Du bist am Zug!\nBitte wähle zuerst aus den weißen, dann aus den farbigen Würfeln.");
			} else {
				notification.show(nextPlayer.getName() + " ist am Zug!\nDu kannst nun die weißen Würfel verwenden.");
			}
		}
	}

	@Override
	public void invalidDiceChoiceMade(IPlayer player, String msg) {
		if (btnShowNotifications.isSelected()) {
			notification.show(
					"Dein gesetztes Kreuz ist nicht erlaubt!\nBitte konzentriere dich und versuche es noch einmal!");
		}
	}

	@Override
	public void gameOver() {
		showGameOverScreen();
	}

	private void showGameOverScreen() {
		dialogGameOver.updateWinningPlayer(game.getWinningPlayer());
		Platform.runLater(() -> dialogGameOver.getDialog().show());
	}

}
