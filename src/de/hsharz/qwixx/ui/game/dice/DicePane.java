package de.hsharz.qwixx.ui.game.dice;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import de.hsharz.qwixx.model.GameListener;
import de.hsharz.qwixx.model.dice.DiceColor;
import de.hsharz.qwixx.model.dice.IDice;
import de.hsharz.qwixx.model.player.Computer;
import de.hsharz.qwixx.model.player.IPlayer;
import de.hsharz.qwixx.ui.AbstractPane;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Separator;
import javafx.scene.layout.GridPane;

public class DicePane extends AbstractPane<GridPane> implements GameListener, DiceListener {

	private List<IDice> dices;
	private List<DiceUI> dicesUI = new ArrayList<>();

	public DicePane(List<IDice> dices) {
		super(new GridPane());

		this.dices = Objects.requireNonNull(dices);

		createWidgets();
		addWidgets();
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

		Predicate<DiceUI> isWhiteDice = d -> d.getDice().getColor().equals(DiceColor.WHITE);
		List<DiceUI> whiteDices = dicesUI.stream().filter(isWhiteDice::test).collect(Collectors.toList());
		List<DiceUI> colorDices = dicesUI.stream().filter(isWhiteDice.negate()::test).collect(Collectors.toList());

		for (int i = 0; i < whiteDices.size(); i++) {
			root.add(whiteDices.get(i).getPane(), 0, i);
		}

		root.add(new Separator(Orientation.VERTICAL), 1, 0);
		root.add(new Separator(Orientation.VERTICAL), 1, 1);

		for (int i = 0; i < colorDices.size(); i++) {
			if (i < 2) {
				root.add(colorDices.get(i).getPane(), (i % 2) + 2, 0);
			} else {
				root.add(colorDices.get(i).getPane(), (i % 2) + 2, 1);
			}
		}
	}

	public void refreshDices() {
		dicesUI.forEach(DiceUI::refreshDice);
	}

	@Override
	public void dicesRolled() {
		refreshDices();
	}

	@Override
	public void nextPlayersTurn(IPlayer nextPlayer) {
		Predicate<DiceUI> isWhiteDice = d -> d.getDice().getColor().equals(DiceColor.WHITE);
		dicesUI.stream().filter(isWhiteDice.negate()::test)
				.forEach(d -> d.getPane().setOpacity(nextPlayer instanceof Computer ? 0.6 : 1));
	}

	@Override
	public void gameOver() {
		// ignore
	}

	@Override
	public void invalidDiceChoiceMade(IPlayer player, String msg) {
		// ignore
	}
}
