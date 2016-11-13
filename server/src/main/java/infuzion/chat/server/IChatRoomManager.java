package infuzion.chat.server;

public interface IChatRoomManager {
    void addChatRoom(IChatRoom IChatRoom);

    void addClient(IChatClient client);

    void addClient(IChatClient client, IChatRoom room);

    void addClient(IChatClient client, String roomName);

    IChatRoom fromString(String string);

    void sendMessageAll(String message, IChatClient client);

    void sendMessageAll(String message, IChatClient client, IChatRoom IChatRoom);

    void moveClient(IChatClient client, IChatRoom newRoom);

    void removeClient(IChatClient IChatClient);

    void kickClient(IChatClient IChatClient, String message);

    void kickClient(IChatClient IChatClient);
}
