package de.hsharz.qwixx.ui.game.board;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hsharz.qwixx.model.board.GameBoardListener;
import de.hsharz.qwixx.model.board.row.Row;
import de.hsharz.qwixx.model.board.row.field.Field;
import de.hsharz.qwixx.model.dice.DiceColor;
import de.hsharz.qwixx.model.dice.DicesSum;
import de.hsharz.qwixx.model.player.HumanInputSupplier;
import de.hsharz.qwixx.model.player.IPlayer;
import de.hsharz.qwixx.ui.AbstractPane;
import de.hsharz.qwixx.ui.game.GameListener;
import de.hsharz.qwixx.ui.game.board.row.RowUI;
import de.hsharz.qwixx.ui.game.board.row.field.NumberFieldUI;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public abstract class GameBoardUI extends AbstractPane<VBox>
		implements GameListener, HumanInputSupplier, GameBoardListener, FieldCrossedListener, MissFieldListener {

	protected IPlayer player;
	private Label lblName;

	protected Map<DiceColor, RowUI> rows = new EnumMap<>(DiceColor.class);
	protected ScoreLegend scoreLegend;
	protected UserScoreUI userScore;

	protected IPlayer playerWaitingForInput;
	protected DicesSum humanInput;

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

		rows.put(DiceColor.RED, new RowUI(player.getGameBoard().getRedRow()));
		rows.put(DiceColor.YELLOW, new RowUI(player.getGameBoard().getYellowRow()));
		rows.put(DiceColor.GREEN, new RowUI(player.getGameBoard().getGreenRow()));
		rows.put(DiceColor.BLUE, new RowUI(player.getGameBoard().getBlueRow()));

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
				doInput(DicesSum.EMPTY);

			}
		});
	}

	private void addWidgets() {
		root.getChildren().add(lblName);
		rows.values().forEach(r -> root.getChildren().add(r.getPane()));

		root.getChildren().add(scoreLegend.getPane());
		root.getChildren().add(userScore.getPane());

	}

	private void doInput(DicesSum input) {
		humanInput = input;

		disableAllButtons();

		if (playerWaitingForInput != null) {
			synchronized (playerWaitingForInput) {
				playerWaitingForInput.notify();
			}
		}
	}

	@Override
	public DicesSum getHumanInput() {
		return humanInput;
	}

	@Override
	public void userCrossedField(RowUI ui, NumberFieldUI btn) {
		System.out.println("User crossed field: " + btn);
		doInput(new DicesSum(ui.getRow().getColor(), btn.getValue()));
		// Fehlerbehandlung bei falschem Input
	}

	@Override
	public void fieldCrossed(Row rowToCross, Field fieldToCross) {
		RowUI rowToCrossUI = rows.get(rowToCross.getColor());

		System.out.println("Field crossed in Row: " + rowToCross.getColor());
		for (NumberFieldUI btn : rowToCrossUI.getButtons()) {
			if (btn.getValue() == fieldToCross.getValue()) {
				btn.setLocked(true);
				btn.setDisabled(true);
				btn.getButton().setSelected(true);
				btn.showCrossImage();
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
		getPlayer().getGameBoard().crossMiss();
	}

	@Override
	public void missCrossed() {
		for (int i = 0; i < scoreLegend.getMissFields().size() - player.getGameBoard().getRemainingMisses(); i++) {
			MissField missField = scoreLegend.getMissFields().get(i);
			missField.setSelected(true);
			missField.setDisabled(true);
		}

		Platform.runLater(() -> userScore.updateScore());
	}

	@Override
	public void askForInput(IPlayer player, List<DicesSum> dices) {
		playerWaitingForInput = player;
		disableAllButtons();
	}

	protected void disableAllButtons() {
		rows.values().forEach(this::disableButtonsOfRow);
	}

	private void disableButtonsOfRow(RowUI row) {
		for (NumberFieldUI btn : row.getButtons()) {
			btn.setDisabled(true);
		}
	}

	public void highlightGameboard(boolean highlight) {
		glowEffect.setColor(highlight ? Color.RED : Color.WHITE);
	}

	@Override
	public void nextPlayersTurn(IPlayer nextPlayer) {
		highlightGameboard(player.equals(nextPlayer));
	}

	@Override
	public void gameOver() {
		// ignore
	}

	public IPlayer getPlayer() {
		return player;
	}

}
