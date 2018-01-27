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

package me.infuzion.chat.server.api.plugin;

import me.infuzion.chat.server.api.IServer;
import me.infuzion.chat.server.api.command.ICommandManager;
import me.infuzion.chat.server.api.event.IEventManager;
import me.infuzion.chat.server.api.permission.IPermissionManager;
import me.infuzion.chat.server.api.plugin.loader.DescriptionFile;

public abstract class BasePlugin implements Plugin {
    private boolean isEnabled;
    private DescriptionFile descriptionFile;
    private ICommandManager commandManager;
    private IEventManager eventManager;
    private IServer server;
    private IPermissionManager permissionManager;

    public BasePlugin() {
    }

    public final void init(DescriptionFile descriptionFile, IServer server) {
        this.descriptionFile = descriptionFile;
        this.commandManager = server.getCommandManager();
        this.eventManager = server.getEventManager();
        this.permissionManager = server.getPermissionManager();
        this.server = server;
    }

    @Override
    public final IEventManager getEventManager() {
        return eventManager;
    }

    @Override
    public void onLoad() {
    }

    @SuppressWarnings("SameReturnValue")
    public boolean onCommand() {
        return false;
    }

    protected ICommandManager getCommandManager() {
        return commandManager;
    }

    @Override
    public final boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public final void setEnabled(boolean enabled) {
        this.isEnabled = enabled;
    }

    @Override
    public DescriptionFile getDescriptionFile() {
        return descriptionFile;
    }

    @Override
    public IServer getServer() {
        return server;
    }

    @Override
    public IPermissionManager getPermissionManager() {
        return permissionManager;
    }
}
