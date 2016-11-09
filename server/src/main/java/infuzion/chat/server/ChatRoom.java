package infuzion.chat.server;

import infuzion.chat.common.DataType;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class ChatRoom implements IChatRoom {
    private static IChatRoomManager IChatRoomManager;
    private final String name;
    private final UUID uuid;
    private List<IChatClient> clients = new LinkedList<>();

    public ChatRoom(String name){
        this.name = name;
        this.uuid = UUID.randomUUID();
    }

    public static IChatRoomManager getChatRoomManager() {
        return IChatRoomManager;
    }

    static void setChatRoomManager(IChatRoomManager IChatRoomManager) {
        ChatRoom.IChatRoomManager = IChatRoomManager;
    }

    public String getName() {
        return name;
    }

    public void addClient(IChatClient client) {
        clients.add(client);
    }

    public void removeClient(IChatClient client) {
        clients.remove(client);
    }

    public List<IChatClient> getClients() {
        return clients;
    }

    public void sendMessage(String message, IChatClient chatClient) {
        for (IChatClient client : clients) {
            client.sendMessage(chatClient.getPrefix() + message);
        }
    }

    public void sendData(String data, DataType dataType){
        for (IChatClient client : clients) {
            client.sendData(data, dataType);
        }
    }

    public boolean equals(Object obj) {
        return obj instanceof IChatRoom && ((ChatRoom) obj).uuid.equals(this.uuid);
    }
}

