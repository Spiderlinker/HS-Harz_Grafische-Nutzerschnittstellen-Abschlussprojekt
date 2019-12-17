package de.hsharz.qwixx.ui.board;

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
import de.hsharz.qwixx.ui.GameListener;
import de.hsharz.qwixx.ui.board.row.RowUI;
import de.hsharz.qwixx.ui.board.row.field.NumberFieldUI;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class GameBoardUI implements GameListener, HumanInputSupplier, GameBoardListener, FieldCrossedListener {

	private VBox root;

	private IPlayer player;

	private Map<DiceColor, RowUI> rows = new EnumMap<>(DiceColor.class);
	private ScoreLegend scoreLegend;
	private UserScoreUI userScore;

	private IPlayer playerWaitingForInput;
	private DicesSum humanInput;

	public GameBoardUI(IPlayer player) {
		this.player = Objects.requireNonNull(player);
		player.getGameBoard().addListener(this);

		createWidgets();
		setupInteractions();
		addWidgets();

		disableAllButtons();
	}

	private void createWidgets() {
		root = new VBox(3);
		root.setMinWidth(800);
		root.setPadding(new Insets(20, 20, 0, 20));
		root.setStyle("-fx-background-color: #E3E3E3;");

		rows.put(DiceColor.RED, new RowUI(player.getGameBoard().getRedRow()));
		rows.put(DiceColor.YELLOW, new RowUI(player.getGameBoard().getYellowRow()));
		rows.put(DiceColor.GREEN, new RowUI(player.getGameBoard().getGreenRow()));
		rows.put(DiceColor.BLUE, new RowUI(player.getGameBoard().getBlueRow()));

		scoreLegend = new ScoreLegend();
		userScore = new UserScoreUI(player.getGameBoard().getScore());

	}

	private void setupInteractions() {
		rows.values().forEach(r -> r.addFieldCrossedListener(this));
		root.setOnMouseClicked(e -> {
			if (MouseButton.SECONDARY == e.getButton()) {
				doInput(DicesSum.EMPTY);

			}
		});
	}

	private void addWidgets() {
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

	public Pane getPane() {
		return root;
	}

	@Override
	public void askForInput(IPlayer player, List<DicesSum> dices) {
		playerWaitingForInput = player;

		disableAllButtons();
		highlightButtonsOfDices(dices);
	}

	@Override
	public DicesSum getHumanInput() {
		return humanInput;
	}

	@Override
	public void fieldCrossed(RowUI ui, NumberFieldUI btn) {
		doInput(new DicesSum(ui.getRow().getColor(), btn.getValue()));
	}

	@Override
	public void fieldCrossed(Row rowToCross, Field fieldToCross) {
		RowUI rowToCrossUI = rows.get(rowToCross.getColor());

		System.out.println("Field crossed in Row: " + rowToCross.getColor());
		for (NumberFieldUI btn : rowToCrossUI.getButtons()) {
			if (btn.getValue() == fieldToCross.getValue()) {
				System.out.println("Button found to enable");
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
	public void missCrossed() {
		for (int i = 0; i < scoreLegend.getMissFields().size() - player.getGameBoard().getRemainingMisses(); i++) {
			MissField missField = scoreLegend.getMissFields().get(i);
			missField.setSelected(true);
			missField.setDisabled(true);
		}

		Platform.runLater(() -> userScore.updateScore());
	}

	private void disableAllButtons() {
		rows.values().forEach(this::disableButtonsOfRow);
	}

	private void disableButtonsOfRow(RowUI row) {
		for (NumberFieldUI btn : row.getButtons()) {
			btn.setDisabled(true);
		}
	}

	private void highlightButtonsOfDices(List<DicesSum> dices) {
		rows.values().forEach(row -> highlightButtonsOfDicesForRow(row, dices));
	}

	private void highlightButtonsOfDicesForRow(RowUI row, List<DicesSum> dices) {

		for (int i = row.getButtons().size() - 1; i >= 0; i--) {
			if (row.getRow().getFields().get(i).isCrossed()) {
				break;
			}

			NumberFieldUI btn = row.getButtons().get(i);
			for (DicesSum d : dices) {
				if ((DiceColor.WHITE.equals(d.getColor()) || row.getRow().getColor().equals(d.getColor()))
						&& d.getSum() == btn.getValue()) {
					btn.setDisabled(false);
					break;
				}
			}

		}

	}

	public void highlightGameboard(boolean highlight) {
		DropShadow glowEffect = null;
		if (highlight) {
			glowEffect = new DropShadow();
			glowEffect.setColor(Color.RED);
			glowEffect.setWidth(30);
			glowEffect.setHeight(30);
		}
		root.setEffect(glowEffect);
	}

	@Override
	public void nextPlayersTurn(IPlayer nextPlayer) {
			highlightGameboard(player.equals(nextPlayer));
	}

}
