package de.hsharz.qwixx.ui.dice;

import java.util.Objects;

import de.hsharz.qwixx.model.dice.DiceColor;
import de.hsharz.qwixx.model.dice.IDice;
import de.hsharz.qwixx.ui.AbstractPane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class DiceUI extends AbstractPane<GridPane> {

	private IDice dice;
	private DropShadow glowEffect;
	private Circle[][] diceEyes = new Circle[3][3];

	public DiceUI(IDice dice) {
		super(new GridPane());

		this.dice = Objects.requireNonNull(dice);

		createWidgets();
		addWidgets();
	}

	public void createWidgets() {
		root.setStyle("-fx-border-color: #999999; -fx-border-radius: 10px; -fx-border-width: 2px; "
				+ "-fx-background-color: " + dice.getColor().getAsHex() + "; -fx-background-radius: 11px;");
		root.setPadding(new Insets(5));
		root.setAlignment(Pos.CENTER);

		addColumnRowConstraints();
		createDiceEyes();
	}

	private void addColumnRowConstraints() {
		for (int i = 0; i < 3; i++) {
			ColumnConstraints col = new ColumnConstraints();
			col.setHgrow(Priority.ALWAYS);
			root.getColumnConstraints().add(col);

			RowConstraints row = new RowConstraints();
			row.setVgrow(Priority.ALWAYS);
			root.getRowConstraints().add(row);
		}
	}

	private void createDiceEyes() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				String diceEyeColor = dice.getColor().equals(DiceColor.WHITE) ? "black" : "white";
				diceEyes[i][j] = new Circle(8, Paint.valueOf(diceEyeColor));
			}
		}
	}

	public void addWidgets() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				BorderPane pane = new BorderPane();
				pane.setPadding(new Insets(5));
				pane.setCenter(diceEyes[i][j]);
				root.add(pane, i, j);
			}
		}
	}

	public void setHighlighted(boolean highlighted) {
		root.setEffect(highlighted ? glowEffect : null);
	}

	public void refreshDice() {
		hideDiceEyes();

		// 1: __ | 2: __ | 3: __ | 4: __ | 5: __ | 6: __ |
		// - - - | # - - | # - - | # - # | # - # | # - # |
		// - # - | - - - | - # - | - - - | - # - | # - # |
		// - - - | - - # | - - # | # - # | # - # | # - # |
		switch (dice.getCurrentValue()) {
		case 5:
			// Für die 5 wird auch die 3 und die 1 gebaut
			diceEyes[0][2].setVisible(true); // oben rechts
			diceEyes[2][0].setVisible(true); // unten links
		case 3:
			// für die 3 wird auch die 1 gebaut
			diceEyes[0][0].setVisible(true); // oben links
			diceEyes[2][2].setVisible(true); // unten rechts
		case 1:
			diceEyes[1][1].setVisible(true); // mitte mitte
			break;
		case 6:
			// für die 6 wird auch die 4 und die 2 gebaut
			diceEyes[0][1].setVisible(true); // links mitte
			diceEyes[2][1].setVisible(true); // rechts mitte
		case 4:
			// für die 4 wird auch die zwei gebaut
			diceEyes[0][2].setVisible(true); // oben rechts
			diceEyes[2][0].setVisible(true); // unten links
		case 2:
			diceEyes[0][0].setVisible(true); // oben links
			diceEyes[2][2].setVisible(true); // unten rechts
			break;
		default:
			throw new IllegalArgumentException("Zahl von Würfel nicht bekannt: " + dice.getCurrentValue());
		}

	}

	private void hideDiceEyes() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				diceEyes[i][j].setVisible(false);
			}
		}
	}

	public IDice getDice() {
		return dice;
	}

}
