package de.hsharz.qwixx.ui;

import java.util.List;
import java.util.Objects;

import de.hsharz.qwixx.model.board.GameBoardListener;
import de.hsharz.qwixx.model.board.row.field.Field;
import de.hsharz.qwixx.model.dice.DiceColor;
import de.hsharz.qwixx.model.dice.DicesSum;
import de.hsharz.qwixx.model.dice.pair.DicePair;
import de.hsharz.qwixx.model.dice.pair.Pair;
import de.hsharz.qwixx.model.player.HumanInputSupplier;
import de.hsharz.qwixx.model.player.IPlayer;
import de.hsharz.qwixx.ui.row.RowUI;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class GameBoardUI implements HumanInputSupplier, GameBoardListener, FieldCrossedListener {

	private VBox root;

	private IPlayer player;

	private RowUI rowRed;
	private RowUI rowYellow;
	private RowUI rowGreen;
	private RowUI rowBlue;

	private ScoreLegend scoreLegend;
	private UserScoreUI userScore;

	private IPlayer playerWaitingForInput;

	private DicesSum firstSelection;
	private DicesSum secondSelection;
	private int maxSelection;

	public GameBoardUI(IPlayer player) {
		this.player = Objects.requireNonNull(player);
		player.getGameBoard().addListener(this);

		createWidgets();
		setupInteractions();
		addWidgets();
	}

	private void createWidgets() {
		root = new VBox(3);
//		root.setVgap(3);
		root.setPadding(new Insets(20, 20, 0, 20));
		root.setStyle("-fx-background-color: #E3E3E3;");

		rowRed = new RowUI(player.getGameBoard().getRedRow());
		rowYellow = new RowUI(player.getGameBoard().getYellowRow());
		rowGreen = new RowUI(player.getGameBoard().getGreenRow());
		rowBlue = new RowUI(player.getGameBoard().getBlueRow());

		scoreLegend = new ScoreLegend();
		userScore = new UserScoreUI(player.getGameBoard().getScore());

	}

	private void setupInteractions() {
		rowRed.addFieldCrossedListener(this);
		rowYellow.addFieldCrossedListener(this);
		rowGreen.addFieldCrossedListener(this);
		rowBlue.addFieldCrossedListener(this);

		root.setOnMouseClicked(e -> {
			if (MouseButton.SECONDARY == e.getButton()) {
				playerSelectedDice(DicesSum.EMPTY);
			}
		});
	}

	private void playerSelectedDice(DicesSum dice) {
		if (firstSelection == null) {
			firstSelection = new DicesSum(dice.getColor(), dice.getSum());
		} else if (secondSelection == null) {
			secondSelection = new DicesSum(dice.getColor(), dice.getSum());
		}

		if ((maxSelection == 1 && firstSelection != null) //
				|| (maxSelection == 2 && secondSelection != null)) {
			synchronized (playerWaitingForInput) {
				playerWaitingForInput.notify();
			}
		}
	}

	private void addWidgets() {
		root.getChildren().add(rowRed.getPane());
		root.getChildren().add(rowYellow.getPane());
		root.getChildren().add(rowGreen.getPane());
		root.getChildren().add(rowBlue.getPane());

		root.getChildren().add(scoreLegend.getPane());
		root.getChildren().add(userScore.getPane());
	}

	public Pane getPane() {
		return root;
	}

	@Override
	public void askForInput(IPlayer player, List<DicesSum> dices, int minDices, int maxDices) {
		playerWaitingForInput = player;
		maxSelection = maxDices;
		firstSelection = null;
		secondSelection = null;

		disableAllButtons();
		highlightButtonsOfDices(dices);
	}

	@Override
	public Pair<DicesSum> getHumanInput() {
		return new DicePair(firstSelection, secondSelection);
	}

	@Override
	public void fieldCrossed(RowUI ui, CrossButton btn) {
		playerSelectedDice(new DicesSum(ui.getRow().getColor(), btn.getValue()));
	}

	@Override
	public void fieldCrossed(Field fieldToCross) {
		Platform.runLater(() -> userScore.updateScore());
	}

	@Override
	public void rowFinished(DiceColor color) {
		switch (color) {
		case RED:
			rowRed.getRowEnd().setCrossed(true);
			break;
		case YELLOW:
			rowYellow.getRowEnd().setCrossed(true);
			break;
		case GREEN:
			rowGreen.getRowEnd().setCrossed(true);
			break;
		case BLUE:
			rowBlue.getRowEnd().setCrossed(true);
			break;
		default:
			System.out.println("Row with unknown color was finished: " + color);
			break;
		}
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
		disableButtonsOfRow(rowRed);
		disableButtonsOfRow(rowYellow);
		disableButtonsOfRow(rowGreen);
		disableButtonsOfRow(rowBlue);
	}

	private void disableButtonsOfRow(RowUI row) {
		for (CrossButton btn : row.getButtons()) {
			btn.setDisabled(true);
		}
	}

	private void highlightButtonsOfDices(List<DicesSum> dices) {
		highlightButtonsOfDicesForRow(rowRed, dices);
		highlightButtonsOfDicesForRow(rowYellow, dices);
		highlightButtonsOfDicesForRow(rowGreen, dices);
		highlightButtonsOfDicesForRow(rowBlue, dices);
	}

	private void highlightButtonsOfDicesForRow(RowUI row, List<DicesSum> dices) {

		for (int i = row.getButtons().size() - 1; i >= 0; i--) {
			if (row.getRow().getFields().get(i).isCrossed()) {
				break;
			}

			CrossButton btn = row.getButtons().get(i);
			for (DicesSum d : dices) {
				if ((DiceColor.WHITE.equals(d.getColor()) || row.getRow().getColor().equals(d.getColor()))
						&& d.getSum() == btn.getValue()) {
					btn.setDisabled(false);
					break;
				}
			}

		}

	}

}
