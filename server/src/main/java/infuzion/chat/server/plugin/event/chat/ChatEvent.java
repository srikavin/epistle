package infuzion.chat.server.plugin.event.chat;

import infuzion.chat.server.IChatClient;
import infuzion.chat.server.plugin.event.Cancelable;
import infuzion.chat.server.plugin.event.Event;

public abstract class ChatEvent extends Event implements Cancelable {
    private IChatClient sender;
    private String message;
    private boolean canceled = false;

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

    @Override
    public boolean isCanceled() {
        return canceled;
    }

    @Override
    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }
}
