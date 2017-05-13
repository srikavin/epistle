/*
 *
 *  *  Copyright 2016 Infuzion
 *  *
 *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *    you may not use this file except in compliance with the License.
 *  *    You may obtain a copy of the License at
 *  *
 *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *    Unless required by applicable law or agreed to in writing, software
 *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *    See the License for the specific language governing permissions and
 *  *    limitations under the License.
 *
 */

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
        stage.setResizable(false);
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
