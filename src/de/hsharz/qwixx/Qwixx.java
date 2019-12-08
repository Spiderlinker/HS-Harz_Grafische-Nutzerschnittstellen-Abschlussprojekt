package de.hsharz.qwixx;

import java.util.Arrays;
import java.util.List;

import de.hsharz.qwixx.model.Game;
import de.hsharz.qwixx.model.board.GameBoard;
import de.hsharz.qwixx.model.dice.DicesSum;
import de.hsharz.qwixx.model.player.Human;
import de.hsharz.qwixx.model.player.HumanInputSupplier;
import de.hsharz.qwixx.model.player.IPlayer;

public class Qwixx {

	private static List<Integer> score = Arrays.asList(0, 1, 3, 6, 10, 15, 21, 28, 36, 45, 55, 66, 78);

	public static void main(String[] args) {

		GameBoard board = new GameBoard();
		HumanInputSupplier humanInputSupplier = new HumanInputSupplier() {

			@Override
			public void askForInput(IPlayer player, List<DicesSum> dices) {
				player.notify();
			}

			@Override
			public DicesSum getHumanInput() {
				// TODO Auto-generated method stub
				return null;
			}
			
		};
//		IPlayer player1 = new Human(board, humanInputSupplier);

		Game game = new Game();
//		game.addPlayer(player1);
		
		board.setRowClosedSupplier(game);

		game.startGame();
	}

}
