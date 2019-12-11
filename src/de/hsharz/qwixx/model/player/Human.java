package de.hsharz.qwixx.model.player;

import java.util.List;
import java.util.Objects;

import de.hsharz.qwixx.model.board.GameBoard;
import de.hsharz.qwixx.model.dice.DicesSum;
import de.hsharz.qwixx.model.dice.pair.Pair;

public class Human extends Player {

	private HumanInputSupplier inputSupplier;

	public Human(GameBoard board) {
		super(board);
	}

	public void setHumanInputSupplier(HumanInputSupplier inputSupplier) {
		this.inputSupplier = Objects.requireNonNull(inputSupplier);
	}

	@Override
	public Pair<DicesSum> chooseDices(List<DicesSum> dices, int minDices, int maxDices) {
		Pair<DicesSum> selection = null;
		synchronized (this) {
			try {

				System.out.println("Waiting for human to make input: " + dices);
				inputSupplier.askForInput(this, dices, minDices, maxDices);
				this.wait();
				selection = inputSupplier.getHumanInput();

				System.out.println("Got input: " + selection);
				
//				if (!selection.isEmpty()) {
//					if (selection.getFirst() != null) {
//						getGameBoard().crossField(selection.getFirst().getColor(), selection.getFirst().getSum());
//					}
//					if (selection.getSecond() != null) {
//						getGameBoard().crossField(selection.getSecond().getColor(), selection.getSecond().getSum());
//					}
//				}

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		return selection;
	}

}
