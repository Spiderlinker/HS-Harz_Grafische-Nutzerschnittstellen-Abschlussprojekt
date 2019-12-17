package de.hsharz.qwixx.ui.dice;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hsharz.qwixx.model.dice.DiceColor;
import de.hsharz.qwixx.model.dice.IDice;
import de.hsharz.qwixx.ui.AbstractPane;
import javafx.application.Platform;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;

public class DicePane extends AbstractPane<HBox> implements DiceListener {

	private List<IDice> dices;
	private List<DiceUI> dicesUI = new ArrayList<>();

	private TilePane whitePane;
	private TilePane colorPane;

	public DicePane(List<IDice> dices) {
		super(new HBox());

		this.dices = Objects.requireNonNull(dices);

		createWidgets();
		addWidgets();

	}

	private void createWidgets() {
		root.setSpacing(20);

		whitePane = new TilePane(10, 10);
		colorPane = new TilePane(10, 10);

		for (IDice dice : dices) {
			DiceUI diceUI = new DiceUI(dice);
			dicesUI.add(diceUI);
		}
	}

	private void addWidgets() {
		root.getChildren().add(whitePane);
		root.getChildren().add(colorPane);

		for (DiceUI dice : dicesUI) {
			if (dice.getDice().getColor().equals(DiceColor.WHITE)) {
				whitePane.getChildren().add(dice.getPane());
			} else {
				colorPane.getChildren().add(dice.getPane());
			}
		}

	}

	public void refreshDices() {
		Platform.runLater(() -> dicesUI.forEach(DiceUI::refreshDice));
	}

	@Override
	public void dicesRolled() {
		refreshDices();
	}

}
