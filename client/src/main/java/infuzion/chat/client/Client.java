package infuzion.chat.client;

import infuzion.chat.common.DataType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.*;

class Client implements Runnable {
    private static Controller controller;
    private DataInputStream input;
    private DataOutputStream output;
    private Map<UUID, String> uuidStringMap = new HashMap<>();

    Client(String ip, int port, String username) throws IOException {
        Socket sock = new Socket(ip, port);
        input = new DataInputStream(sock.getInputStream());
        output = new DataOutputStream(sock.getOutputStream());
        output.writeByte(DataType.ClientHello.byteValue);
        output.writeUTF(username);
        output.writeByte(DataType.EndOfData.byteValue);
        Controller.setClient(this);

        Timer heartbeat = new Timer();
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
                    disconnection();
                }
            }
        }, 10, 5000);
    }

    public static void setController(Controller controller) {
        Client.controller = controller;
    }

    public void disconnection() {
//        this
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
            disconnection();
        }
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public void run() {
        try {
            while (true) {
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
                    controller.displayMessage(message + "\n");
                }
                Thread.sleep(250);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            disconnection();
        }
    }
}
