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

import com.sun.media.sound.InvalidFormatException;
import infuzion.chat.client.Main;
import infuzion.chat.client.util.SerializableServer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.commons.validator.routines.UrlValidator;

import java.io.IOException;

public class ConnectionOptionsController {

    private static Stage stage;
    @FXML
    public TextField username;
    @FXML
    public TextField ip;
    @FXML
    public TextField port;
    @FXML
    public Button launchButton;
    @FXML
    public Button saveButton;
    public TextField serverName;
    private ConnectionListController connectionListController;

    public static void show() {
        stage.show();
    }

    @FXML
    private void submit() {
        String userNameValue = username.getText();
        String ipValue = ip.getText();
        int portValue = Integer.parseInt(port.getText());
        if (isValid(username.getText(), ip.getText(), port.getText())) {
            if (!connect(userNameValue, ipValue, portValue)) {
                IncorrectSettingsController.show();
            }
        } else {
            IncorrectSettingsController.show();
        }
    }

    public ConnectionOptionsController setStage(Stage stage) {
        ConnectionOptionsController.stage = stage;
        return this;
    }

    public boolean isValid(String name, String ip, String port) {
        try {
            //noinspection ResultOfMethodCallIgnored
            Integer.parseInt(port);
            UrlValidator urlValidator = new UrlValidator(UrlValidator.ALLOW_LOCAL_URLS + UrlValidator.ALLOW_ALL_SCHEMES) {
                @Override
                public boolean isValid(String string) {
                    return super.isValid(string) || super.isValid("http://" + string);
                }
            };

            if (!urlValidator.isValid(ip)) {
                throw new InvalidFormatException();
            }

        } catch (NumberFormatException | InvalidFormatException e) {
            return false;
        }
        return true;
    }

    public boolean connect(String name, String ip, int port) {
        try {
            Main.startClientThread(name, ip, port);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainChat.fxml"));
        try {
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.setTitle("Chat");
            Main.mainChatController = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void init(ConnectionListController connectionListController) {
        this.connectionListController = connectionListController;
    }

    @FXML
    public void save(ActionEvent event) {
        if (isValid(username.getText(), ip.getText(), port.getText())) {
            connectionListController.addServer(new SerializableServer(serverName.getText(), ip.getText(), Integer.parseInt(port.getText()), username.getText()));
        }
        stage.close();
        ip.clear();
        username.clear();
        serverName.clear();
        port.clear();
    }

    @FXML
    public void launch(ActionEvent event) {

    }
}
