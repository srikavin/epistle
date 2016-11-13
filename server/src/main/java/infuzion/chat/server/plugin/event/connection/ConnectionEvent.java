package infuzion.chat.server.plugin.event.connection;

import infuzion.chat.server.IChatClient;
import infuzion.chat.server.plugin.event.Event;

public abstract class ConnectionEvent extends Event {
    private final IChatClient client;

    public ConnectionEvent(IChatClient client) {
        this.client = client;
    }

    public IChatClient getClient() {
        return client;
    }
}
