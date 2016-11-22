package infuzion.chat.server.permission;

import infuzion.chat.server.IServer;
import infuzion.chat.server.command.CommandManager;
import infuzion.chat.server.event.IEventManager;
import infuzion.chat.server.plugin.loader.IPluginManager;

/**
 * Created by Sri on 11/22/2016.
 */
public class FakeServer implements IServer {
    @Override
    public void reload() {
    }

    @Override
    public void stop() {

    }

    @Override
    public IPluginManager getPluginManager() {
        return null;
    }

    @Override
    public IEventManager getEventManager() {
        return new FakeEventManager();
    }

    @Override
    public CommandManager getCommandManager() {
        return null;
    }

    @Override
    public IPermissionManager getPermissionManager() {
        return null;
    }

    @Override
    public int getTps() {
        return 0;
    }

    @Override
    public long getTotalTps() {
        return 0;
    }
}
