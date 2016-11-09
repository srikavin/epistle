package infuzion.chat.server.plugin.loader;

import infuzion.chat.server.plugin.Plugin;

import java.io.File;
import java.util.List;

public interface IPluginManager {
    void addPlugin(File pluginFile) throws Exception;

    void addAllPlugins(File directory);

    List<Plugin> getPlugins();

    void load();

    void enable();

    void disable();
}
