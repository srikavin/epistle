package infuzion.chat.server.plugin;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.jar.JarFile;

public class PluginManager {
    private static List<Plugin> plugins = new ArrayList<>();

    public void addPlugin(File file) throws Exception {
        PluginDescriptionFile descriptionFile = new PluginDescriptionFile(new JarFile(file));
        ClassLoader loader = URLClassLoader.newInstance(new URL[]{file.toURI().toURL()});
        Field f = ClassLoader.class.getDeclaredField("classes");
        f.setAccessible(true);

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Vector<Class> classes = (Vector<Class>) f.get(classLoader);
        for (Class e : classes) {
            System.out.println(e.getName());
        }
        System.out.println(descriptionFile.getMainClass());
        Plugin plugin = (Plugin) loader.loadClass(descriptionFile.getMainClass()).newInstance();
        plugins.add(plugin);
    }

    public void addAllPlugins(File file) {
        if (file == null || !file.isDirectory() || file.listFiles() == null) {
            return;
        }
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
