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

import me.infuzion.chat.server.mock.FakeServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PluginManagerTest {
    @Test
    public void load() throws Exception {
        createAndAddAllPlugins();
        assertEquals("1", System.getProperty("loaded"));
    }

    @Test
    public void enable() throws Exception {
        PluginManager pl = createAndAddAllPlugins();
        pl.enable();
        assertEquals("1", System.getProperty("enabled"));
    }

    @Test
    public void disable() throws Exception {
        PluginManager pl = createAndAddAllPlugins();
        pl.disable();
        assertEquals("1", System.getProperty("disabled"));
    }

    @Test
    public void addPlugin() {
    }

    private PluginManager createAndAddAllPlugins() throws Exception {
        PluginManager pl = new PluginManager(new FakeServer());
        pl.addAllPlugins(new File(getClass().getResource("/plugins" + File.separator).toURI()));
        return pl;
    }

    @BeforeEach
    public void reset() {
        System.setProperty("enabled", "0");
        System.setProperty("loaded", "0");
        System.setProperty("disabled", "0");
    }

    @Test
    public void addAllPlugins() throws Exception {
        PluginManager pl = createAndAddAllPlugins();
        assertEquals(1, pl.getPlugins().size());
    }

    @Test
    public void getPlugins() throws Exception {
        assertEquals(0, new PluginManager(new FakeServer()).getPlugins().size());
        assertEquals(1, createAndAddAllPlugins().getPlugins().size());
    }

}