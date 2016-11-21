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

package infuzion.chat.server.permission.def;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import infuzion.chat.server.IChatClient;
import infuzion.chat.server.IServer;
import infuzion.chat.server.command.Command;
import infuzion.chat.server.permission.IPermissionManager;
import infuzion.chat.server.permission.Permission;
import infuzion.chat.server.permission.PermissionAttachment;
import infuzion.chat.server.permission.PermissionDefault;
import infuzion.chat.server.plugin.event.IEventListener;
import infuzion.chat.server.plugin.event.command.PreCommandEvent;
import infuzion.chat.server.plugin.event.connection.JoinEvent;
import infuzion.chat.server.plugin.event.reflection.EventHandler;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class DefaultPermissionManager implements IPermissionManager, IEventListener {
    private Map<UUID, PermissionAttachment> permissionMap;
    private Map<Command, Permission> commandPermissionMap;
    private File permissionFile;
    private IServer server;

    public DefaultPermissionManager(IServer server) {
        this.server = server;
        this.permissionMap = new HashMap<>();
        this.commandPermissionMap = new HashMap<>();
        permissionFile = new File("permissions.yml");
        server.getEventManager().registerEvents(this, null);
        try {
            permissionFile.createNewFile();
            Reader reader = new InputStreamReader(new FileInputStream(permissionFile));

            YamlReader ymlReader = new YamlReader(reader);
            Object object = ymlReader.read();
            @SuppressWarnings("unchecked")
            Map<String, List<String>> map = (Map<String, List<String>>) object;
            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                List<Permission> permissions = entry.getValue().stream().map(Permission::new).collect(Collectors.toList());
                PermissionAttachment attachment = new PermissionAttachment(permissions);
                permissionMap.put(UUID.fromString(entry.getKey()), attachment);
            }
        } catch (FileNotFoundException | YamlException e) {
            e.printStackTrace();
            System.out.println("Invalid permissions.yml file!");
            System.exit(-1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean hasPermission(Permission permission, IChatClient chatClient) {
        if (permission.getType().equals(PermissionDefault.TRUE)) { // Needs to be changed for negated permissions
            return true;
        } else {
            if (permissionMap.get(chatClient.getUuid()) != null) {
                return permissionMap.get(chatClient.getUuid()).containsPermission(permission);
            }
        }
        return false;
    }

    @EventHandler
    public void onJoin(JoinEvent event) {
        event.getClient().setPermissionAttachment(getPermissionAttachment(event.getClient()));
    }

    @EventHandler
    public void onCommand(PreCommandEvent event) {
        if (event.getSender().isConsole()) {
            return;
        }
        if (commandPermissionMap.containsKey(new Command(event.getCommand()))) {
            Permission permission = commandPermissionMap.get(new Command(event.getCommand()));
            if (hasPermission(permission, event.getSender())) {
                return;
            }
        }
        event.setCanceled(true);
        event.getSender().sendMessage(noPermissionMessage());
    }

    @Override
    public PermissionAttachment getPermissions(IChatClient client) {
        return permissionMap.get(client.getUuid());
    }

    @Override
    public void registerPermission(Command command, Permission permission) {
        commandPermissionMap.put(command, permission);
    }

    @Override
    public PermissionAttachment getPermissionAttachment(IChatClient chatClient) {
        if (chatClient.getPermissionAttachment() != null) {
            return chatClient.getPermissionAttachment();
        }
        if (permissionMap.containsKey(chatClient.getUuid())) {
            return permissionMap.get(chatClient.getUuid());
        }
        return new PermissionAttachment();
    }
}
