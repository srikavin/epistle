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
import infuzion.chat.client.Main;
import infuzion.chat.client.util.SerializableServer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConnectionListController {
    @FXML
    ListView<String> listView;

    private ObservableList<String> list = FXCollections.observableList(new ArrayList<>());

    private File serversFile;
    private List<SerializableServer> servers = new ArrayList<>();
    private Map<String, Integer> stringIntegerMap = new HashMap<>();
    private int counter = 0;
    private Stage stage;

    public void setServersFile(File serversFile) {
        this.serversFile = serversFile;
    }

    @FXML
    public void launchServer(ActionEvent event) {
        String string = listView.getSelectionModel().getSelectedItem();
        if (string == null) {
            return;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainChat.fxml"));

        try {
            Scene scene = new Scene(loader.load());
            MainChatController controller = loader.getController();
            SerializableServer server = servers.get(stringIntegerMap.get(string));
            Client client = Main.startClientThread(server.getClientName(), server.getIp(), server.getPort());
            controller.init(client, scene);
            controller.show();
            Main.mainChatController = controller;
            stage.hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void show() {
        stage.show();
    }

    public void init(Stage stage) throws IOException, ClassNotFoundException {
        this.stage = stage;
        try {
            ObjectInputStream serversInput = new ObjectInputStream(new FileInputStream(serversFile));
            //noinspection unchecked
            servers = (List<SerializableServer>) serversInput.readObject();
        } catch (EOFException ignored) {
        }
        servers.forEach(this::addToListView);
        listView.setItems(list);
    }

    public void addServer(SerializableServer server) {
        String string = server.getServerName() + " @ " + server.getIp() + ":" + server.getPort();
        addToListView(server);
        servers.add(counter, server);
        save();
    }

    private void addToListView(SerializableServer server) {
        String string = server.getServerName() + " @ " + server.getIp() + ":" + server.getPort();
        stringIntegerMap.put(string, counter);
        list.add(string);
        counter++;
    }

    public void deleteServer(SerializableServer server) {
        servers.remove(server);
        save();
    }

    private void save() {
        try {
            ObjectOutputStream temp = new ObjectOutputStream(new FileOutputStream(serversFile));
            temp.writeObject(servers);
            temp.flush();
            temp.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void quit(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    public void about(ActionEvent event) {
        AboutDialogController.show();
    }

    @FXML
    public void newServer(ActionEvent event) {
        ConnectionOptionsController.show();
    }

    @FXML
    public void editServer(ActionEvent event) {

    }
}
