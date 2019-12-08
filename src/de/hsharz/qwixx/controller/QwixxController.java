package de.hsharz.qwixx.controller;

import java.util.Objects;

import de.hsharz.qwixx.model.Game;
import de.hsharz.qwixx.model.board.row.field.Field;
import de.hsharz.qwixx.ui.GameBoardUI;

public class QwixxController {

	private Game model;
	private GameBoardUI ui;

	public QwixxController(Game model, GameBoardUI ui) {
		this.model = Objects.requireNonNull(model);
		this.ui = Objects.requireNonNull(ui);
	}

	public Game getModel() {
		return model;
	}

	public GameBoardUI getUI() {
		return ui;
	}

	public void missCrossed() {
	}

	public void fieldCrossed(Field field) {
		
	}

	public void finishedTurn() {
		
	}

}
