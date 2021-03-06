package de.hsharz.qwixx.ui.game.dice;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hsharz.qwixx.model.GameListener;
import de.hsharz.qwixx.model.board.row.RowsClosedSupplier;
import de.hsharz.qwixx.model.dice.DiceColor;
import de.hsharz.qwixx.model.dice.IDice;
import de.hsharz.qwixx.model.player.Computer;
import de.hsharz.qwixx.model.player.IPlayer;
import de.hsharz.qwixx.ui.AbstractPane;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Separator;
import javafx.scene.layout.GridPane;

public class DicePane extends AbstractPane<GridPane> implements GameListener, DiceListener {

	private List<IDice> dices;
	private List<DiceUI> dicesUI = new ArrayList<>();

	private RowsClosedSupplier rowClosedSupplier;

	public DicePane(List<IDice> dices) {
		super(new GridPane());

		this.dices = Objects.requireNonNull(dices);

		createWidgets();
		addWidgets();
	}

	public void addRowClosedSupplier(RowsClosedSupplier supplier) {
		this.rowClosedSupplier = supplier;
	}

	private void createWidgets() {
		root.setHgap(20);
		root.setVgap(20);
		root.setPadding(new Insets(20));

		root.setAlignment(Pos.CENTER);

		for (IDice dice : dices) {
			DiceUI diceUI = new DiceUI(dice);
			dicesUI.add(diceUI);
		}
	}

	private void addWidgets() {

		List<DiceUI> whiteDices = new ArrayList<>();
		List<DiceUI> colorDices = new ArrayList<>();

		// W�rfel nach Wei�- und Farbw�rfeln in Listen sortieren
		for (DiceUI dice : dicesUI) {
			if (DiceColor.WHITE.equals(dice.getDice().getColor())) {
				whiteDices.add(dice);
			} else {
				colorDices.add(dice);
			}
		}

		// Wei�e W�rfel ins Pane hinzuf�gen
		for (int i = 0; i < whiteDices.size(); i++) {
			root.add(whiteDices.get(i).getPane(), 0, i);
		}

		// Trennlinie
		root.add(new Separator(Orientation.VERTICAL), 1, 0);
		root.add(new Separator(Orientation.VERTICAL), 1, 1);

		// Farbw�rfel ins Pane hinzuf�gen
		for (int i = 0; i < colorDices.size(); i++) {
			if (i < 2) {
				root.add(colorDices.get(i).getPane(), (i % 2) + 2, 0);
			} else {
				root.add(colorDices.get(i).getPane(), (i % 2) + 2, 1);
			}
		}
	}

	public void refreshDices() {
		Platform.runLater(() -> {
			for (DiceUI dice : dicesUI) {
				dice.refreshDice();
			}
		});
	}

	@Override
	public void dicesRolled() {
		refreshDices();
	}

	@Override
	public void nextPlayersTurn(IPlayer nextPlayer) {
		double opacity = nextPlayer instanceof Computer ? 0.6 : 1;

		Platform.runLater(() -> {

			for (DiceUI dice : dicesUI) {
				// Falls die Reihe der Farbe des W�rfels bereits geschlossen ist,
				// so soll dieser W�rfel ausgeblendet werden
				if (rowClosedSupplier != null && rowClosedSupplier.isRowClosed(dice.getDice().getColor())) {
					dice.getPane().setVisible(false);
					continue;
				}

				if (!dice.getDice().getColor().equals(DiceColor.WHITE)) {
					dice.getPane().setOpacity(opacity);
				}
			}

		});
	}

}
