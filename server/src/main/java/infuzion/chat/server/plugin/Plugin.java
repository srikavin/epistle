package infuzion.chat.server.plugin;

import infuzion.chat.server.command.CommandManager;

public abstract class Plugin {
    private boolean isEnabled;
    private PluginDescriptionFile descriptionFile;

    protected Plugin(Class<?> mainClass, PluginDescriptionFile descriptionFile) {
        this.descriptionFile = descriptionFile;
    }

    public Plugin() {
    }

    public abstract void onEnable();

    public abstract void onDisable();

    public void onLoad() {
    }

    public boolean onCommand() {
        return false;
    }

    public CommandManager getCommandManager() {
        return new CommandManager();
    }

    public final boolean isEnabled() {
        return isEnabled;
    }

    public final void setEnabled(boolean enabled) {
        this.isEnabled = enabled;
    }

    public PluginDescriptionFile getDescriptionFile() {
        return descriptionFile;
    }
}
