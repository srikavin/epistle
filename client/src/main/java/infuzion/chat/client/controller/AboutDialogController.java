package infuzion.chat.client.controller;

import infuzion.chat.client.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AboutDialogController {
    private static Stage stage;

    @FXML
    private Label messageLabel;

    public static void show() {
        stage.show();
    }

    @FXML
    private void close(ActionEvent event) {
        stage.close();
    }

    public void setStage(Stage stage) {
        stage.initStyle(StageStyle.UNIFIED);
        stage.setTitle("About");
        AboutDialogController.stage = stage;
        messageLabel.setText("Chat " + Main.version);
    }
}
