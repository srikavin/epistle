package infuzion.chat.server;

import infuzion.chat.common.DataType;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChatClient {
    private final static Map<Socket, ChatClient> clientSocketMap = new HashMap<>();
    private final String name;
    private final UUID uuid;
    private String prefix;
    private String color;
    private DataOutputStream outputStream;

    ChatClient(String name, UUID uuid, Socket socket) {
        this.name = name;
        this.uuid = uuid;
        try {
            this.outputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        clientSocketMap.put(socket, this);
        prefix = "[" + name + "] ";
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    void sendMessage(String message){
        sendData(message, DataType.Message);

    }

    void sendData(String data, DataType id){
        try {
            outputStream.writeByte(id.byteValue);
            outputStream.writeUTF(data);
            outputStream.writeUTF(String.valueOf(DataType.EndOfData));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static ChatClient fromSocket(Socket sock){
        return clientSocketMap.get(sock);
    }

    public String getPrefix() {
        return prefix;
    }
}
