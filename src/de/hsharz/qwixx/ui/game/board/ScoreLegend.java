package de.hsharz.qwixx.ui.game.board;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import de.hsharz.qwixx.ui.AbstractPane;
import de.hsharz.qwixx.ui.game.board.utils.BreakLabel;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class ScoreLegend extends AbstractPane<HBox> {

	private List<Integer> score;
	private int amountMisses;

	private GridPane gridMisses;
	private List<MissField> missFields = new LinkedList<>();
	private List<BreakLabel> scoreLegendLabels = new LinkedList<>();

	public ScoreLegend(List<Integer> scoreLegend, int amountMisses) {
		super(new HBox());

		this.score = Objects.requireNonNull(scoreLegend);
		this.amountMisses = amountMisses;

		createWidgets();
//		setupInteractions();
		addWidgets();
	}

	private void createWidgets() {
		root.setSpacing(2);
		root.setPadding(new Insets(5, 0, 5, 0));
		root.setAlignment(Pos.CENTER);

		for (int i = 0; i < score.size(); i++) {
			BreakLabel lblScore = new BreakLabel((i + 1) + "x", Integer.toString(score.get(i)));
			scoreLegendLabels.add(lblScore);
		}

		for (int i = 0; i < amountMisses; i++) {
			missFields.add(new MissField());
		}

		gridMisses = new GridPane();
		gridMisses.setHgap(2);

	}

	private void addWidgets() {
		Label missed = getBorderLabel("Fehlwürfe je -5", "#999999");
		missed.setStyle("-fx-font-size: 16px; -fx-alignment: center;");

		gridMisses.add(missed, 0, 0, 4, 1);
		for (int i = 0; i < missFields.size(); i++) {
			gridMisses.add(missFields.get(i).getPane(), i, 1);
		}

		GridPane.setHgrow(missed, Priority.ALWAYS);
		GridPane.setHalignment(missed, HPos.CENTER);

		BreakLabel scorePerCross = new BreakLabel("Kreuze", "Punkte");
		scorePerCross.getPane().setStyle(""); // remove style

		root.getChildren().add(scorePerCross.getPane());
		scoreLegendLabels.stream().map(BreakLabel::getPane).forEach(root.getChildren()::add);

		root.getChildren().add(gridMisses);

		HBox.setMargin(gridMisses, new Insets(0, 0, 0, 20));
	}

	public Label getBorderLabel(String text, String color) {
		Label lbl = new Label(text);

		lbl.setStyle("-fx-font-size: 16px; -fx-alignment: center; -fx-border-color: " + color
				+ "; -fx-border-radius: 10px; -fx-border-width: 3px; "
				+ "-fx-background-color: white; -fx-background-radius: 11px; ");

		return lbl;
	}

	public List<MissField> getMissFields() {
		return missFields;
	}

}
