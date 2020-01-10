package de.hsharz.qwixx.ui.game.board;

import java.util.List;

import de.hsharz.qwixx.model.dice.DicePair;
import de.hsharz.qwixx.model.player.DiceSelectionType;
import de.hsharz.qwixx.model.player.IPlayer;
import de.hsharz.qwixx.ui.game.board.row.RowUI;
import de.hsharz.qwixx.ui.game.board.row.field.NumberFieldUI;

public class GameBoardHighlight extends GameBoardUI {

	public GameBoardHighlight(IPlayer player) {
		super(player);
	}

	@Override
	public void askForInput(List<DicePair> dices, DiceSelectionType selectionType) {
		super.askForInput(dices, selectionType);
		highlightButtonsOfDices(dices);
	}

	private void highlightButtonsOfDices(List<DicePair> dices) {
		rows.values().forEach(row -> highlightButtonsOfDicesForRow(row, dices));
	}

	private void highlightButtonsOfDicesForRow(RowUI row, List<DicePair> dices) {
		if (player.getGameBoard().getRowClosedSupplier().isRowClosed(row.getRow().getColor())) {
			return;
		}

		for (int i = row.getButtons().size() - 1; i >= 0; i--) {
			if (row.getRow().getFields().get(i).isCrossed()) {
				break;
			}

			NumberFieldUI btn = row.getButtons().get(i);
			for (DicePair d : dices) {
				if (row.getRow().getColor().equals(d.getColor()) && d.getSum() == btn.getValue()) {
					btn.setDisabled(false);
					break;
				}
			}
		}
	}

}
