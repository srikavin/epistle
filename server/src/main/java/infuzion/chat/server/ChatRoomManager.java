package infuzion.chat.server;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class ChatRoomManager implements IChatRoomManager {
    private List<IChatRoom> IChatRooms = new ArrayList<>();

    ChatRoomManager() {
        IChatRooms.add(new ChatRoom("default"));
        ChatRoom.setChatRoomManager(this);
    }

    @Override
    public void addChatRoom(IChatRoom IChatRoom) {
        IChatRooms.add(IChatRoom);
    }

    @Override
    public void addClient(IChatClient client) {
        addClient(client, IChatRooms.get(0));

    }

    @Override
    public void addClient(IChatClient client, IChatRoom room) {
        room.addClient(client);
        client.setChatRoom(room);
        if (!IChatRooms.contains(room)) {
            IChatRooms.add(room);
        }
    }

    @Override
    public void addClient(IChatClient client, String roomName) {
        IChatRoom IChatRoom = fromString(roomName);
        if (IChatRoom != null) {
            addClient(client, IChatRoom);
        }
    }

    @Override
    public IChatRoom fromString(String string) {
        for (IChatRoom e : IChatRooms) {
            if (e.getName().equalsIgnoreCase(string)) {
                return e;
            }
        }
        return null;
    }

    @Override
    public void sendMessageAll(String message, ChatClient client) {
        for (IChatRoom e : IChatRooms) {
            sendMessageAll(message, client, e);
        }
    }

    @Override
    public void sendMessageAll(String message, IChatClient client, IChatRoom IChatRoom) {
        IChatRoom.sendMessage(message, client);
    }

    @Override
    public void moveClient(IChatClient client, IChatRoom newRoom) {
        removeClient(client);
        addClient(client, newRoom);
    }

    @Override
    public void removeClient(IChatClient IChatClient) {
        IChatRooms.stream().filter(e -> e.getClients().contains(IChatClient)).forEach(e -> e.removeClient(IChatClient));
    }

    @Override
    public void kickClient(IChatClient IChatClient, String message) {
        removeClient(IChatClient);
        IChatClient.kick(message);
    }

    @Override
    public void kickClient(IChatClient IChatClient) {
        kickClient(IChatClient, "");
    }
}
