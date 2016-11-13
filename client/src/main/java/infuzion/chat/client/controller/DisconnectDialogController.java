package infuzion.chat.client.controller;

import infuzion.chat.client.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.PrintWriter;
import java.io.StringWriter;

public class DisconnectDialogController {
    private Stage stage;

    @FXML
    private Label detailsLabel;

    @FXML
    private TitledPane detailsPane;

    @FXML
    private Label messageLabel;

    public void show(Throwable error) {
        messageLabel.setText(error.getLocalizedMessage());
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        error.printStackTrace(pw);
        detailsLabel.setText(sw.toString()); // stack trace as a string
        detailsPane.heightProperty().addListener(((observable, oldValue, newValue) -> stage.sizeToScene()));
        stage.show();
    }

    @FXML
    private void close(ActionEvent event) {
        stage.close();
    }

    public void setStage(Stage stage) {
        stage.initStyle(StageStyle.UNIFIED);
        stage.setTitle("Disconnected!");
        this.stage = stage;
    }

    @FXML
    public void quit(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    public void serverSelector(ActionEvent event) {
        Main.connectionListController.show();
        Main.mainChatController.close();
        stage.close();
    }
}
