package infuzion.chat.server.plugin.event.connection;

import infuzion.chat.server.IChatClient;

public class QuitEvent extends ConnectionEvent {
    public QuitEvent(IChatClient client) {
        super(client);
    }
}
