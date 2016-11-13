package infuzion.chat.server.plugin.loader;

import infuzion.chat.server.Server;
import infuzion.chat.server.plugin.Plugin;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;

public class PluginManager implements IPluginManager {
    private List<Plugin> plugins = new ArrayList<>();
    private Server server;

    public PluginManager(Server server) {
        this.server = server;
    }

    public void addPlugin(File file) throws Exception {
        PluginDescriptionFile descriptionFile = new PluginDescriptionFile(new JarFile(file));
        ClassLoader loader = URLClassLoader.newInstance(new URL[]{file.toURI().toURL()});

        Plugin plugin = (Plugin) loader.loadClass(descriptionFile.getMainClass()).newInstance();
        plugin.init(descriptionFile, server);
        plugin.onLoad();
        System.out.println("Successfully loaded " + descriptionFile.getName() + " v" + descriptionFile.getVersion());
        plugins.add(plugin);
    }

    public void addAllPlugins(File file) {
        if (file == null || !file.isDirectory() || file.listFiles() == null) {
            return;
        }
        //noinspection ConstantConditions
        for (File f : file.listFiles()) {
            try {
                addPlugin(f);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public List<Plugin> getPlugins() {
        return plugins;
    }

    public void load() {
        plugins.forEach(Plugin::onLoad);
    }

    public void enable() {
        plugins.forEach(Plugin::onEnable);
    }

    public void disable() {
        plugins.forEach(Plugin::onDisable);
    }
}
