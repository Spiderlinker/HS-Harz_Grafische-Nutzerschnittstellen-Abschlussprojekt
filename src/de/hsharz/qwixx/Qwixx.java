package de.hsharz.qwixx;


import de.hsharz.qwixx.model.Game;
import de.hsharz.qwixx.model.board.GameBoard;
import de.hsharz.qwixx.model.player.Computer;
import de.hsharz.qwixx.model.player.Human;
import de.hsharz.qwixx.model.player.IPlayer;
import de.hsharz.qwixx.ui.GameBoardUI;
import de.hsharz.qwixx.ui.dice.DicePane;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Qwixx extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		GridPane root = new GridPane();
		root.setGridLinesVisible(true);
		Game game = new Game();

		{
			GameBoard board = new GameBoard();
			board.setRowClosedSupplier(game);
			IPlayer player = new Human("Peter",board);
			GameBoardUI ui = new GameBoardUI(player);
			game.addGameListener(ui);
			((Human) player).setHumanInputSupplier(ui);
			game.addPlayer(player);

			ui.getPane().setScaleX(0.65);
			ui.getPane().setScaleY(0.65);
			root.add(new Group(ui.getPane()), 1, 2);
		}

		for (int i = 0; i < 3; i++) {
			GameBoard board = new GameBoard();
			board.setRowClosedSupplier(game);

			IPlayer player = new Computer("Computer #" + i, board);
			GameBoardUI ui = new GameBoardUI(player);
			game.addGameListener(ui);
			
			game.addPlayer(player);

			ui.getPane().setScaleX(0.6);
			ui.getPane().setScaleY(0.6);
			
			switch(i) {
			case 0:
				root.add(new Group(ui.getPane()), 0, 1);
				break;
			case 1:
				root.add(new Group(ui.getPane()), 1, 0);
				break;
			case 2:
				root.add(new Group(ui.getPane()), 2, 1);
				break;
			
			}
		}

		DicePane dicePane = new DicePane(game.getDices());
		game.addDiceListener(dicePane);

		root.add(dicePane.getPane(), 1, 1);

		GridPane.setHalignment(dicePane.getPane(), HPos.CENTER);
		GridPane.setValignment(dicePane.getPane(), VPos.CENTER);
		
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