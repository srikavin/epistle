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

package infuzion.chat.server.permission.def;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import infuzion.chat.server.IChatClient;
import infuzion.chat.server.IServer;
import infuzion.chat.server.command.Command;
import infuzion.chat.server.event.IEventListener;
import infuzion.chat.server.event.command.PreCommandEvent;
import infuzion.chat.server.event.connection.JoinEvent;
import infuzion.chat.server.event.reflection.EventHandler;
import infuzion.chat.server.permission.*;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DefaultPermissionManager implements IPermissionManager, IEventListener {
    private final List<IPermissionGroup> permissionGroups = new ArrayList<>();
    private final Map<Command, Permission> commandPermissionMap = new HashMap<>();

    public DefaultPermissionManager(IServer server, Map<String, Map<String, List<String>>> map) {
        //Group or UUID "Permission" or "groups" Permissions/Groups
        if (map == null) {
            return;
        }
        for (Map.Entry<String, Map<String, List<String>>> entry : map.entrySet()) {
            Map<String, List<String>> value = entry.getValue();
            String name = entry.getKey();

            boolean defaultGroup = false;

            DefaultPermissionGroup group = IPermissionGroup.fromName(name);
            if (group == null) {
                group = new DefaultPermissionGroup(name, new PermissionAttachment(), false);
            }

            PermissionAttachment attachment = new PermissionAttachment();

            if (value.containsKey("permissions")) {
                List<Permission> permissions = value.get("permissions").stream().map(Permission::new).collect(Collectors.toList());
                attachment = new PermissionAttachment(permissions);
            }
            if (value.containsKey("default")) {
                if (value.get("default").size() > 0) {
                    if (value.get("default").get(0).equalsIgnoreCase("true")) {
                        defaultGroup = true;
                    }
                }
            }

            if (value.containsKey("group")) {
                for (String groupName : value.get("group")) {
                    IPermissionGroup permissionGroup = IPermissionGroup.fromName(groupName);
                    if (permissionGroup == null) {
                        permissionGroup = new DefaultPermissionGroup(groupName, new PermissionAttachment(), false);
                    }
                    group.addGroup(permissionGroup);
                }
            }

            group.setDefault(defaultGroup);
            group.setPermissions(attachment);
            permissionGroups.add(group);
        }

    }


    public DefaultPermissionManager(IServer server, InputStreamReader permissionFileReader) throws YamlException {
        //noinspection unchecked
        this(server, (Map<String, Map<String, List<String>>>) new YamlReader(permissionFileReader).read());
    }

    @Override
    public boolean hasPermission(Permission permission, IChatClient chatClient) {
        if (chatClient.isConsole()) {
            return true;
        }
        if (permission.getType().equals(PermissionDefault.TRUE)) { // Needs to be changed for negated permissions
            return true;
        } else {
            PermissionAttachment pA = getPermissionAttachment(chatClient);
            if (pA != null && pA.containsPermission(permission)) {
                return true;
            }
        }
        return chatClient.getPermissionAttachment() != null
                && chatClient.getPermissionAttachment().containsPermission(permission);
    }

    @EventHandler
    public void onJoin(JoinEvent event) {
        event.getClient().setPermissionAttachment(getPermissionAttachment(event.getClient()));
    }

    @EventHandler
    public void onCommand(PreCommandEvent event) {
        IChatClient sender = event.getSender();
        if (sender.isConsole()) {
            return;
        }
        Command command = new Command(event.getCommand());

        if (commandPermissionMap.containsKey(command)) {
            Permission permission = commandPermissionMap.get(command);
            if (hasPermission(permission, sender)) {
                return;
            }
        }
        event.setCanceled(true);
        event.getSender().sendMessage(noPermissionMessage());
    }

    private PermissionAttachment getDefault() {
        PermissionAttachment temp = new PermissionAttachment();
        for (IPermissionGroup group : permissionGroups) {
            if (group.isDefault()) {
                temp.addPermissions(group.getCalculatedPermissions());
            }
        }
        return temp;
    }

    @Override
    public void registerPermission(Command command, Permission permission) {
        commandPermissionMap.put(command, permission);
    }

    @Override
    public PermissionAttachment getPermissionAttachment(IChatClient chatClient) {
        IPermissionGroup group = IPermissionGroup.fromName(chatClient.getUuid().toString());
        if (group != null) {
            return group.getCalculatedPermissions();
        }
        return getDefault();
    }
}
