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

package infuzion.chat.server.plugin;

import infuzion.chat.server.IServer;
import infuzion.chat.server.command.CommandManager;
import infuzion.chat.server.event.IEventManager;

public abstract class Plugin {
    private boolean isEnabled;
    private DescriptionFile descriptionFile;
    private CommandManager commandManager;
    private IEventManager IEventManager;

    public Plugin() {
    }

    public final void init(DescriptionFile descriptionFile, IServer server) {
        this.descriptionFile = descriptionFile;
        this.commandManager = server.getCommandManager();
        this.IEventManager = server.getEventManager();
    }

    public final IEventManager getEventManager() {
        return IEventManager;
    }

    public abstract void onEnable();

    public abstract void onDisable();

    public void onLoad() {
    }

    public boolean onCommand() {
        return false;
    }

    protected CommandManager getCommandManager() {
        return commandManager;
    }

    public final boolean isEnabled() {
        return isEnabled;
    }

    public final void setEnabled(boolean enabled) {
        this.isEnabled = enabled;
    }

    public DescriptionFile getDescriptionFile() {
        return descriptionFile;
    }
}
