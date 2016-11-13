package infuzion.chat.server;

import infuzion.chat.common.DataType;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("WeakerAccess")
public class ChatClient implements IChatClient {
    private final static Map<Socket, IChatClient> clientSocketMap = new HashMap<>();
    private final static Map<String, IChatClient> clientStringMap = new HashMap<>();
    private final String name;
    private final UUID uuid;
    private String prefix;
    private DataOutputStream outputStream;
    private IChatRoom currentIChatRoom;
    private Socket socket;

    public ChatClient(String name, UUID uuid, Socket socket) {
        this.name = name;
        this.uuid = uuid;
        try {
            this.outputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.socket = socket;
        clientSocketMap.put(socket, this);
        clientStringMap.put(name, this);
        prefix = "[" + name + "] ";
    }

    public static IChatClient fromSocket(Socket sock) {
        return clientSocketMap.get(sock);
    }

    public static IChatClient fromName(String name) {
        return clientStringMap.get(name);
    }

    public void kick(String message) {
        sendMessage(message);
        clientSocketMap.remove(socket);
        clientStringMap.remove(name);
        try {
            socket.shutdownInput();
            socket.shutdownOutput();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public void sendMessage(String message) {
        sendData(message, DataType.Message);
    }

    public IChatRoom getChatRoom() {
        return currentIChatRoom;
    }

    public void setChatRoom(IChatRoom IChatRoom) {
        this.currentIChatRoom = IChatRoom;
    }

    @SuppressWarnings("Duplicates")
    public void sendData(String data, DataType id) {
        try {
            outputStream.writeByte(id.byteValue);
            outputStream.writeUTF(data);
            outputStream.writeByte(DataType.EndOfData.byteValue);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getPrefix() {
        return prefix;
    }


    @Override
    public boolean equals(Object obj) {
        return obj instanceof IChatClient && this.uuid.equals(((IChatClient) obj).getUuid());
    }
}
