package infuzion.chat.server.plugin.loader;

import com.esotericsoftware.yamlbeans.YamlReader;
import infuzion.chat.server.plugin.DescriptionFile;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PluginDescriptionFile implements DescriptionFile {
    private String name;
    private String version;
    private String mainClass;

    public PluginDescriptionFile(String name, String version, String mainClass) {
        this.name = name;
        this.version = version;
        this.mainClass = mainClass;
    }

    public PluginDescriptionFile(JarFile jarFile) throws Exception {
        JarEntry entry = jarFile.getJarEntry("plugin.yml");
        if (entry == null) {
            throw new Exception("No plugin.yml found!");
        }
        Reader reader = new InputStreamReader(jarFile.getInputStream(entry));
        YamlReader ymlReader = new YamlReader(reader);
        Object object = ymlReader.read();
        Map map = (Map) object;

        name = (String) map.get("name");
        version = (String) map.get("version");
        mainClass = (String) map.get("main");
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public String getMainClass() {
        return mainClass;
    }
}
