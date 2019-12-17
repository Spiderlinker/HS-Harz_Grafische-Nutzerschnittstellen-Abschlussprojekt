package de.hsharz.qwixx.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import de.hsharz.qwixx.model.Game;
import de.hsharz.qwixx.model.player.Human;
import de.hsharz.qwixx.model.player.IPlayer;
import de.hsharz.qwixx.ui.board.GameBoardUI;
import de.hsharz.qwixx.ui.dice.DicePane;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class GameUI extends AbstractPane<GridPane> {

	private Game game;
	private Map<IPlayer, GameBoardUI> boards = new HashMap<>();
	private DicePane dicePane;

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
		root.setGridLinesVisible(true);

		dicePane = new DicePane(game.getDices());
		game.addDiceListener(dicePane);
	}

	private void createPlayerBoards() {
		for (IPlayer player : game.getPlayer()) {
			GameBoardUI boardUI = new GameBoardUI(player);
			game.addGameListener(boardUI);

			if (player instanceof Human) {
				((Human) player).setHumanInputSupplier(boardUI);
			}

			boards.put(player, boardUI);
		}
	}

	private void addWidgets() {

		boards.values().forEach(board -> {
			board.getPane().setScaleX(0.7);
			board.getPane().setScaleY(0.7);
		});

		root.add(dicePane.getPane(), 1, 1);

		GridPane.setHalignment(dicePane.getPane(), HPos.CENTER);
		GridPane.setValignment(dicePane.getPane(), VPos.CENTER);

		switch (boards.size()) {

		case 2:
			// Boards oben mittig und unten
			for (Entry<IPlayer, GameBoardUI> e : boards.entrySet()) {
				Group group = new Group(e.getValue().getPane());
				if (e.getKey() instanceof Human) {
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
			for (Entry<IPlayer, GameBoardUI> e : boards.entrySet()) {
				Group group = new Group(e.getValue().getPane());
				if (e.getKey() instanceof Human) {
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
			for (Entry<IPlayer, GameBoardUI> e : boards.entrySet()) {
				Group group = new Group(e.getValue().getPane());
				if (e.getKey() instanceof Human) {
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
			for (Entry<IPlayer, GameBoardUI> e : boards.entrySet()) {
				Group group = new Group(e.getValue().getPane());
				if (e.getKey() instanceof Human) {
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

	public Map<IPlayer, GameBoardUI> getPlayerGameBoards() {
		return boards;
	}

	public DicePane getDicePane() {
		return dicePane;
	}

}
