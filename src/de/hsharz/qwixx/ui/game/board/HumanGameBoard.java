package de.hsharz.qwixx.ui.game.board;

import java.util.List;
import java.util.Map.Entry;

import com.jfoenix.controls.JFXButton;

import de.hsharz.qwixx.model.dice.DiceColor;
import de.hsharz.qwixx.model.dice.DicePair;
import de.hsharz.qwixx.model.player.DiceSelectionType;
import de.hsharz.qwixx.model.player.HumanInputSupplier;
import de.hsharz.qwixx.model.player.IPlayer;
import de.hsharz.qwixx.ui.game.board.row.RowUI;
import de.hsharz.qwixx.ui.game.board.row.field.NumberFieldUI;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;

public class HumanGameBoard extends GameBoardUI implements HumanInputSupplier, FieldCrossedListener, MissFieldListener {

	private Label lblHint;
	private JFXButton btnNext;

	protected boolean shouldNotify = false;
	private DicePair humanInput;
	private DiceSelectionType selectionType;

	public HumanGameBoard(IPlayer player) {
		super(player);

		createWidgets();
		setupInteractions();
		addWidgets();
	}

	private void createWidgets() {
		lblHint = new Label();
		lblHint.setStyle("-fx-font-size: 18pt; -fx-font-weight: bold;");

		btnNext = new JFXButton("", new ImageView(getClass().getResource("/images/arrow_right.png").toString()));
		btnNext.setFocusTraversable(false);

		Tooltip btnNextTooltip = new Tooltip("Weiter zum nächsten Würfelpaar / Zug");
		btnNextTooltip.setStyle("-fx-font-size: 14pt");
		btnNext.setTooltip(btnNextTooltip);
	}

	private void setupInteractions() {
		btnNext.setOnAction(e -> playerSelectedDice(DicePair.EMPTY));

		rows.values().forEach(r -> r.addFieldCrossedListener(this));
		scoreLegend.getMissFields().forEach(f -> f.addListener(this));

		root.setOnMouseClicked(e -> {
			if (MouseButton.SECONDARY == e.getButton()) {
				playerSelectedDice(DicePair.EMPTY);
				e.consume();
			}
		});
	}

	private void addWidgets() {
		boxGameBoardHeader.getChildren().add(lblHint);
		boxGameBoardHeader.getChildren().add(btnNext);
		btnNext.setAlignment(Pos.CENTER);
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

	@Override
	public void askForInput(List<DicePair> dices, DiceSelectionType selectionType) {
		this.humanInput = null;
		this.shouldNotify = true;
		this.selectionType = selectionType;

		checkCrossedButtons();
		updateHintLabel();
	}

	private void checkCrossedButtons() {
		for (Entry<DiceColor, RowUI> e : rows.entrySet()) {
			for (int i = 0; i < e.getValue().getButtons().size(); i++) {
				NumberFieldUI numberField = e.getValue().getButtons().get(i);
				numberField.setSelected(player.getGameBoard().getRow(e.getKey()).getFields().get(i).isCrossed());
			}
		}
	}

	private void updateHintLabel() {
		Platform.runLater(() -> lblHint.setText(
				(DiceSelectionType.COLOR_DICE.equals(selectionType) ? "Farbwürfel" : "Weißen Würfel") + " wählen"));
	}

	/*
	 * Der Spieler hat das gegebene Würfelpaar über die UI ausgewählt. Der Spieler
	 * dieses Spielfeldes wird darüber benachrichtigt.
	 */
	protected void playerSelectedDice(DicePair dice) {
		try {
			humanInput = dice;
			disableAllButtons();
		} finally {
			if (shouldNotify) {
				synchronized (player) {
					player.notify();
				}
			}
		}
	}

	@Override
	public DicePair getHumanInput() {
		return humanInput;
	}

	@Override
	public void userCrossedField(RowUI ui, NumberFieldUI btn) {
		System.out.println("User crossed field: " + btn);
		playerSelectedDice(new DicePair(ui.getRow().getColor(), btn.getValue()));
	}

	@Override
	public void userCrossedMiss() {
		// remove selected dices and notify player to continue
		playerSelectedDice(DicePair.MISS);
	}

}
