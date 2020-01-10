package de.hsharz.qwixx.ui.game.board;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import de.hsharz.qwixx.model.GameListener;
import de.hsharz.qwixx.model.board.GameBoardListener;
import de.hsharz.qwixx.model.board.row.Row;
import de.hsharz.qwixx.model.board.row.field.Field;
import de.hsharz.qwixx.model.dice.DiceColor;
import de.hsharz.qwixx.model.dice.DicePair;
import de.hsharz.qwixx.model.player.DiceSelectionType;
import de.hsharz.qwixx.model.player.HumanInputSupplier;
import de.hsharz.qwixx.model.player.IPlayer;
import de.hsharz.qwixx.ui.AbstractPane;
import de.hsharz.qwixx.ui.game.board.row.RowUI;
import de.hsharz.qwixx.ui.game.board.row.field.NumberFieldUI;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public abstract class GameBoardUI extends AbstractPane<VBox>
		implements GameListener, HumanInputSupplier, GameBoardListener, FieldCrossedListener, MissFieldListener {

	protected IPlayer player;
	private Label lblName;
	private Label lblHint;

	protected Map<DiceColor, RowUI> rows = new EnumMap<>(DiceColor.class);
	protected ScoreLegend scoreLegend;
	protected UserScoreUI userScore;

	protected boolean shouldNotify = false;
	private DicePair humanInput;
	private boolean isCurrentPlayersTurn = false;
	private DiceSelectionType selectionType;

	private DropShadow glowEffect;

	public GameBoardUI(IPlayer player) {
		super(new VBox());

		this.player = Objects.requireNonNull(player);
		player.getGameBoard().addListener(this);

		createWidgets();
		setupInteractions();
		addWidgets();

		disableAllButtons();
	}

	private void createWidgets() {
		root.setSpacing(3);
		root.setMinWidth(800);
		root.setPadding(new Insets(10, 20, 0, 20));
		root.setStyle("-fx-background-color: #E3E3E3;");

		lblName = new Label(player.getName());
		lblName.setStyle("-fx-font-size: 18pt; -fx-font-weight: bold;");

		lblHint = new Label();
		lblHint.setStyle("-fx-font-size: 18pt; -fx-font-weight: bold;");

		// add for each Row a new RowUI
		player.getGameBoard().getRows().entrySet().forEach(e -> rows.put(e.getKey(), new RowUI(e.getValue())));

		scoreLegend = new ScoreLegend();
		userScore = new UserScoreUI(player.getGameBoard().getScore());

		glowEffect = new DropShadow();
		glowEffect.setColor(Color.WHITE);
		glowEffect.setWidth(30);
		glowEffect.setHeight(30);
		root.setEffect(glowEffect);
	}

	private void setupInteractions() {
		rows.values().forEach(r -> r.addFieldCrossedListener(this));
		scoreLegend.getMissFields().forEach(f -> f.addListener(this));

		root.setOnMouseClicked(e -> {
			if (MouseButton.SECONDARY == e.getButton()) {
				playerSelectedDice(DicePair.EMPTY);
			}
		});
	}

	private void addWidgets() {
		HBox descriptionBox = new HBox();
		descriptionBox.getChildren().add(lblName);
		descriptionBox.getChildren().add(getHBoxSpacer());
		descriptionBox.getChildren().add(lblHint);

		root.getChildren().add(descriptionBox);
		rows.values().forEach(r -> root.getChildren().add(r.getPane()));

		root.getChildren().add(scoreLegend.getPane());
		root.getChildren().add(userScore.getPane());
	}

	private Region getHBoxSpacer() {
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		return spacer;
	}

	@Override
	public void askForInput(List<DicePair> dices, DiceSelectionType selectionType) {
		this.shouldNotify = true;
		this.selectionType = selectionType;

		checkCrossedButtons();
		disableAllButtons();

		setMissFieldsDisabled(!isFirstDiceSelection());
		updateHintLabel();
	}

	private boolean isFirstDiceSelection() {
		return isCurrentPlayersTurn && humanInput == null;
	}

	private void playerSelectedDice(DicePair dice) {
		humanInput = dice;

		if (shouldNotify) {
			synchronized (player) {
				player.notify();
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
	public void fieldCrossed(Row rowToCross, Field fieldToCross) {
		RowUI rowToCrossUI = rows.get(rowToCross.getColor());

		System.out.println("Field crossed in Row: " + rowToCross.getColor());
		for (NumberFieldUI btn : rowToCrossUI.getButtons()) {
			if (btn.getValue() == fieldToCross.getValue()) {
				btn.setDisabled(true);
				btn.setSelected(true);
				break;
			}
		}
		Platform.runLater(() -> userScore.updateScore());
	}

	@Override
	public void rowFinished(DiceColor color) {
		rows.get(color).getRowEnd().setCrossed(true);
	}

	@Override
	public void userCrossedMiss() {
		// remove selected dices and notify player to continue
		playerSelectedDice(DicePair.MISS);
	}

	@Override
	public void missCrossed() {
		for (int i = 0; i < scoreLegend.getMissFields().size(); i++) {
			MissField missField = scoreLegend.getMissFields().get(i);

			boolean isMissAlreadyUsed = i < (scoreLegend.getMissFields().size()
					- player.getGameBoard().getRemainingMisses());
			missField.setSelected(isMissAlreadyUsed);
			missField.setDisabled(isMissAlreadyUsed);
		}

		Platform.runLater(() -> userScore.updateScore());
	}

	private void checkCrossedButtons() {
		for (Entry<DiceColor, RowUI> e : rows.entrySet()) {
			for (int i = 0; i < e.getValue().getButtons().size(); i++) {
				NumberFieldUI numberField = e.getValue().getButtons().get(i);
				numberField.setSelected(player.getGameBoard().getRow(e.getKey()).getFields().get(i).isCrossed());
			}
		}
	}

	protected void disableAllButtons() {
		rows.values().forEach(this::disableButtonsOfRow);
	}

	private void disableButtonsOfRow(RowUI row) {
//		System.out.println("Disabling Row: " + row.getRow().getColor());
		for (NumberFieldUI btn : row.getButtons()) {
//			System.out.println("Disabling " + btn.getValue());
			btn.setDisabled(true);
		}
	}

	public void highlightGameboard(boolean highlight) {
		glowEffect.setColor(highlight ? Color.RED : Color.WHITE);
	}

	private void setMissFieldsDisabled(boolean disabled) {
		scoreLegend.getMissFields().forEach(f -> f.setDisabled(disabled));
	}

	private void updateHintLabel() {
		Platform.runLater(() -> lblHint.setText(
				(DiceSelectionType.COLOR_DICE.equals(selectionType) ? "Farbwürfel" : "Weißen Würfel") + " wählen"));
	}

	@Override
	public void nextPlayersTurn(IPlayer nextPlayer) {
		isCurrentPlayersTurn = isCurrentPlayer(nextPlayer);
		highlightGameboard(isCurrentPlayersTurn);

		humanInput = null;
	}

	private boolean isCurrentPlayer(IPlayer playerToCheck) {
		return player.equals(playerToCheck);
	}

	@Override
	public void invalidDiceChoiceMade(IPlayer player, String msg) {
		humanInput = null;
	}

	@Override
	public void gameOver() {
		// ignore
	}

	public IPlayer getPlayer() {
		return player;
	}

}
