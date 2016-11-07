package infuzion.chat.server;

import infuzion.chat.common.DataType;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

class ChatClient {
    private final static Map<Socket, ChatClient> clientSocketMap = new HashMap<>();
    private final String name;
    private final UUID uuid;
    private String prefix;
    private String color;
    private DataOutputStream outputStream;
    private ChatRoom currentChatRoom;

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

    static ChatClient fromSocket(Socket sock) {
        return clientSocketMap.get(sock);
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

    ChatRoom getChatRoom() {
        return currentChatRoom;
    }

    void setChatRoom(ChatRoom chatRoom) {
        this.currentChatRoom = chatRoom;
    }

    void sendData(String data, DataType id){
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
        return obj instanceof ChatClient && this.uuid.equals(((ChatClient) obj).getUuid());
    }
}
