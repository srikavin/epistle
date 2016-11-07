package infuzion.chat.server;

import infuzion.chat.common.DataType;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

class ChatRoom {
    private final String name;
    private final UUID uuid;
    private List<ChatClient> clients = new LinkedList<>();

    public ChatRoom(String name){
        this.name = name;
        this.uuid = UUID.randomUUID();
    }

    public String getName() {
        return name;
    }

    public void addClient(ChatClient client){
        clients.add(client);
    }

    public void removeClient(ChatClient client){
        clients.remove(client);
    }

    public List<ChatClient> getClients() {
        return clients;
    }

    public void sendMessage(String message, ChatClient chatClient){
        for(ChatClient client: clients){
            client.sendMessage(chatClient.getPrefix() + message);
        }
    }

    public void sendData(String data, DataType dataType){
        for(ChatClient client: clients){
            client.sendData(data, dataType);
        }
    }

    public boolean equals(Object obj) {
        return obj instanceof ChatRoom && ((ChatRoom) obj).uuid.equals(this.uuid);
    }
}

