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
