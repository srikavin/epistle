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

package infuzion.chat.client;

import infuzion.chat.client.controller.DisconnectDialogController;
import infuzion.chat.client.controller.MainChatController;
import infuzion.chat.common.DataType;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Client implements Runnable {
    private DataInputStream input;
    private DataOutputStream output;
    private Map<UUID, String> uuidStringMap = new HashMap<>();
    private volatile boolean disconnected = false;
    private volatile boolean disconnectHandled = false;
    private Timer heartbeat;

    Client(String ip, int port, String username) throws IOException {
        Socket sock = new Socket(ip, port);
        input = new DataInputStream(sock.getInputStream());
        output = new DataOutputStream(sock.getOutputStream());
        output.writeByte(DataType.ClientHello.byteValue);
        output.writeUTF(username);
        output.writeByte(DataType.EndOfData.byteValue);

        heartbeat = new Timer();
        heartbeat.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    output.writeByte(DataType.Heartbeat.byteValue);
                    output.writeUTF("heart");
                    output.writeByte(DataType.EndOfData.byteValue);
                } catch (IOException e) {
                    this.cancel();
                    e.printStackTrace();
                    disconnection(e);
                }
            }
        }, 10, 5000);
    }

    private void disconnection(Throwable throwable) {
        if (!Platform.isFxApplicationThread()) {
            if (disconnected) {
                return;
            }
            disconnected = true;
            Platform.runLater(() -> disconnection(throwable));
            return;
        }
        try {
            if (disconnectHandled) {
                return;
            }
            disconnectHandled = true;
            heartbeat.cancel();
            input.close();
            output.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/disconnectDialog.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(loader.load());
            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);
            DisconnectDialogController disconnectDialogController = loader.getController();
            disconnectDialogController.setStage(stage);
            disconnectDialogController.show(throwable);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        if (message.startsWith("/")) {
            sendData(message, DataType.Command);
        } else {
            sendData(message, DataType.Message);
        }

    }

    @SuppressWarnings("Duplicates")
    public void sendData(String data, DataType type) {
        try {
            output.writeByte(type.byteValue);
            output.writeUTF(data);
            output.writeByte(DataType.EndOfData.byteValue);
        } catch (IOException e) {
            e.printStackTrace();
            disconnection(e);
        }
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public void run() {
        try {
            while (true) {
                long curTime = System.currentTimeMillis();
                if (input.available() <= 0) {
                    continue;
                }
                byte messageType = input.readByte();
                String message = input.readUTF();
                byte end = input.readByte();
                if (end != DataType.EndOfData.byteValue) {
                    continue;
                }
                DataType mType = DataType.valueOf(messageType);
                if (mType == null) {
                    continue;
                }

                if (mType.equals(DataType.Message)) {
                    System.out.println(message);
                    MainChatController.displayMessage(message + "\n");
                }
                while (curTime + 100 > System.currentTimeMillis()) {
                    long toSleep = (curTime + 100) - System.currentTimeMillis();
                    TimeUnit.MILLISECONDS.sleep(toSleep);
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            disconnection(e);
        }
    }
}
