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

import infuzion.chat.server.IChatClient;
import infuzion.chat.server.command.Command;
import infuzion.chat.server.plugin.Plugin;
import infuzion.chat.server.plugin.command.ICommandExecutor;
import infuzion.chat.server.plugin.event.IEventListener;
import infuzion.chat.server.plugin.event.chat.MessageEvent;
import infuzion.chat.server.plugin.event.command.PreCommandEvent;
import infuzion.chat.server.plugin.event.connection.JoinEvent;
import infuzion.chat.server.plugin.event.reflection.EventHandler;

public class test extends Plugin implements ICommandExecutor, IEventListener {

    @Override
    public void onEnable() {
        System.out.println("For testing: onEnable()");
        getCommandManager().registerCommand(new Command("test"), this);
        getEventManager().registerEvents(this, this);
    }

    @Override
    public void onLoad() {
        System.out.println("For testing: onLoad()");
    }

    @Override
    public void onDisable() {
        System.out.println("For testing: onDisable()");
    }


    @Override
    public void onCommand(String commandName, String[] args, IChatClient client) {
        client.sendMessage("Your inputted arguments are displayed below: ");
        StringBuilder toSend = new StringBuilder();
        for (String e : args) {
            toSend.append(e).append("\n");
        }
        client.sendMessage(toSend.toString());
    }

    @Override
    public String[] getHelp() {
        return new String[0];
    }

    @EventHandler
    public void onChat(MessageEvent event) {
        if (event.getMessage().contains("cancel")) {
            event.setCanceled(true);
            event.getSender().sendMessage("Canceled!");
        }
    }

    @EventHandler
    public void onCommand(PreCommandEvent event) {
        event.setCanceled(false);
        event.getSender().sendMessage("You issued the command " + event.getCommand());
    }

    @EventHandler
    public void onJoin(JoinEvent event) {
        event.setCanceled(true);
    }
}
