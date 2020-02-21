package de.hsharz.qwixx.ui.statistics;

import java.sql.SQLException;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import de.hsharz.qwixx.database.ScoreDatabase;
import de.hsharz.qwixx.database.ScoreDatabase.PlayerScore;
import de.hsharz.qwixx.ui.AbstractPane;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class StatisticPane extends AbstractPane<GridPane> {

	private Stage stage;
	private Scene scene;

	private ObservableList<PlayerScore> scores = FXCollections.observableArrayList();
	private JFXTreeTableView<PlayerScore> tableScore;
	private JFXComboBox<Integer> boxChoosePlayer;

	private Label lblAmountPlayer;

	public StatisticPane() {
		super(new GridPane());

		createWidgets();
		setupInteractions();
		addWidgets();
	}

	@SuppressWarnings("unchecked")
	private void createWidgets() {
		root.setPadding(new Insets(50));
		root.setHgap(10);
		root.setVgap(10);
		root.setStyle("-fx-background-color: #ffffff;");

		scene = new Scene(root);

		stage = new Stage(StageStyle.UTILITY);
		stage.setScene(scene);
		stage.setWidth(500);
		stage.setAlwaysOnTop(true);
		
		lblAmountPlayer = new Label("Anzahl deiner Mitspieler: ");
		lblAmountPlayer.setStyle("-fx-font-size: 16pt;");

		boxChoosePlayer = new JFXComboBox<>(FXCollections.observableArrayList(1, 2, 3, 4));
		boxChoosePlayer.getSelectionModel().select(2);
		boxChoosePlayer.setStyle("-fx-font-size: 16pt;");

		tableScore = new JFXTreeTableView<>();

		TreeTableColumn<PlayerScore, String> columnName = new JFXTreeTableColumn<>("Name");
		columnName.setPrefWidth(220);
		columnName.setCellValueFactory(param -> param.getValue().getValue().getName());
		TreeTableColumn<PlayerScore, String> columnScore = new JFXTreeTableColumn<>("Punktzahl");
		columnScore.setPrefWidth(140);
		columnScore.setCellValueFactory(param -> param.getValue().getValue().getScore());

		tableScore.getColumns().setAll(columnName, columnScore);

		TreeItem<PlayerScore> root = new RecursiveTreeItem<>(scores, RecursiveTreeObject::getChildren);
		tableScore.setRoot(root);
		tableScore.setShowRoot(false);

		tableScore.setStyle("");
		
	}

	private void setupInteractions() {

		root.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
			if (e.getCode() == KeyCode.ESCAPE) {
				stage.close();
			}
		});

		boxChoosePlayer.getSelectionModel().selectedItemProperty()
				.addListener((ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) -> {
					scores.clear();
					try {
						scores.addAll(ScoreDatabase.getScoresOfGameWithAmountPlayer(newValue.intValue() + 1));
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				});
	}

	private void addWidgets() {
		root.add(lblAmountPlayer, 0, 0);
		root.add(boxChoosePlayer, 1, 0);
		root.add(tableScore, 0, 1, 2, 1);

		GridPane.setHgrow(lblAmountPlayer, Priority.ALWAYS);

		GridPane.setHgrow(tableScore, Priority.ALWAYS);
		GridPane.setVgrow(tableScore, Priority.ALWAYS);

	}

	public Stage getStage() {
		return stage;
	}

}
