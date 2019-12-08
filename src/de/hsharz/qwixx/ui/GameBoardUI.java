package de.hsharz.qwixx.ui;

import java.util.List;
import java.util.Objects;

import de.hsharz.qwixx.model.board.GameBoard;
import de.hsharz.qwixx.model.board.row.RowUtils;
import de.hsharz.qwixx.model.dice.DiceColor;
import de.hsharz.qwixx.model.dice.DicesSum;
import de.hsharz.qwixx.model.player.HumanInputSupplier;
import de.hsharz.qwixx.model.player.IPlayer;
import javafx.geometry.Insets;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class GameBoardUI implements HumanInputSupplier, FieldCrossedListener {

	private VBox root;

	private IPlayer player;

	private RowUI rowRed;
	private RowUI rowYellow;
	private RowUI rowGreen;
	private RowUI rowBlue;

	private ScoreLegend scoreLegend;
	private UserScore userScore;

	public GameBoardUI(IPlayer player) {
		this.player = Objects.requireNonNull(player);

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
		userScore = new UserScore();

	}

	private void setupInteractions() {
		rowRed.addFieldCrossedListener(this);
		rowYellow.addFieldCrossedListener(this);
		rowGreen.addFieldCrossedListener(this);
		rowBlue.addFieldCrossedListener(this);
		
		
		root.setOnMouseClicked(e->{
			if(MouseButton.SECONDARY == e.getButton()) {
				humanInput = DicesSum.EMPTY;

				enableAllButtons();

				synchronized (playerWaitingForInput) {
					playerWaitingForInput.notify();
				}
			}
		});
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

	IPlayer playerWaitingForInput;
	DicesSum humanInput;

	@Override
	public void askForInput(IPlayer player, List<DicesSum> dices) {

		playerWaitingForInput = player;
		disableButtonsExcept(dices);

	}

	@Override
	public DicesSum getHumanInput() {
		return humanInput;
	}

	@Override
	public void fieldCrossed(RowUI ui, CrossButton btn) {
		humanInput = new DicesSum(ui.getRow().getColor(), btn.getValue());

		enableAllButtons();

		synchronized (playerWaitingForInput) {
			playerWaitingForInput.notify();
		}
	}

	private void disableButtonsExcept(List<DicesSum> dices) {
		for (CrossButton btn : rowRed.getButtons()) {
			btn.setDisabled(true);
		}
		for (CrossButton btn : rowYellow.getButtons()) {
			btn.setDisabled(true);
		}
		for (CrossButton btn : rowGreen.getButtons()) {
			btn.setDisabled(true);
		}
		for (CrossButton btn : rowBlue.getButtons()) {
			btn.setDisabled(true);
		}

		for (DicesSum sum : dices) {
			for (CrossButton btn : rowRed.getButtons()) {
				if ((DiceColor.RED.equals(sum.getColor()) || DiceColor.WHITE.equals(sum.getColor()))
						&& sum.getSum() == btn.getValue() && !btn.getButton().isSelected()) {

					btn.setDisabled(RowUtils.isCrossedAfterValue(rowRed.getRow(), btn.getValue()));
				}
			}
			for (CrossButton btn : rowYellow.getButtons()) {
				if ((DiceColor.YELLOW.equals(sum.getColor()) || DiceColor.WHITE.equals(sum.getColor()))
						&& sum.getSum() == btn.getValue() && !btn.getButton().isSelected()) {
					btn.setDisabled(RowUtils.isCrossedAfterValue(rowYellow.getRow(), btn.getValue()));
				}
			}
			for (CrossButton btn : rowGreen.getButtons()) {
				if ((DiceColor.GREEN.equals(sum.getColor()) || DiceColor.WHITE.equals(sum.getColor()))
						&& sum.getSum() == btn.getValue() && !btn.getButton().isSelected()) {
					btn.setDisabled(RowUtils.isCrossedAfterValue(rowGreen.getRow(), btn.getValue()));
				}
			}
			for (CrossButton btn : rowBlue.getButtons()) {
				if ((DiceColor.BLUE.equals(sum.getColor()) || DiceColor.WHITE.equals(sum.getColor()))
						&& sum.getSum() == btn.getValue() && !btn.getButton().isSelected()) {
					btn.setDisabled(RowUtils.isCrossedAfterValue(rowBlue.getRow(), btn.getValue()));
				}
			}
		}

	}

	private void enableAllButtons() {
		for (CrossButton btn : rowRed.getButtons()) {
			btn.setDisabled(false);
		}
		for (CrossButton btn : rowYellow.getButtons()) {
			btn.setDisabled(false);
		}
		for (CrossButton btn : rowGreen.getButtons()) {
			btn.setDisabled(false);
		}
		for (CrossButton btn : rowBlue.getButtons()) {
			btn.setDisabled(false);
		}
	}

}
