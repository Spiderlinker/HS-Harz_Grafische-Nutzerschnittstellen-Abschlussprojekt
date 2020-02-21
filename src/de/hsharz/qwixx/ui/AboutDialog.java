package de.hsharz.qwixx.ui;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javafx.geometry.Insets;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class AboutDialog extends AbstractPane<VBox> {

	private Label lblAuthor;
	
	private Hyperlink linkQwixx;
	private Hyperlink linkJFoenix;
	
	private TextFlow flowQwixx;
	private TextFlow flowLibs;

	public AboutDialog() {
		super(new VBox());

		createWidgets();
		setupInteractions();
		addWidgets();
	}

	private void createWidgets() {
		root.setPadding(new Insets(20));
		root.setSpacing(20);

		lblAuthor = new Label("Entwickelt von Oliver Lindemann");
		lblAuthor.setStyle("-fx-font-size: 16pt;");

		Text lblQwixx = new Text("Eine Nachbildung des Gesellschaftspiels 'Qwixx'\n");
		linkQwixx = new Hyperlink("https://nsv.de/nsvspielttv/qwixx_video/");

		Text lblLibs = new Text("Verwendete Bibliotheken: \n - JFoenix: ");
		linkJFoenix = new Hyperlink("http://www.jfoenix.com/");

		flowQwixx = new TextFlow(lblQwixx, linkQwixx);
		flowLibs = new TextFlow(lblLibs, linkJFoenix);
	}

	private void setupInteractions() {
		linkQwixx.setOnAction(e -> openLink(linkQwixx.getText()));
		linkJFoenix.setOnAction(e -> openLink(linkJFoenix.getText()));

	}

	private void addWidgets() {
		root.getChildren().add(lblAuthor);
		root.getChildren().add(flowQwixx);
		root.getChildren().add(flowLibs);
	}

	private void openLink(String link) {
		try {
			Desktop.getDesktop().browse(new URI(link));
		} catch (IOException | URISyntaxException ex) {
			ex.printStackTrace();
		}
	}

}
