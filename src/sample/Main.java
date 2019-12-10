package sample;

import de.hsharz.qwixx.model.Game;
import de.hsharz.qwixx.model.board.GameBoard;
import de.hsharz.qwixx.model.player.Human;
import de.hsharz.qwixx.model.player.IPlayer;
import de.hsharz.qwixx.ui.GameBoardUI;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		VBox root = new VBox(10);

		Game game = new Game();

		for (int i = 0; i < 2; i++) {
			GameBoard board = new GameBoard();
			board.setRowClosedSupplier(game);
			IPlayer player = new Human(board);

			GameBoardUI ui = new GameBoardUI(player);
			((Human) player).setHumanInputSupplier(ui);

			game.addPlayer(player);
			root.getChildren().add(ui.getPane());
		}

		new Thread(game::startGame).start();


		root.setStyle("-fx-background: gray;");

		primaryStage.setOnCloseRequest(e -> System.exit(0));
		primaryStage.setTitle("Hello World");
		primaryStage.setScene(new Scene(root, 800, 500));
		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}
}
