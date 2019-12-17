package de.hsharz.qwixx.ui;

import java.util.Objects;

import javafx.scene.layout.Pane;

public abstract class AbstractPane<P extends Pane> {

	protected P root;

	public AbstractPane(P root) {
		this.root = Objects.requireNonNull(root);
	}

	public P getPane() {
		return root;
	}

}
