package infuzion.chat.server.plugin.event;

import infuzion.chat.server.plugin.Plugin;

public interface IEventManager {
    void registerEvents(Class events);

    void registerEvents(IEventListener listener, Plugin plugin);

    void fireEvent(Event event);
}
