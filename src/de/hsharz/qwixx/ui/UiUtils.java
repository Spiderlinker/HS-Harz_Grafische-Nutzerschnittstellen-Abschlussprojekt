package de.hsharz.qwixx.ui;

import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class UiUtils {

	private UiUtils() {
		// Utility class
	}

	/**
	 * Erstellt einen VBox-Spacer, der den verbleibenden Platz in einer VBox
	 * einnimmt.
	 */
	public Node getVBoxSpacer() {
		Region spacer = new Region();
		VBox.setVgrow(spacer, Priority.ALWAYS);
		return spacer;
	}

	/**
	 * Erstellt einen HBox-Spacer, der den verbleibenden Platz in einer HBox
	 * einnimmt.
	 */
	public static Node getHBoxSpacer() {
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		return spacer;
	}

}