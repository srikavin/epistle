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
import infuzion.chat.server.permission.IPermissionManager;
import infuzion.chat.server.permission.Permission;
import infuzion.chat.server.permission.PermissionAttachment;
import infuzion.chat.server.permission.PermissionDefault;

import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DefaultPermissionManager implements IPermissionManager, IEventListener {
    private static Pattern uuidTest = Pattern.compile("" +
            "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
    private Map<Command, Permission> commandPermissionMap;
    private Map<String, PermissionAttachment> groupPermissionMap;
    private Map<UUID, List<String>> chatClientGroupMap;
    private Map<String, List<String>> groupInheritanceMap;
    private Map<UUID, PermissionAttachment> permissionMap;
    private PermissionAttachment defaultPerms;
    private String defaultGroup;

    public DefaultPermissionManager(IServer server, Map<String, Map<String, List<String>>> map) {
        this.permissionMap = new HashMap<>();
        this.commandPermissionMap = new HashMap<>();
        this.groupPermissionMap = new HashMap<>();
        this.chatClientGroupMap = new HashMap<>();
        this.groupInheritanceMap = new HashMap<>();
        defaultPerms = new PermissionAttachment();
        server.getEventManager().registerListener(this, null);
        if (map == null) {
            System.out.println("YOU ARE RUNNING WITHOUT ANY PERMISSIONS! THIS CAN BE DANGEROUS");
            System.out.println("YOU ARE RUNNING WITHOUT ANY PERMISSIONS! THIS CAN BE DANGEROUS");
            System.out.println("YOU ARE RUNNING WITHOUT ANY PERMISSIONS! THIS CAN BE DANGEROUS");
            System.out.println("YOU ARE RUNNING WITHOUT ANY PERMISSIONS! THIS CAN BE DANGEROUS");
            System.out.println("YOU ARE RUNNING WITHOUT ANY PERMISSIONS! THIS CAN BE DANGEROUS");
            return;
        }
        for (Map.Entry<String, Map<String, List<String>>> entry : map.entrySet()) {
            String groupName = entry.getKey();
            Map<String, List<String>> value = entry.getValue();
            List<Permission> permissions = null;
            PermissionAttachment attachment = null;

            List<String> groups = new ArrayList<>();

            if (value.containsKey("permissions")) {
                permissions = value.get("permissions").stream().map(Permission::new).collect(Collectors.toList());
                attachment = new PermissionAttachment(permissions);
            }
            if (value.containsKey("group")) {
                groups.addAll(value.get("group"));
            }
            if (permissions != null && permissions.size() != 0) {
                if (groupName.equals("*")) {
                    defaultPerms = attachment;
                } else if (uuidTest.matcher(groupName).matches()) {
                    permissionMap.put(UUID.fromString(entry.getKey()), attachment);
                    if (value.containsKey("group")) {
                        chatClientGroupMap.put(UUID.fromString(entry.getKey()), value.get("group"));
                    }
                } else {
                    groupPermissionMap.put(groupName, attachment);
                }
            }

            if (groups.size() != 0) {
                groupInheritanceMap.put(groupName, groups);
            }
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
//            if (permissionMap.get(chatClient.getUuid()) != null) {
//                return permissionMap.get(chatClient.getUuid()).containsPermission(permission);
//            } else {
            PermissionAttachment pA = getPermissionAttachment(chatClient);
            if (pA != null && pA.containsPermission(permission)) {
                return true;
            }
//            }
        }
        return false;
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
        PermissionAttachment toReturn = new PermissionAttachment();
        if (chatClient.getPermissionAttachment() != null) {
            toReturn = chatClient.getPermissionAttachment();
        } else if (permissionMap.containsKey(chatClient.getUuid())) {
            List<String> groups = chatClientGroupMap.getOrDefault(chatClient.getUuid(), new ArrayList<>());
            for (String group : groups) {
                toReturn.addPermissions(combineInheritedPermissions(group));
            }
            toReturn.addPermissions(permissionMap.get(chatClient.getUuid()));
        }

        chatClient.setPermissionAttachment(toReturn);
        return toReturn;
    }

    private PermissionAttachment combineInheritedPermissions(String group) {
        PermissionAttachment toReturn =
                (groupPermissionMap.containsKey(group)) ? groupPermissionMap.get(group) : new PermissionAttachment();
        if (groupInheritanceMap.containsKey(group)) {
            for (String inheritedGroup : groupInheritanceMap.get(group)) {
                if (groupInheritanceMap.containsKey(inheritedGroup)) {
                    PermissionAttachment temp = combineInheritedPermissions(inheritedGroup);
                    temp.addPermissions(groupPermissionMap.get(inheritedGroup));
                    toReturn.addPermissions(temp);
                } else {
                    toReturn.addPermissions(groupPermissionMap.get(inheritedGroup));
                }
            }
        }
        return toReturn;
    }
}
