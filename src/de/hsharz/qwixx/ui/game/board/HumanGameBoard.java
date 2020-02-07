package de.hsharz.qwixx.ui.game.board;

import com.jfoenix.controls.JFXButton;

import de.hsharz.qwixx.model.dice.DicePair;
import de.hsharz.qwixx.model.player.IPlayer;
import javafx.geometry.Pos;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;

public class HumanGameBoard extends GameBoardUI {

	public HumanGameBoard(IPlayer player) {
		super(player);
		createNextButton();
	}

	private void createNextButton() {
		JFXButton btnNext = new JFXButton("",
				new ImageView(getClass().getResource("/images/arrow_right.png").toString()));
		btnNext.setFocusTraversable(false);

		Tooltip btnNextTooltip = new Tooltip("Weiter zum nächsten Würfelpaar / Zug");
		btnNextTooltip.setStyle("-fx-font-size: 14pt");
		btnNext.setTooltip(btnNextTooltip);

		btnNext.setOnAction(e -> playerSelectedDice(DicePair.EMPTY));

		btnNext.setAlignment(Pos.CENTER);
		boxGameBoardHeader.getChildren().add(btnNext);
	}

	@Override
	public void nextPlayersTurn(IPlayer nextPlayer) {
		super.nextPlayersTurn(nextPlayer);

		boolean isCurrentPlayersTurn = player.equals(nextPlayer);
		setMissFieldsDisabled(!isCurrentPlayersTurn);
	}

	private void setMissFieldsDisabled(boolean disabled) {
		scoreLegend.getMissFields().forEach(f -> f.setDisabled(disabled));
	}

}
