package infuzion.chat.client.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class IncorrectSettingsController {
    private static Stage stage;

    public static void show() {
        stage.show();
    }

    @FXML
    private void close(ActionEvent event) {
        stage.close();
    }

    public void setStage(Stage stage) {
        stage.initStyle(StageStyle.UNIFIED);
        stage.setTitle("Invalid Settings");
        IncorrectSettingsController.stage = stage;
    }
}
