package infuzion.chat.server;

import infuzion.chat.common.DataType;

import java.util.List;

public interface IChatRoom {
    String getName();

    void addClient(IChatClient client);

    void removeClient(IChatClient client);

    List<IChatClient> getClients();

    void sendMessage(String message, IChatClient chatClient);

    void sendData(String data, DataType dataType);
}
