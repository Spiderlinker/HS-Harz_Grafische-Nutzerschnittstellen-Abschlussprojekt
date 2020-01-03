package de.hsharz.qwixx.ui.game;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialog.DialogTransition;

import de.hsharz.qwixx.model.Game;
import de.hsharz.qwixx.model.player.Human;
import de.hsharz.qwixx.model.player.IPlayer;
import de.hsharz.qwixx.ui.AbstractPane;
import de.hsharz.qwixx.ui.game.board.GameBoardSimple;
import de.hsharz.qwixx.ui.game.board.GameBoardUI;
import de.hsharz.qwixx.ui.game.dice.DicePane;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class GameUI extends AbstractPane<StackPane> implements GameListener {

	private Game game;
	private List<GameBoardUI> boards = new ArrayList<>();
	private GridPane gamePane;
	private DicePane dicePane;

	public GameUI(Game game) {
		super(new StackPane());

		this.game = Objects.requireNonNull(game);
		this.game.addGameListener(this);

		createWidgets();
		addWidgets();
	}

	private void createWidgets() {
		createPlayerBoards();

		gamePane = new GridPane();
		gamePane.setHgap(10);
		gamePane.setVgap(5);
		gamePane.setAlignment(Pos.CENTER);

		gamePane.setBackground(
				new Background(new BackgroundImage(new Image(new File("images/background.jpg").toURI().toString()),
						BackgroundRepeat.NO_REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT,
						new BackgroundSize(100, 100, true, true, true, true))));

		dicePane = new DicePane(game.getDices());
		game.addDiceListener(dicePane);
		game.addGameListener(dicePane);
	}

	private void createPlayerBoards() {
		for (IPlayer player : game.getPlayer()) {
			GameBoardUI boardUI = new GameBoardSimple(player);
			game.addGameListener(boardUI);

			if (player instanceof Human) {
				((Human) player).setHumanInputSupplier(boardUI);
			}

			boards.add(boardUI);
		}
	}

	private void addWidgets() {
		root.getChildren().add(gamePane);

		gamePane.add(dicePane.getPane(), 1, 1);
//		gamePane.add(new Group(dicePane.getPane()), 1, 1);
		GridPane.setHalignment(dicePane.getPane(), HPos.CENTER);
		GridPane.setValignment(dicePane.getPane(), VPos.CENTER);

		switch (boards.size()) {

		case 2:
			// Boards oben mittig und unten
			for (GameBoardUI board : boards) {
				Group group = new Group(board.getPane());
				if (board.getPlayer() instanceof Human) {
					gamePane.add(group, 1, 2);
					GridPane.setHalignment(group, HPos.CENTER);
				} else {
					gamePane.add(group, 1, 0);
					GridPane.setHalignment(group, HPos.CENTER);
				}
			}
			break;
		case 3:
			// Boards links, rechts und unten
			int index = 0;
			for (GameBoardUI board : boards) {
				Group group = new Group(board.getPane());
				if (board.getPlayer() instanceof Human) {
					gamePane.add(group, 1, 2);
					GridPane.setHalignment(group, HPos.CENTER);
				} else {
					if (index == 0) {
						gamePane.add(group, 0, 1);
					} else {
						gamePane.add(group, 2, 1);
					}
					index++;
				}
			}
			break;
		case 4:
			// Boards oben mittig, links, rechts und unten
			index = 0;
			for (GameBoardUI board : boards) {
				Group group = new Group(board.getPane());
				if (board.getPlayer() instanceof Human) {
					gamePane.add(group, 1, 2);
					GridPane.setHalignment(group, HPos.CENTER);
				} else {
					if (index == 0) {
						gamePane.add(group, 0, 1);
					} else if (index == 1) {
						gamePane.add(group, 1, 0);
						GridPane.setHalignment(group, HPos.CENTER);
					} else {
						gamePane.add(group, 2, 1);
					}
					index++;
				}
			}
			break;
		case 5:
			// Boards oben links, oben rechts, links, rechts und unten
			index = 0;
			for (GameBoardUI board : boards) {
				Group group = new Group(board.getPane());
				if (board.getPlayer() instanceof Human) {
					gamePane.add(group, 1, 2);
					GridPane.setHalignment(group, HPos.CENTER);
				} else {
					if (index == 0) {
						gamePane.add(group, 0, 1);
					} else if (index == 1) {
						gamePane.add(group, 0, 0, 2, 1);
						GridPane.setHalignment(group, HPos.CENTER);
					} else if (index == 2) {
						gamePane.add(group, 1, 0, 2, 1);
						GridPane.setHalignment(group, HPos.CENTER);
					} else {
						gamePane.add(group, 2, 1);
					}
					index++;
				}
			}
			break;
		default:
			throw new IllegalArgumentException(
					"Ungültige Anzahl an Spielbrettern! Min: 2; Max: 5; Current: " + boards.size());
		}

	}

	public List<GameBoardUI> getPlayerGameBoards() {
		return boards;
	}

	public DicePane getDicePane() {
		return dicePane;
	}

	public void scaleGameUI(double scale) {
		boards.forEach(boardUI -> {
			boardUI.getPane().setScaleX(scale);
			boardUI.getPane().setScaleY(scale);
		});
//		dicePane.getPane().setScaleX(scale);
//		dicePane.getPane().setScaleY(scale);
	}

	@Override
	public void nextPlayersTurn(IPlayer nextPlayer) {
		// ignore
	}

	@Override
	public void gameOver() {
		showGameOverScreen();
	}

	private void showGameOverScreen() {
		List<IPlayer> winningPlayer = game.getWinningPlayer();
		JFXDialog dialog = new JFXDialog(getPane(), new Label(winningPlayer.toString()), DialogTransition.CENTER);
		dialog.setOverlayClose(false);
		dialog.setOnDialogClosed(e -> {
			System.out.println("Should exit...");
		});
		Platform.runLater(() -> dialog.show());
	}

}
