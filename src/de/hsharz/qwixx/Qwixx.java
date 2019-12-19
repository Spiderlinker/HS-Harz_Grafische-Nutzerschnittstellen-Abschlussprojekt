package de.hsharz.qwixx;

import de.hsharz.qwixx.model.Game;
import de.hsharz.qwixx.model.board.GameBoard;
import de.hsharz.qwixx.model.player.Computer;
import de.hsharz.qwixx.model.player.Human;
import de.hsharz.qwixx.model.player.IPlayer;
import de.hsharz.qwixx.ui.StartScreen;
import de.hsharz.qwixx.ui.game.GameUI;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Qwixx extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		StackPane root = new StackPane();

//		root.setStyle("-fx-background-color: black;");

		Game game = new Game();

		{
			GameBoard board = new GameBoard();
			board.setRowClosedSupplier(game);
			IPlayer player = new Human("Peter", board);
			game.addPlayer(player);
		}

		for (int i = 0; i < 4; i++) {
			GameBoard board = new GameBoard();
			board.setRowClosedSupplier(game);

			IPlayer player = new Computer("Computer #" + i, board);
			game.addPlayer(player);
		}

		GameUI gameUI = new GameUI(game);
//		root.setCenter(gameUI.getPane());

		new Thread(game::startGame).start();

//		Background background = new Background(new BackgroundImage(new Image(new File("images/background.jpg").toURI().toString()),
//				BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER,
//				new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)));
//		root.setBackground(background);

		
		root.getChildren().add(new StartScreen().getPane());
		
		primaryStage.setOnCloseRequest(e -> System.exit(0));
		primaryStage.setTitle("Hello World");
		primaryStage.setScene(new Scene(root, 400, 500));
//		primaryStage.setFullScreen(true);

		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}
}