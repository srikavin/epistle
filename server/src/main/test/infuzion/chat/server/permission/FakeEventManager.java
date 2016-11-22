package infuzion.chat.server.permission;

import infuzion.chat.server.event.Event;
import infuzion.chat.server.event.IEventListener;
import infuzion.chat.server.event.IEventManager;
import infuzion.chat.server.plugin.Plugin;

/**
 * Created by Sri on 11/22/2016.
 */
public class FakeEventManager implements IEventManager {
    @Override
    public void registerEvent(Class<? extends Event> events) {

    }

    @Override
    public void registerListener(IEventListener listener, Plugin plugin) {

    }

    @Override
    public void fireEvent(Event event) {

    }

    @Override
    public void reset() {

    }
}
