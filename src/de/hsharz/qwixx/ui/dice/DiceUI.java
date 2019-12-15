package de.hsharz.qwixx.ui.dice;

import java.util.Objects;

import de.hsharz.qwixx.model.dice.DiceColor;
import de.hsharz.qwixx.model.dice.IDice;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class DiceUI {

	private IDice dice;

	private GridPane root;
	private DropShadow glowEffect;

	public DiceUI(IDice dice) {
		this.dice = Objects.requireNonNull(dice);

		createWidgets();
	}

	public void createWidgets() {
		root = new GridPane();
		root.setStyle("-fx-border-color: #999999; -fx-border-radius: 10px; -fx-border-width: 2px; "
				+ "-fx-background-color: " + dice.getColor().getAsHex() + "; -fx-background-radius: 11px;");

//		root.setHgap(5);
//		root.setVgap(5);
		root.setPadding(new Insets(5));
		root.setAlignment(Pos.CENTER);
		root.setMinSize(3*16, 3*16);

		addColumnRowConstraints();
		createGlowEffect();
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

	private void createGlowEffect() {
		glowEffect = new DropShadow();
		glowEffect.setColor(Color.valueOf(dice.getColor().getAsHex()));
		glowEffect.setWidth(30);
		glowEffect.setHeight(30);
	}

	public void addWidgets() {

		// 1: __ | 2: __ | 3: __ | 4: __ | 5: __ | 6: __ |
		// - - - | # - - | # - - | # - # | # - # | # - # |
		// - # - | - - - | - # - | - - - | - # - | # - # |
		// - - - | - - # | - - # | # - # | # - # | # - # |

		switch (dice.getCurrentValue()) {
		case 5:
			// Für die 5 wird auch die 3 und die 1 gebaut
			root.add(createDiceEye(), 0, 2); // oben rechts
			root.add(createDiceEye(), 2, 0); // unten links
		case 3:
			// für die 3 wird auch die 1 gebaut
			root.add(createDiceEye(), 0, 0); // oben links
			root.add(createDiceEye(), 2, 2); // unten rechts
		case 1:
			root.add(createDiceEye(), 1, 1); // mitte mitte
			break;
		case 6:
			// für die 6 wird auch die 4 und die 2 gebaut
			root.add(createDiceEye(), 0, 1); // links mitte
			root.add(createDiceEye(), 2, 1); // rechts mitte
		case 4:
			// für die 4 wird auch die zwei gebaut
			root.add(createDiceEye(), 0, 2); // oben rechts
			root.add(createDiceEye(), 2, 0); // unten links
		case 2:
			root.add(createDiceEye(), 0, 0); // oben links
			root.add(createDiceEye(), 2, 2); // unten rechts
			break;
		default:
			throw new IllegalArgumentException("Zahl von Würfel nicht bekannt: " + dice.getCurrentValue());
		}

	}

	private Pane createDiceEye() {
		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(5));

		Circle eye = new Circle(8, Paint.valueOf(dice.getColor().equals(DiceColor.WHITE) ? "black" : "white"));
		pane.setCenter(eye);

		return pane;
	}

	public void setHighlighted(boolean highlighted) {
		root.setEffect(highlighted ? glowEffect : null);
	}

	public IDice getDice() {
		return dice;
	}

	public Pane getPane() {
		return root;
	}

	public void refreshDice() {
		root.getChildren().clear();
		addWidgets();
	}

}
