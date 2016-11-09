package infuzion.chat.server.plugin.event.reflection;

import java.util.ArrayList;
import java.util.List;

public class HandlerList {
    private static List<Listener> allListeners = new ArrayList<>();
    private volatile List<Listener> handlers = new ArrayList<>();

    public List<Listener> getHandlers() {
        return handlers;
    }

    public void register(Listener listener) {
        handlers.add(listener);
    }

    public List<Listener> getAllListeners() {
        return allListeners;
    }
}
