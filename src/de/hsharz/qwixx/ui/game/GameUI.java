package de.hsharz.qwixx.ui.game;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.jfoenix.controls.JFXButton;

import de.hsharz.qwixx.model.Game;
import de.hsharz.qwixx.model.player.Human;
import de.hsharz.qwixx.model.player.IPlayer;
import de.hsharz.qwixx.ui.AbstractPane;
import de.hsharz.qwixx.ui.game.board.GameBoardUI;
import de.hsharz.qwixx.ui.game.dice.DicePane;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;

public class GameUI extends AbstractPane<GridPane> {

	private Game game;
	private List<GameBoardUI> boards = new ArrayList<>();
	private DicePane dicePane;

	private JFXButton btnExit;
	private JFXButton btnHelp;

	public GameUI(Game game) {
		super(new GridPane());

		this.game = Objects.requireNonNull(game);

		createWidgets();
		addWidgets();
	}

	private void createWidgets() {
		createPlayerBoards();

		root.setHgap(10);
		root.setVgap(5);
		root.setAlignment(Pos.CENTER);

		root.setBackground(
				new Background(new BackgroundImage(new Image(new File("images/background.jpg").toURI().toString()),
						BackgroundRepeat.NO_REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT,
						new BackgroundSize(100, 100, true, true, true, true))));

		dicePane = new DicePane(game.getDices());
		game.addDiceListener(dicePane);
		game.addGameListener(dicePane);

		btnExit = new JFXButton("Spiel beenden");
		btnHelp = new JFXButton("Hilfe");
	}

	private void createPlayerBoards() {
		for (IPlayer player : game.getPlayer()) {
			GameBoardUI boardUI = new GameBoardUI(player);
			game.addGameListener(boardUI);

			if (player instanceof Human) {
				((Human) player).setHumanInputSupplier(boardUI);
			}

			boards.add(boardUI);
		}
	}

	private void addWidgets() {

		root.add(dicePane.getPane(), 1, 1);
		GridPane.setHalignment(dicePane.getPane(), HPos.CENTER);
		GridPane.setValignment(dicePane.getPane(), VPos.CENTER);

		root.add(btnHelp, 0, 0);
		GridPane.setHalignment(btnHelp, HPos.LEFT);
		GridPane.setValignment(btnHelp, VPos.TOP);

		root.add(btnExit, 2, 0);
		GridPane.setHalignment(btnExit, HPos.RIGHT);
		GridPane.setValignment(btnExit, VPos.TOP);

		switch (boards.size()) {

		case 2:
			// Boards oben mittig und unten
			for (GameBoardUI board : boards) {
				Group group = new Group(board.getPane());
				if (board.getPlayer() instanceof Human) {
					root.add(group, 1, 2);
					GridPane.setHalignment(group, HPos.CENTER);
				} else {
					root.add(group, 1, 0);
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
					root.add(group, 1, 2);
					GridPane.setHalignment(group, HPos.CENTER);
				} else {
					if (index == 0) {
						root.add(group, 0, 1);
					} else {
						root.add(group, 2, 1);
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
					root.add(group, 1, 2);
					GridPane.setHalignment(group, HPos.CENTER);
				} else {
					if (index == 0) {
						root.add(group, 0, 1);
					} else if (index == 1) {
						root.add(group, 1, 0);
						GridPane.setHalignment(group, HPos.CENTER);
					} else {
						root.add(group, 2, 1);
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
					root.add(group, 1, 2);
					GridPane.setHalignment(group, HPos.CENTER);
				} else {
					if (index == 0) {
						root.add(group, 0, 1);
					} else if (index == 1) {
						root.add(group, 0, 0, 2, 1);
						GridPane.setHalignment(group, HPos.CENTER);
					} else if (index == 2) {
						root.add(group, 1, 0, 2, 1);
						GridPane.setHalignment(group, HPos.CENTER);
					} else {
						root.add(group, 2, 1);
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
	}

}
