package infuzion.chat.server;

import infuzion.chat.common.DataType;

import java.util.UUID;

public interface IChatClient {
    void kick(String message);

    UUID getUuid();

    String getName();

    void sendMessage(String message);

    IChatRoom getChatRoom();

    void setChatRoom(IChatRoom IChatRoom);

    void sendData(String data, DataType id);

    String getPrefix();
}
