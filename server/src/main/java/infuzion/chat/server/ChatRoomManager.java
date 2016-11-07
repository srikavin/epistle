package infuzion.chat.server;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class ChatRoomManager {
    private List<ChatRoom> chatRooms = new ArrayList<>();

    ChatRoomManager() {
        chatRooms.add(new ChatRoom("default"));
        ChatRoom.setChatRoomManager(this);
    }

    public void addChatRoom(ChatRoom chatRoom) {
        chatRooms.add(chatRoom);
    }

    public void addClient(ChatClient client) {
        addClient(client, chatRooms.get(0));

    }

    public void addClient(ChatClient client, ChatRoom room) {
        room.addClient(client);
        client.setChatRoom(room);
        if (!chatRooms.contains(room)) {
            chatRooms.add(room);
        }
    }

    public void addClient(ChatClient client, String roomName) {
        ChatRoom chatRoom = fromString(roomName);
        if (chatRoom != null) {
            addClient(client, chatRoom);
        }
    }

    public ChatRoom fromString(String string) {
        for (ChatRoom e : chatRooms) {
            if (e.getName().equalsIgnoreCase(string)) {
                return e;
            }
        }
        return null;
    }

    public void sendMessageAll(String message, ChatClient client) {
        for (ChatRoom e : chatRooms) {
            sendMessageAll(message, client, e);
        }
    }

    public void sendMessageAll(String message, ChatClient client, ChatRoom chatRoom) {
        chatRoom.sendMessage(message, client);
    }

    public void moveClient(ChatClient client, ChatRoom newRoom) {
        removeClient(client);
        addClient(client, newRoom);
    }

    public void removeClient(ChatClient chatClient) {
        chatRooms.stream().filter(e -> e.getClients().contains(chatClient)).forEach(e -> e.removeClient(chatClient));
    }
}
