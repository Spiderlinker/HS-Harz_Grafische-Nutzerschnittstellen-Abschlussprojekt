package de.hsharz.qwixx.model.player;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.hsharz.qwixx.model.board.GameBoard;

abstract class PlayerTest<P extends Player> {

	protected abstract P getPlayer(String name, GameBoard board);

	@Test
	void testExceptionNameNull() {
		Assertions.assertThrows(NullPointerException.class, () -> getPlayer(null, null));
	}

	@Test
	void testExceptionNameEmpty() {
		Assertions.assertThrows(NullPointerException.class, () -> getPlayer("", null));
	}

	@Test
	void testExceptionGameBoardNull() {
		Assertions.assertThrows(NullPointerException.class, () -> getPlayer("Test", null));
	}

	@Test
	void testGetName() {
		String testName = "Test";

		Player testPlayer = getPlayer(testName, new GameBoard());
		Assertions.assertEquals(testName, testPlayer.getName());
	}

	@Test
	void testGetGameBoard() {
		GameBoard testBoard = new GameBoard();
		Player testPlayer = getPlayer("Test", testBoard);
		Assertions.assertEquals(testBoard, testPlayer.getGameBoard());
	}

	@Test
	void testToStringContainsPlayerName() {
		String testName = "Test";
		Player testPlayer = getPlayer(testName, new GameBoard());
		Assertions.assertTrue(testPlayer.toString().contains(testName));
	}

}
