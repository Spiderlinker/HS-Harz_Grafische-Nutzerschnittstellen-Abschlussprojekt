package de.hsharz.qwixx.ui;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;
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
	 * 
	 * @return Spacer in VBox
	 */
	public static Node getVBoxSpacer() {
		Region spacer = new Region();
		VBox.setVgrow(spacer, Priority.ALWAYS);
		return spacer;
	}

	/**
	 * Erstellt einen HBox-Spacer, der den verbleibenden Platz in einer HBox
	 * einnimmt.
	 * 
	 * @return Spacer in HBox
	 */
	public static Node getHBoxSpacer() {
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		return spacer;
	}

	/**
	 * Erstellt einen GridPane-Horizontal-Spacer, der den verbleibenden Platz in dem
	 * GridPane einnimmt.
	 * 
	 * @return Horizontal Spacer in GridPane
	 */
	public static Node getGridPaneVSpacer() {
		Region spacer = new Region();
		GridPane.setVgrow(spacer, Priority.ALWAYS);
		return spacer;
	}

}
