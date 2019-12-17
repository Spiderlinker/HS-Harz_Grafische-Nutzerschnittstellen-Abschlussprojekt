package de.hsharz.qwixx.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hsharz.qwixx.model.Game;
import de.hsharz.qwixx.model.player.Human;
import de.hsharz.qwixx.model.player.IPlayer;
import de.hsharz.qwixx.ui.board.GameBoardUI;
import de.hsharz.qwixx.ui.dice.DicePane;
import javafx.scene.layout.GridPane;

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
		dicePane = new DicePane(game.getDices());
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

	}

	public Map<IPlayer, GameBoardUI> getPlayerGameBoards() {
		return boards;
	}

	public DicePane getDicePane() {
		return dicePane;
	}

}
