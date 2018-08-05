/*
 * Copyright 2018 Srikavin Ramkumar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.infuzion.chat.server.plugin.loader;

import com.esotericsoftware.yamlbeans.YamlReader;
import me.infuzion.chat.server.api.plugin.loader.DescriptionFile;

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
