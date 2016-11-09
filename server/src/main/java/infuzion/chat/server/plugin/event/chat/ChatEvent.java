package infuzion.chat.server.plugin.event.chat;

import infuzion.chat.server.IChatClient;
import infuzion.chat.server.plugin.event.CancelableEvent;

public abstract class ChatEvent extends CancelableEvent {
    private IChatClient sender;
    private String message;

    public ChatEvent(IChatClient sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    public IChatClient getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
