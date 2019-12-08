package de.hsharz.qwixx.ui;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

public class ScoreLegend {

	private static List<Integer> scoreLegend = Arrays.asList(1, 3, 6, 10, 15, 21, 28, 36, 45, 55, 66, 78);

	private HBox root;

	private GridPane gridMisses;
//	private CrossButton btnMiss1;
//	private CrossButton btnMiss2;
//	private CrossButton btnMiss3;
//	private CrossButton btnMiss4;

	private List<BreakLabel> scoreLegendLabels = new LinkedList<>();

	public ScoreLegend() {

		createWidgets();
//		setupInteractions();
		addWidgets();
	}

	private void createWidgets() {

		root = new HBox(2);
		root.setPadding(new Insets(5, 0, 5, 0));
		root.setAlignment(Pos.CENTER);

//		btnMiss1 = new CrossButton(0, DiceColor.GREEN);
//		btnMiss2 = new CrossButton(0, DiceColor.GREEN);
//		btnMiss3 = new CrossButton(0, DiceColor.GREEN);
//		btnMiss4 = new CrossButton(0, DiceColor.GREEN);

		for (int i = 0; i < scoreLegend.size(); i++) {
			BreakLabel lblScore = new BreakLabel((i+1) + "x", Integer.toString(scoreLegend.get(i)));
			scoreLegendLabels.add(lblScore);
		}

		gridMisses = new GridPane();
		gridMisses.setHgap(2);

	}

	private void addWidgets() {
		Label missed = getBorderLabel("Fehlwürfe je -5", "#999999");
		missed.setStyle("-fx-font-size: 16px; -fx-alignment: center;");
		
		
		gridMisses.add(missed, 0, 0, 4, 1);
		gridMisses.add(new MissField().getPane(), 0, 1);
		gridMisses.add(new MissField().getPane(), 1, 1);
		gridMisses.add(new MissField().getPane(), 2, 1);
		gridMisses.add(new MissField().getPane(), 3, 1);
		
		GridPane.setHgrow(missed, Priority.ALWAYS);
		GridPane.setHalignment(missed, HPos.CENTER);

		BreakLabel scorePerCross = new BreakLabel("Kreuze", "Punkte");
		scorePerCross.getPane().setStyle(""); // remove style
		
		root.getChildren().add(scorePerCross.getPane());
		scoreLegendLabels.stream().map(BreakLabel::getPane).forEach(root.getChildren()::add);

		root.getChildren().add(gridMisses);

		HBox.setMargin(gridMisses, new Insets(0, 0, 0, 20));

//		root.getChildren().add(btnMiss1.getPane());
//		root.getChildren().add(btnMiss2.getPane());
//		root.getChildren().add(btnMiss3.getPane());
//		root.getChildren().add(btnMiss4.getPane());
	}

	private Label getSimpleLabel(String text, int fontsize) {
		Label lbl = new Label(text);
		lbl.setStyle("-fx-font-size: " + fontsize + "px; -fx-font-weight: bold");
		return lbl;
	}

	public Label getBorderLabel(String text, String color) {
		Label lbl = new Label(text);
//		lbl.setPrefSize(30, 40);

		lbl.setStyle("-fx-font-size: 16px; -fx-alignment: center; -fx-border-color: " + color
				+ "; -fx-border-radius: 10px; -fx-border-width: 3px; -fx-background-color: white; -fx-background-radius: 11px; ");

		return lbl;
	}

	public Pane getPane() {
		return root;
	}

}
