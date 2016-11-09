package infuzion.chat.server.plugin.event;

import infuzion.chat.server.plugin.event.reflection.HandlerList;
import infuzion.chat.server.plugin.event.reflection.Listener;

import java.util.List;

public abstract class Event {
    private String name;

    public Event() {
    }

    public static List<Listener> getAllHandlers() {

        return new HandlerList().getAllListeners();
    }

//    abstract HandlerList getHandlers();

    public String getName() {
        if (this.name == null) {
            return getClass().getSimpleName();
        }
        return name;
    }
}
