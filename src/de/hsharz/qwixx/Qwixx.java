package de.hsharz.qwixx;

import de.hsharz.qwixx.ui.StartScreen;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Qwixx extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		StackPane root = new StackPane();

//		root.setStyle("-fx-background-color: black;");

//		Background background = new Background(new BackgroundImage(new Image(new File("images/background.jpg").toURI().toString()),
//				BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER,
//				new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)));
//		root.setBackground(background);

		root.getChildren().add(new StartScreen(primaryStage).getPane());

		primaryStage.setOnCloseRequest(e -> Platform.exit());
		primaryStage.setTitle("Qwixx");
		primaryStage.setScene(new Scene(root, 440, 500));
		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}
}