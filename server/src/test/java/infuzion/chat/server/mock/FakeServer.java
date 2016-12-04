/*
 *
 *  *  Copyright 2016 Infuzion
 *  *
 *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *    you may not use this file except in compliance with the License.
 *  *    You may obtain a copy of the License at
 *  *
 *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *    Unless required by applicable law or agreed to in writing, software
 *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *    See the License for the specific language governing permissions and
 *  *    limitations under the License.
 *
 */

package infuzion.chat.server.mock;

import infuzion.chat.server.IServer;
import infuzion.chat.server.command.ICommandManager;
import infuzion.chat.server.event.IEventManager;
import infuzion.chat.server.permission.IPermissionManager;
import infuzion.chat.server.plugin.loader.IPluginManager;


public class FakeServer implements IServer {
    private int tps = -1;
    private long totalTPS = -1;

    @Override
    public void reload() {
    }

    @Override
    public void stop() {

    }

    public void setTps(int tps, long totalTPS) {
        this.tps = tps;
        this.totalTPS = totalTPS;
    }

    @Override
    public IPluginManager getPluginManager() {
        return null;
    }

    @Override
    public IEventManager getEventManager() {
        return new FakeEventManager();
    }

    @Override
    public ICommandManager getCommandManager() {
        return new FakeCommandManager();
    }

    @Override
    public IPermissionManager getPermissionManager() {
        return new FakePermissionManager();
    }

    @Override
    public void setPermissionManager(IPermissionManager permissionManager) {

    }

    @Override
    public int getTps() {
        return tps;
    }

    @Override
    public long getTotalTps() {
        return totalTPS;
    }
}
