package infuzion.chat.server.plugin.event.chat;

import infuzion.chat.server.IChatClient;

public class MessageEvent extends ChatEvent {
    public MessageEvent(IChatClient sender, String message) {
        super(sender, message);
    }
}
