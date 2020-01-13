package de.hsharz.qwixx.model.player;

import java.util.Objects;

import de.hsharz.qwixx.model.board.GameBoard;
import de.hsharz.qwixx.utils.StringUtils;

/**
 * Diese Klasse bietet eine teilweise Implementierung des Interfaces
 * {@link IPlayer}. Die Erzeugung, der Name und das Spielfeld werden in dieser
 * Klasse verwaltet und alle dazugehörigen Methoden sind implementiert.
 * 
 * @author Oliver
 */
public abstract class Player implements IPlayer {

	/** Name des Spielers */
	private String name;
	/** Spieldfeld / Spielblock des Spielers */
	private GameBoard board;

	/**
	 * Erzeugt einen neuen Spieler mit dem gegebenen Namen {@code name} und dem
	 * gegebenen Spieldfeld / Spielblock {@code board}. <br>
	 * Beide gegebenen Parameter dürfen nicht {@code null} sein. Zudem darf der Name
	 * nicht leer sein. <br>
	 * Falls ein null-Wert übergeben werden sollte, so wird eine
	 * {@link NullPointerException} geworfen.
	 * 
	 * @param name  Name des Spielers, nicht {@code null} und nicht leer.
	 * @param board Spielfeld des Spielers, nicht {@code null}
	 */
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
