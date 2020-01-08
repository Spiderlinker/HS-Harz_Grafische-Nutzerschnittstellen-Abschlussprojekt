package de.hsharz.qwixx;

import de.hsharz.qwixx.ui.StartScreen;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Qwixx extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		StackPane root = new StackPane();
		root.getChildren().add(new StartScreen(primaryStage).getPane());

		primaryStage.setOnCloseRequest(e -> {
			primaryStage.hide();
			System.exit(0);
		});
		primaryStage.setTitle("Qwixx");
		primaryStage.setScene(new Scene(root, 440, 500));
		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}
}