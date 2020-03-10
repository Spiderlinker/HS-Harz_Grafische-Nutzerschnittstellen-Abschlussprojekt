package de.hsharz.qwixx.ui.game.board;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import de.hsharz.qwixx.model.GameListener;
import de.hsharz.qwixx.model.board.GameBoard;
import de.hsharz.qwixx.model.board.GameBoardListener;
import de.hsharz.qwixx.model.board.UserScore;
import de.hsharz.qwixx.model.board.row.Row;
import de.hsharz.qwixx.model.board.row.field.Field;
import de.hsharz.qwixx.model.dice.DiceColor;
import de.hsharz.qwixx.model.player.IPlayer;
import de.hsharz.qwixx.ui.AbstractPane;
import de.hsharz.qwixx.ui.UiUtils;
import de.hsharz.qwixx.ui.game.board.row.RowUI;
import de.hsharz.qwixx.ui.game.board.row.field.NumberFieldUI;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public abstract class GameBoardUI extends AbstractPane<VBox> implements GameListener, GameBoardListener {

	protected IPlayer player;

	protected HBox boxGameBoardHeader;
	private Label lblName;

	protected Map<DiceColor, RowUI> rows = new EnumMap<>(DiceColor.class);
	protected ScoreLegend scoreLegend;
	protected UserScoreUI userScore;

	private DropShadow glowEffect;

	public GameBoardUI(IPlayer player) {
		super(new VBox());

		this.player = Objects.requireNonNull(player);
		player.getGameBoard().addListener(this);

		createWidgets();
		addWidgets();

		disableAllButtons();
	}

	private void createWidgets() {
		root.setSpacing(3);
		root.setMinWidth(800);
		root.setPadding(new Insets(10, 20, 0, 20));
		root.setStyle("-fx-background-color: #E3E3E3;");

		boxGameBoardHeader = new HBox(20);

		lblName = new Label(player.getName());
		lblName.setStyle("-fx-font-size: 18pt; -fx-font-weight: bold;");
		lblName.maxWidthProperty().bind(Bindings.divide(root.widthProperty(), 2.5));

		// add for each Row a new RowUI
		player.getGameBoard().getRows().entrySet().forEach(e -> rows.put(e.getKey(), new RowUI(e.getValue())));

		List<Integer> scoreList = UserScore.SCORE_PER_CROSS.stream().filter(i -> i != 0).collect(Collectors.toList());
		scoreLegend = new ScoreLegend(scoreList, GameBoard.AMOUNT_MISSES);
		userScore = new UserScoreUI(player.getGameBoard().getScore());

		glowEffect = new DropShadow();
		glowEffect.setColor(Color.WHITE);
		glowEffect.setWidth(30);
		glowEffect.setHeight(30);
		root.setEffect(glowEffect);
	}

	private void addWidgets() {
		boxGameBoardHeader.getChildren().add(lblName);
		boxGameBoardHeader.getChildren().add(UiUtils.getHBoxSpacer());

		root.getChildren().add(boxGameBoardHeader);
		rows.values().forEach(r -> root.getChildren().add(r.getPane()));

		root.getChildren().add(scoreLegend.getPane());
		root.getChildren().add(userScore.getPane());
	}

	@Override
	public void fieldCrossed(Row rowToCross, Field fieldToCross) {
		userScore.updateScore();

		System.out.println("Field crossed in Row: " + rowToCross.getColor());
		RowUI rowToCrossUI = rows.get(rowToCross.getColor());
		for (NumberFieldUI btn : rowToCrossUI.getButtons()) {
			if (btn.getValue() == fieldToCross.getValue()) {
				btn.setDisabled(true);
				btn.setSelected(true);
				break;
			}
		}
	}

	@Override
	public void rowFinished(DiceColor color) {
		rows.get(color).getRowEnd().setCrossed(true);
	}

	@Override
	public void missCrossed() {
		userScore.updateScore();

		for (int i = 0; i < scoreLegend.getMissFields().size(); i++) {
			MissField missField = scoreLegend.getMissFields().get(i);

			boolean isMissAlreadyUsed = i < (scoreLegend.getMissFields().size()
					- player.getGameBoard().getRemainingMisses());
			missField.setSelected(isMissAlreadyUsed);
			missField.setDisabled(isMissAlreadyUsed);
		}
	}

	protected void disableAllButtons() {
		for (RowUI row : rows.values()) {
			disableButtonsOfRow(row);
		}
	}

	private void disableButtonsOfRow(RowUI row) {
		for (NumberFieldUI btn : row.getButtons()) {
			btn.setDisabled(true);
		}
	}

	public void highlightGameboard(boolean highlight) {
		Platform.runLater(() -> glowEffect.setColor(highlight ? Color.RED : Color.WHITE));
	}

	@Override
	public void nextPlayersTurn(IPlayer nextPlayer) {
		boolean isCurrentPlayersTurn = player.equals(nextPlayer);
		highlightGameboard(isCurrentPlayersTurn);

		strokeClosedRows();
	}

	private void strokeClosedRows() {
		for (RowUI row : rows.values()) {
			if (player.getGameBoard().getRowClosedSupplier().isRowClosed(row.getRow().getColor())) {
				row.setStroked(true);
			}
		}
	}

	public IPlayer getPlayer() {
		return player;
	}

}
