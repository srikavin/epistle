package infuzion.chat.server.plugin.event.reflection;

import infuzion.chat.server.plugin.Plugin;
import infuzion.chat.server.plugin.event.Event;
import infuzion.chat.server.plugin.event.IEventListener;

import java.lang.reflect.Method;

public class Listener {
    private final Plugin plugin;
    private final EventPriority priority;
    private final Class<? extends Event> event;
    private final Method listenerMethod;
    private final IEventListener eventListener;

    public Listener(Plugin plugin, EventPriority priority, Class<? extends Event> event, Method listener, IEventListener eventListener) {
        this.plugin = plugin;
        this.priority = priority;
        this.event = event;
        this.listenerMethod = listener;
        this.eventListener = eventListener;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public EventPriority getPriority() {
        return priority;
    }

    public Method getListenerMethod() {
        return listenerMethod;
    }

    public Class<? extends Event> getEvent() {
        return event;
    }

    public IEventListener getEventListener() {
        return eventListener;
    }
}
