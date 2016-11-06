package infuzion.chat.server;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"WeakerAccess", "unused"})
public class ChatRoomManager {
    private List<ChatRoom> chatRooms = new ArrayList<>();

    ChatRoomManager() {
        chatRooms.add(new ChatRoom("default"));
    }

    void addChatRoom(ChatRoom chatRoom){
        chatRooms.add(chatRoom);
    }

    void addClient(ChatClient client) {
        addClient(client, chatRooms.get(0));

    }

    void addClient(ChatClient client, ChatRoom room) {
        room.addClient(client);
        client.setChatRoom(room);
        if (!chatRooms.contains(room)) {
            chatRooms.add(room);
        }
    }

    void addClient(ChatClient client, String roomName) {
        ChatRoom chatRoom = fromString(roomName);
        if (chatRoom != null) {
            addClient(client, chatRoom);
        }
    }

    ChatRoom fromString(String string) {
        for (ChatRoom e : chatRooms) {
            if (e.getName().equalsIgnoreCase(string)) {
                return e;
            }
        }
        return null;
    }

    void sendMessageAll(String message, ChatClient client) {
        for (ChatRoom e : chatRooms) {
            sendMessageAll(message, client, e);
        }
    }

    void sendMessageAll(String message, ChatClient client, ChatRoom chatRoom) {
        chatRoom.sendMessage(message, client);
    }

    public void moveClient(ChatClient client, ChatRoom newRoom) {
        removeClient(client);

    }

    public void removeClient(ChatClient chatClient) {
        for (ChatRoom e : chatRooms) {
            if (e.getClients().contains(chatClient)) {
                e.removeClient(chatClient);
            }
        }
    }
}
