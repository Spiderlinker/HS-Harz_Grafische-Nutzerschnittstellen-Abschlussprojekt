package de.hsharz.qwixx.model.player;

import java.util.List;
import java.util.Objects;

import de.hsharz.qwixx.model.board.GameBoard;
import de.hsharz.qwixx.model.dice.DicesSum;

public class Human extends Player {

	private GameBoard board;
	private HumanInputSupplier inputSupplier;

	public Human(GameBoard board) {
		this.board = Objects.requireNonNull(board);
	}

	public void setHumanInputSupplier(HumanInputSupplier inputSupplier) {
		this.inputSupplier = Objects.requireNonNull(inputSupplier);
	}

	@Override
	public DicesSum chooseWhiteDices(List<DicesSum> dices) {
		return waitForSelectionAndCrossField(dices);
	}

	@Override
	public DicesSum chooseColorDices(List<DicesSum> dices) {
		return waitForSelectionAndCrossField(dices);
	}

	private DicesSum waitForSelectionAndCrossField(List<DicesSum> dices) {
		DicesSum selection = null;
		synchronized (this) {
			try {

				System.out.println("Waiting for human to make input: " + dices);
				inputSupplier.askForInput(this, dices);
				this.wait();
				selection = inputSupplier.getHumanInput();

				if (!selection.equals(DicesSum.EMPTY)) {
					board.crossField(selection.getColor(), selection.getSum());
				}

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		return selection;
	}

	@Override
	public GameBoard getGameBoard() {
		return board;
	}

}
