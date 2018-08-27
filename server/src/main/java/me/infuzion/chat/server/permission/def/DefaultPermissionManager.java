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

package me.infuzion.chat.server.permission.def;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import me.infuzion.chat.server.api.IChatClient;
import me.infuzion.chat.server.api.Server;
import me.infuzion.chat.server.api.command.Command;
import me.infuzion.chat.server.api.command.DefaultCommand;
import me.infuzion.chat.server.api.event.IEventListener;
import me.infuzion.chat.server.api.event.command.PreCommandEvent;
import me.infuzion.chat.server.api.event.connection.JoinEvent;
import me.infuzion.chat.server.api.event.reflection.EventHandler;
import me.infuzion.chat.server.api.permission.*;
import me.infuzion.chat.server.permission.DefaultPermissionAttachment;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DefaultPermissionManager implements IPermissionManager, IEventListener {
    private final List<IPermissionGroup> permissionGroups = new ArrayList<>();
    private final Map<Command, Permission> commandPermissionMap = new HashMap<>();

    public DefaultPermissionManager(Server server, Map<String, Map<String, List<String>>> map) {
        //Group or UUID "Permission" or "groups" Permissions/Groups
        if (map == null) {
            return;
        }
        for (Map.Entry<String, Map<String, List<String>>> entry : map.entrySet()) {
            Map<String, List<String>> value = entry.getValue();
            String name = entry.getKey();

            boolean defaultGroup = false;

            IPermissionGroup group = IPermissionGroup.fromName(name);
            if (group == null) {
                group = new DefaultPermissionGroup(name, new DefaultPermissionAttachment(), false);
            }

            PermissionAttachment attachment = new DefaultPermissionAttachment();

            if (value.containsKey("permissions")) {
                List<Permission> permissions = value.get("permissions").stream().map(DefaultPermission::new).collect(Collectors.toList());
                attachment = new DefaultPermissionAttachment(permissions);
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
                        permissionGroup = new DefaultPermissionGroup(groupName, new DefaultPermissionAttachment(), false);
                    }
                    group.addGroup(permissionGroup);
                }
            }

            group.setDefault(defaultGroup);
            group.setPermissions(attachment);
            permissionGroups.add(group);
        }

    }

    public DefaultPermissionManager(Server server, InputStreamReader permissionFileReader) throws YamlException {
        //noinspection unchecked
        this(server, (Map<String, Map<String, List<String>>>) new YamlReader(permissionFileReader).read());
    }

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
        Command command = new DefaultCommand(event.getCommand());

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
        PermissionAttachment temp = new DefaultPermissionAttachment();
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
    public boolean hasPermission(String permission, IChatClient chatClient) {
        return hasPermission(new DefaultPermission(permission), chatClient);
    }

    @Override
    public void registerPermission(String command, String permission) {
        registerPermission(new DefaultCommand(command), new DefaultPermission(permission));
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
