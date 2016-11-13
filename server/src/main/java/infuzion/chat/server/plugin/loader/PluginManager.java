/*
 *  Copyright 2016 Infuzion
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

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
