package infuzion.chat.server;

import infuzion.chat.server.command.CommandManager;
import infuzion.chat.server.plugin.event.IEventManager;
import infuzion.chat.server.plugin.loader.IPluginManager;

public interface IServer {
    void reload();

    void stop();

    IPluginManager getPluginManager();

    IEventManager getEventManager();

    CommandManager getCommandManager();

}
