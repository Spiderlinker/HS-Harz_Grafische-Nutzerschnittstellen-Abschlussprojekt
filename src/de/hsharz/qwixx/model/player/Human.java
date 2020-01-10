package de.hsharz.qwixx.model.player;

import java.util.List;
import java.util.Objects;

import de.hsharz.qwixx.model.board.GameBoard;
import de.hsharz.qwixx.model.dice.DicePair;

public class Human extends Player {

	private HumanInputSupplier inputSupplier;

	public Human(String name, GameBoard board) {
		super(name, board);
	}

	public void setHumanInputSupplier(HumanInputSupplier inputSupplier) {
		this.inputSupplier = Objects.requireNonNull(inputSupplier);
	}

	@Override
	public DicePair chooseWhiteDices(List<DicePair> dices) {
		return waitForSelectionAndCrossField(dices, DiceSelectionType.WHITE_DICE);
	}

	@Override
	public DicePair chooseColorDices(List<DicePair> dices) {
		return waitForSelectionAndCrossField(dices, DiceSelectionType.COLOR_DICE);
	}

	private DicePair waitForSelectionAndCrossField(List<DicePair> dices, DiceSelectionType selectionType) {
		DicePair selection = null;
		synchronized (this) {
			try {

				System.out.println("Waiting for human to make input: " + dices);
				inputSupplier.askForInput(dices, selectionType);
				this.wait();
				selection = inputSupplier.getHumanInput();

			} catch (InterruptedException e) {
				System.err.println("Player left the game or system interrupted this thread! " + e.getMessage());
			}
		}

		return selection;
	}
}
