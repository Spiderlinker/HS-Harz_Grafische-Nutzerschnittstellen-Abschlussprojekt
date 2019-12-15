package de.hsharz.qwixx.model.player;

import java.util.Objects;

import de.hsharz.qwixx.model.board.GameBoard;
import de.hsharz.qwixx.utils.StringUtils;

public abstract class Player implements IPlayer {

	private String name;
	private GameBoard board;

	public Player(String name, GameBoard board) {
		this.name = StringUtils.requireNonNullOrEmpty(name);
		this.board = Objects.requireNonNull(board);
	}

	@Override
	public GameBoard getGameBoard() {
		return board;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + ": " + getName();
	}

}
