package de.hsharz.qwixx.model.player;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.hsharz.qwixx.model.board.GameBoard;
import de.hsharz.qwixx.model.dice.DicePair;

class HumanTest extends PlayerTest<Human> {

	@Override
	protected Human getPlayer(String name, GameBoard board) {
		return new Human(name, board);
	}

	@Test
	void testExceptionNoHumanInputSupplierSet() {
		Player player = getPlayer("Test", new GameBoard());

		Assertions.assertThrows(NullPointerException.class,
				() -> player.chooseDicePair(new ArrayList<>(), DiceSelectionType.WHITE_DICE));
		Assertions.assertThrows(NullPointerException.class,
				() -> player.chooseDicePair(new ArrayList<>(), DiceSelectionType.COLOR_DICE));
	}

	@Test
	void testExceptionSetHumanInputSupplierNull() {
		Human player = getPlayer("Test", new GameBoard());
		Assertions.assertThrows(NullPointerException.class, () -> player.setHumanInputSupplier(null));
	}

	@Test
	void testHumanInput() {
		Human player = getPlayer("Test", new GameBoard());
		player.setHumanInputSupplier(new TestInputSupplier(player));

		Assertions.assertEquals(DicePair.EMPTY, player.chooseDicePair(new ArrayList<>(), DiceSelectionType.WHITE_DICE));
		Assertions.assertEquals(DicePair.EMPTY, player.chooseDicePair(new ArrayList<>(), DiceSelectionType.COLOR_DICE));

	}

	class TestInputSupplier implements HumanInputSupplier {

		private Player player;

		private boolean humanMadeInput = false;

		public TestInputSupplier(Player player) {
			this.player = player;
		}

		@Override
		public void askForInput(List<DicePair> dices, DiceSelectionType selectionType) {
			new Thread(() -> {
				try {
					// Simuliere Menschliche Eingabe
					Thread.sleep(200);
					humanMadeInput = true;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				System.out.println("Waking up");
				synchronized (player) {
					player.notifyAll();
				}
			}).start();
		}

		@Override
		public DicePair getHumanInput() {
			return humanMadeInput ? DicePair.EMPTY : null;
		}

	}

}
