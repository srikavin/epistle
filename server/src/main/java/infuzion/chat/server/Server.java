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

package infuzion.chat.server;

import com.esotericsoftware.yamlbeans.YamlException;
import infuzion.chat.common.DataType;
import infuzion.chat.server.command.CommandManager;
import infuzion.chat.server.command.ICommandManager;
import infuzion.chat.server.event.EventManager;
import infuzion.chat.server.event.IEventManager;
import infuzion.chat.server.event.chat.MessageEvent;
import infuzion.chat.server.event.connection.JoinEvent;
import infuzion.chat.server.permission.IPermissionManager;
import infuzion.chat.server.permission.def.DefaultPermissionManager;
import infuzion.chat.server.plugin.loader.IPluginManager;
import infuzion.chat.server.plugin.loader.PluginManager;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Server implements Runnable, IServer {
    private final List<DataInputStream> clientInput = new ArrayList<>();
    private final List<Socket> clientSockets = new ArrayList<>();
    private final Map<IChatClient, Integer> heartbeat = new HashMap<>();
    private final IChatClient serverClient = new ServerChatClient();
    private final Scanner scanner;
    private final List<Runnable> toRun = new ArrayList<>();
    private final File permissionFile = new File("permissions.yml");
    private ServerSocket socket;
    private IChatRoomManager chatRoomManager;
    private IPluginManager pluginManager;
    private ICommandManager commandManager;
    private IEventManager eventManager;
    private IPermissionManager permissionManager;
    private int tps = 20;
    private long tpsTotal = 0;
    private long tpsCounter = 0;

    @SuppressWarnings("InfiniteLoopStatement")
    Server(int port) throws IOException {
        scanner = new Scanner(System.in);
        socket = new ServerSocket(port, 50, InetAddress.getByName("0.0.0.0"));
        System.out.println("Bound to port " + socket.getLocalPort() + " at " + socket.getInetAddress().toString());

        chatRoomManager = new ChatRoomManager();
        pluginManager = new PluginManager(this);
        eventManager = new EventManager(this);

        new Thread(() -> {
            while (true) {
                try {
                    Socket sock = socket.accept();
                    clientSockets.add(sock);
                    synchronized (clientInput) {
                        clientInput.add(new DataInputStream(sock.getInputStream()));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (Iterator<Map.Entry<IChatClient, Integer>> iterator = heartbeat.entrySet().iterator();
                     iterator.hasNext(); ) {
                    Map.Entry<IChatClient, Integer> e = iterator.next();
                    e.setValue(e.getValue() - 1000);
                    if (e.getValue() <= 0) {
                        iterator.remove();
                        chatRoomManager.removeClient(e.getKey());
                        System.out.println("Removed client: ");
                        System.out.println("UUID: " + e.getKey().getUuid());
                        System.out.println("Name: " + e.getKey().getName());
                        System.out.println();
                    }
                }
            }
        }, 10, 1000);

        reload();

        new Thread(() -> {
            while (true) {
                if (scanner.hasNext()) {
                    String input = scanner.nextLine();
                    String[] split = input.split(" ");
                    String[] args;
                    String command;
                    if (split.length > 1) {
                        command = split[0];
                        args = Arrays.copyOfRange(split, 1, split.length);
                    } else {
                        command = input;
                        args = new String[]{};
                    }
                    toRun.add(() -> commandManager.executeCommand(command, args, serverClient, eventManager));
                }
                try {
                    TimeUnit.MILLISECONDS.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public void run() {
        try {
            while (true) {
                long curTime = System.currentTimeMillis();
                Iterator<Runnable> iterator = toRun.iterator();
                while (iterator.hasNext()) {
                    Runnable r = iterator.next();
                    r.run();
                    iterator.remove();
                }

                synchronized (clientInput) {
                    for (int i = 0, clientInputSize = clientInput.size(); i < clientInputSize; i++) {
                        DataInputStream e = clientInput.get(i);
                        if (e.available() <= 0) {
                            continue;
                        }

                        byte messageType = e.readByte();
                        DataType mType = DataType.valueOf(messageType);
                        String message = e.readUTF();
                        byte end = e.readByte();

                        if (end != DataType.EndOfData.byteValue || mType == null) {
                            continue;
                        }

                        if (mType.equals(DataType.Heartbeat)) {
                            if (message.equals("heart")) {
                                IChatClient client = ChatClient.fromSocket(clientSockets.get(i));
                                client.sendData("beat", DataType.Heartbeat);
                                heartbeat.replace(client, 10000);
                            }
                        }
                        if (mType.equals(DataType.ClientHello)) {
                            IChatClient client = new ChatClient(message, UUID.nameUUIDFromBytes(message.getBytes()),
                                    clientSockets.get(i));
                            JoinEvent joinEvent = new JoinEvent(client);
                            eventManager.fireEvent(joinEvent);
                            if (joinEvent.isCanceled()) {
                                client.sendData("You were kicked.", DataType.Kick);
                                chatRoomManager.kickClient(client);
                                continue;
                            }
                            heartbeat.put(client, 10000);
                            chatRoomManager.addClient(client);

                            System.out.println("Client connected: ");
                            System.out.println("UUID: " + client.getUuid());
                            System.out.println("Name: " + client.getName());
                            System.out.println();

                        } else if (mType.equals(DataType.Message)) {
                            IChatClient client = ChatClient.fromSocket(clientSockets.get(i));
                            if (client == null) {
                                continue;
                            }
                            MessageEvent messageEvent = new MessageEvent(client, message);
                            eventManager.fireEvent(messageEvent);
                            if (messageEvent.isCanceled()) {
                                continue;
                            }
                            chatRoomManager.sendMessageAll(message, client, client.getChatRoom());
                            System.out.println(client.getPrefix() + message);
                        } else if (mType.equals(DataType.Command)) {
                            IChatClient sender = ChatClient.fromSocket(clientSockets.get(i));
                            String[] split = message.split(" ");
                            String command;
                            String[] args;
                            if (split.length > 1) {
                                command = split[0].replace("/", "");
                                args = Arrays.copyOfRange(split, 1, split.length);
                            } else {
                                command = message.replace("/", "");
                                args = new String[]{};
                            }
                            commandManager.executeCommand(command, args, sender, eventManager);
                        }
                    }
                }
                while (curTime + 50 > System.currentTimeMillis()) {
                    long toSleep = (curTime + 50) - System.currentTimeMillis();
                    tpsTotal += toSleep;
                    tpsCounter++;
                    tps = 1000 / Math.toIntExact(tpsTotal / tpsCounter);
                    TimeUnit.MILLISECONDS.sleep(toSleep);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        pluginManager.disable();
        try {
            File file = new File("plugins");
            //noinspection ResultOfMethodCallIgnored
            file.mkdir();
            pluginManager = new PluginManager(this);
            eventManager.reset();
            try {
                permissionManager = new DefaultPermissionManager(this,
                        new InputStreamReader(new FileInputStream(permissionFile)));
            } catch (YamlException e) {
                System.out.println("Errors in permissions.yml");
                e.printStackTrace();
                System.exit(0);
            }
            //Init this last
            commandManager = new CommandManager(this);
            pluginManager.addAllPlugins(file);
            pluginManager.enable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        pluginManager.disable();
        System.exit(0);
    }

    public IPluginManager getPluginManager() {
        return pluginManager;
    }

    public IEventManager getEventManager() {
        return eventManager;
    }

    public ICommandManager getCommandManager() {
        return commandManager;
    }

    @Override
    public IPermissionManager getPermissionManager() {
        return permissionManager;
    }

    public void setPermissionManager(IPermissionManager permissionManager) {
        this.permissionManager = permissionManager;
    }

    public int getTps() {
        return tps;
    }

    @Override
    public long getTotalTps() {
        return tpsTotal;
    }
}
