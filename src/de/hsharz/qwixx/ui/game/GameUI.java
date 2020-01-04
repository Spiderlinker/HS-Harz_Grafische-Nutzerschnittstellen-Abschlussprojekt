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
import de.hsharz.qwixx.ui.game.board.ComputerGameBoardUI;
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

			GameBoardUI boardUI = null;

			if (player instanceof Human) {
				boardUI = new GameBoardSimple(player);
				((Human) player).setHumanInputSupplier(boardUI);
			} else {
				boardUI = new ComputerGameBoardUI(player);
			}

			game.addGameListener(boardUI);
			boards.add(boardUI);
		}
	}

	private void addWidgets() {
		root.getChildren().add(gamePane);

//		gamePane.add(dicePane.getPane(), 1, 1);
		gamePane.add(new Group(dicePane.getPane()), 1, 1);
		GridPane.setHalignment(dicePane.getPane(), HPos.CENTER);
		GridPane.setValignment(dicePane.getPane(), VPos.CENTER);

		switch (boards.size()) {
		case 2:
			// Boards oben mittig und unten
			positionTwoGameBoards();
			break;
		case 3:
			// Boards links, rechts und unten
			positionThreeGameBoards();
			break;
		case 4:
			// Boards oben mittig, links, rechts und unten
			positionFourGameBoards();
			break;
		case 5:
			// Boards oben links, oben rechts, links, rechts und unten
			positionFiveGameBoards();
			break;
		default:
			throw new IllegalArgumentException(
					"Ung�ltige Anzahl an Spielbrettern! Min: 2; Max: 5; Current: " + boards.size());
		}

	}

	private void positionTwoGameBoards() {
		for (GameBoardUI board : boards) {
			if (board.getPlayer() instanceof Human) {
				addGameBoard(board, 1, 2);
			} else {
				addGameBoard(board, 1, 0);
			}
		}
	}

	private void positionThreeGameBoards() {
		int index = 0;
		for (GameBoardUI board : boards) {
			if (board.getPlayer() instanceof Human) {
				addGameBoard(board, 1, 2);
			} else {
				if (index == 0) {
					addGameBoard(board, 0, 1);
				} else {
					addGameBoard(board, 2, 1);
				}
				index++;
			}
		}
	}

	private void positionFourGameBoards() {
		int computerBoardCount = 0;
		for (GameBoardUI board : boards) {
			if (board.getPlayer() instanceof Human) {
				addGameBoard(board, 1, 2);
			} else {
				if (computerBoardCount == 0) {
					addGameBoard(board, 0, 1);
				} else if (computerBoardCount == 1) {
					addGameBoard(board, 1, 0);
				} else {
					addGameBoard(board, 2, 1);
				}
				computerBoardCount++;
			}
		}
	}

	private void positionFiveGameBoards() {
		int computerBoardCount = 0;
		for (GameBoardUI board : boards) {
			if (board.getPlayer() instanceof Human) {
				addGameBoard(board, 1, 2);
			} else {
				if (computerBoardCount == 0) {
					addGameBoard(board, 0, 1);
				} else if (computerBoardCount == 1) {
					addGameBoard(board, 0, 0, 2, 1);
				} else if (computerBoardCount == 2) {
					addGameBoard(board, 1, 0, 2, 1);
				} else {
					addGameBoard(board, 2, 1);
				}
				computerBoardCount++;
			}
		}
	}

	private void addGameBoard(GameBoardUI board, int column, int row) {
		addGameBoard(board, column, row, 1, 1);
	}

	private void addGameBoard(GameBoardUI board, int column, int row, int colSpan, int rowSpan) {
		Group boardGroup = new Group(board.getPane()); // group for scaling board and fit into place
		gamePane.add(boardGroup, column, row, colSpan, rowSpan);
		GridPane.setHalignment(boardGroup, HPos.CENTER);
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
		dicePane.getPane().setScaleX(scale);
		dicePane.getPane().setScaleY(scale);
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
		Platform.runLater(dialog::show);
	}

}
