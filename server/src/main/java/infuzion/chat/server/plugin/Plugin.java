package infuzion.chat.server.plugin;

import infuzion.chat.server.IServer;
import infuzion.chat.server.command.CommandManager;
import infuzion.chat.server.plugin.event.IEventManager;

public abstract class Plugin {
    private boolean isEnabled;
    private DescriptionFile descriptionFile;
    private CommandManager commandManager;
    private IEventManager IEventManager;

    public Plugin() {
    }

    public final void init(DescriptionFile descriptionFile, IServer server) {
        this.descriptionFile = descriptionFile;
        this.commandManager = server.getCommandManager();
        this.IEventManager = server.getEventManager();
    }

    public final IEventManager getEventManager() {
        return IEventManager;
    }

    public abstract void onEnable();

    public abstract void onDisable();

    public void onLoad() {
    }

    public boolean onCommand() {
        return false;
    }

    protected CommandManager getCommandManager() {
        return commandManager;
    }

    public final boolean isEnabled() {
        return isEnabled;
    }

    public final void setEnabled(boolean enabled) {
        this.isEnabled = enabled;
    }

    public DescriptionFile getDescriptionFile() {
        return descriptionFile;
    }
}
