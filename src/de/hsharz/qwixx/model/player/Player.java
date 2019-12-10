package de.hsharz.qwixx.model.player;

import java.util.Objects;

import de.hsharz.qwixx.model.board.GameBoard;

public abstract class Player implements IPlayer {

	private GameBoard board;

	public Player(GameBoard board) {
		this.board = Objects.requireNonNull(board);
	}

	@Override
	public GameBoard getGameBoard() {
		return board;
	}

}
