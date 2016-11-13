/*
 *  Copyright 2016 Infuzion
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package infuzion.chat.client.controller;

import infuzion.chat.client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainChatController {
    private static TextArea chatArea;
    private Scene scene;
    private Stage stage;
    private Client client;
    @FXML
    private TextArea chatTextArea;
    @FXML
    private TextField inputField;

    public MainChatController() {
    }

    public static void displayMessage(String string) {
        chatArea.appendText(string);
    }

    public void show() {
        stage = new Stage();
        stage.initStyle(StageStyle.UNIFIED);
        stage.setScene(scene);
        stage.show();
    }

    public void init(Client client, Scene scene) {
        this.client = client;
        this.scene = scene;
        this.scene.setFill(Color.TRANSPARENT);
        chatArea = chatTextArea;
    }

    public void close() {
        stage.close();
    }

    @FXML
    public void onChat(ActionEvent event) {
        client.sendMessage(inputField.getText());
        inputField.clear();
    }

    public void quitMenuAction(ActionEvent event) {
        System.exit(0);
    }
}
