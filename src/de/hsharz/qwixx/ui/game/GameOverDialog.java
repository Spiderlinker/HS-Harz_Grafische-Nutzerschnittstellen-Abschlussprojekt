package de.hsharz.qwixx.ui.game;

import java.util.List;
import java.util.Objects;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialog.DialogTransition;

import de.hsharz.qwixx.model.player.Human;
import de.hsharz.qwixx.model.player.IPlayer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class GameOverDialog {

	private JFXDialog dialog;

	private StackPane dialogContainer;
	private BorderPane root;

	private Label lblHeader;
	private Text lblWinningPlayer;
	private JFXButton btnClose;

	public GameOverDialog(StackPane dialogContainer) {
		this.dialogContainer = Objects.requireNonNull(dialogContainer);

		createWidgets();
		setupInteractions();
		addWidgets();
	}

	private void createWidgets() {
		root = new BorderPane();
		root.setPadding(new Insets(20));
		root.setStyle("");

		lblHeader = new Label("Game over");
		lblHeader.setStyle("-fx-font-size: 50pt; -fx-font-family: Gabriola; ");

		lblWinningPlayer = new Text();
		lblWinningPlayer.setStyle("-fx-font-size: 16pt;");

		btnClose = new JFXButton("Beenden");

		dialog = new JFXDialog(dialogContainer, root, DialogTransition.CENTER);
		dialog.setOverlayClose(false);
	}

	private void setupInteractions() {
		btnClose.setOnAction(e -> dialog.close());
	}

	private void addWidgets() {
		root.setTop(lblHeader);
		root.setCenter(lblWinningPlayer);
		root.setBottom(btnClose);

		BorderPane.setMargin(lblWinningPlayer, new Insets(20, 0, 20, 0));

		BorderPane.setAlignment(lblHeader, Pos.CENTER);
		BorderPane.setAlignment(btnClose, Pos.BOTTOM_RIGHT);
	}

	public void updateWinningPlayer(List<IPlayer> winningPlayer) {
		StringBuilder winningPlayerText = new StringBuilder();
		if (winningPlayer.size() == 1) {
			IPlayer player = winningPlayer.get(0);
			if (player instanceof Human) {
				lblHeader.setText("Herzlichen Glückwunsch!");
				winningPlayerText.append("Du hast gewonnen!");
			} else {
				winningPlayerText.append("Der Spieler " + player.getName() + " hat gewonnen!");
			}
		} else {
			lblHeader.setText("Game Over");
			winningPlayerText.append("Die Spieler:\n");
			for (IPlayer p : winningPlayer) {
				winningPlayerText.append(p.getName() + "\n");
			}
			winningPlayerText.append(" haben gewonnen!");
		}
		lblWinningPlayer.setText(winningPlayerText.toString());
	}

	public JFXDialog getDialog() {
		return dialog;
	}
}
