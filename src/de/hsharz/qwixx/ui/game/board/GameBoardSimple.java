package de.hsharz.qwixx.ui.game.board;

import java.io.File;
import java.util.List;

import com.jfoenix.controls.JFXButton;

import de.hsharz.qwixx.model.dice.DicePair;
import de.hsharz.qwixx.model.player.DiceSelectionType;
import de.hsharz.qwixx.model.player.IPlayer;
import de.hsharz.qwixx.ui.game.board.row.RowUI;
import javafx.geometry.Pos;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;

public class GameBoardSimple extends GameBoardUI {

	public GameBoardSimple(IPlayer player) {
		super(player);
		createNextButton();
	}

	private void createNextButton() {
		JFXButton btnNext = new JFXButton("", new ImageView(new File("images/arrow_right.png").toURI().toString()));
		btnNext.setFocusTraversable(false);

		Tooltip btnNextTooltip = new Tooltip("Weiter zum nächsten Würfelpaar / Zug");
		btnNextTooltip.setStyle("-fx-font-size: 14pt");
		btnNext.setTooltip(btnNextTooltip);
		
		btnNext.setOnAction(e -> playerSelectedDice(DicePair.EMPTY));

		btnNext.setAlignment(Pos.CENTER);
		boxGameBoardHeader.getChildren().add(btnNext);
	}

	@Override
	public void askForInput(List<DicePair> dices, DiceSelectionType selectionType) {
		super.askForInput(dices, selectionType);
		highlightRemainingFields();
	}

	private void highlightRemainingFields() {
		rows.values().forEach(this::highlightRemainingFieldsOfRow);
	}

	private void highlightRemainingFieldsOfRow(RowUI row) {
		if (player.getGameBoard().getRowClosedSupplier().isRowClosed(row.getRow().getColor())) {
			return;
		}

		for (int i = row.getButtons().size() - 1; i >= 0; i--) {
			if (row.getRow().getFields().get(i).isCrossed()) {
				break;
			}

			row.getButtons().get(i).setDisabled(false);
		}
	}

}
