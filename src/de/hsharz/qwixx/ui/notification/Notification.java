package de.hsharz.qwixx.ui.notification;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import de.hsharz.qwixx.ui.AbstractPane;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class Notification extends AbstractPane<BorderPane> {

	public static final long DEFAULT_DISPLAY_TIME = 5000;

	private long displayTime = DEFAULT_DISPLAY_TIME;
	private Text lblMessage;
	private double opacity = 0.8;

	private FadeTransition transition;
	private ScheduledExecutorService displayTimer;
	private ScheduledFuture<?> scheduledTask;

	public Notification() {
		super(new BorderPane());

		createWidgets();
		addWidgets();
	}

	private void createWidgets() {
		root.setPadding(new Insets(20));
		root.setStyle("-fx-border-color: #999999; -fx-border-radius: 10px; -fx-border-width: 2px; "
				+ "-fx-background-color: white; -fx-background-radius: 11px;");
		root.setMaxSize(200, 100);
		root.setMouseTransparent(true);

		lblMessage = new Text();

		transition = new FadeTransition(Duration.millis(500), root);
		displayTimer = Executors.newSingleThreadScheduledExecutor();
	}

	private void addWidgets() {
		root.setCenter(lblMessage);
	}

	public void setDisplayTime(long millis) {
		if (millis < 0) {
			throw new IllegalArgumentException("DisplayTime must be greater or equals 0");
		}
		this.displayTime = millis;
	}

	public void show(String text) {
		if (scheduledTask != null) {
			scheduledTask.cancel(true);
		}

		Platform.runLater(() -> {
			lblMessage.setText(text);

			transition.setFromValue(0.0);
			transition.setToValue(opacity);
			transition.play();

			scheduledTask = displayTimer.schedule(this::hide, displayTime, TimeUnit.MILLISECONDS);
		});
	}

	public void hide() {
		Platform.runLater(() -> {
			transition.setFromValue(root.getOpacity());
			transition.setToValue(0.0);
			transition.play();
		});
	}

}
