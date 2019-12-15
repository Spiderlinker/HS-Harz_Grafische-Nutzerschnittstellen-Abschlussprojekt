package de.hsharz.qwixx;

import de.hsharz.qwixx.model.Game;
import de.hsharz.qwixx.model.board.GameBoard;
import de.hsharz.qwixx.model.player.Computer;
import de.hsharz.qwixx.model.player.Human;
import de.hsharz.qwixx.model.player.IPlayer;
import de.hsharz.qwixx.ui.GameBoardUI;
import de.hsharz.qwixx.ui.dice.DicePane;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class Qwixx extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		GridPane root = new GridPane();
		root.setGridLinesVisible(true);
		Game game = new Game();

		HBox games = new HBox();

		{
			GameBoard board = new GameBoard();
			board.setRowClosedSupplier(game);
			IPlayer player = new Human("Peter",board);
			GameBoardUI ui = new GameBoardUI(player);
			((Human) player).setHumanInputSupplier(ui);
			game.addPlayer(player);
			root.add(ui.getPane(), 0, 1);
		}

		for (int i = 0; i < 3; i++) {
			GameBoard board = new GameBoard();
			board.setRowClosedSupplier(game);

			IPlayer player = new Computer("Computer #" + i, board);
			GameBoardUI ui = new GameBoardUI(player);

			game.addPlayer(player);
			games.getChildren().add(ui.getPane());
		}

		games.setScaleX(0.6);
		games.setScaleY(0.6);
//		games.setRotate(180);

		ScrollPane scroll = new ScrollPane();
		scroll.setContent(new Group(games));
		scroll.setStyle("-fx-background-color: blue;");
		root.add(scroll, 0, 0, 2, 1);

		games.setStyle("-fx-background-color: red;");

		GridPane.setFillHeight(scroll, true);
		GridPane.setFillWidth(scroll, true);

		GridPane.setHgrow(scroll, Priority.ALWAYS);
		GridPane.setVgrow(scroll, Priority.ALWAYS);

		DicePane dicePane = new DicePane(game.getDices());
		game.addDiceListener(dicePane);

		root.add(dicePane.getPane(), 1, 1);

		new Thread(game::startGame).start();

		primaryStage.setOnCloseRequest(e -> System.exit(0));
		primaryStage.setTitle("Hello World");
		primaryStage.setScene(new Scene(root, 900, 600));
		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}
}