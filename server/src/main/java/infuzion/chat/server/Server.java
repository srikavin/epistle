package infuzion.chat.server;

import infuzion.chat.common.DataType;
import infuzion.chat.server.command.CommandManager;
import infuzion.chat.server.plugin.event.EventManager;
import infuzion.chat.server.plugin.event.IEventManager;
import infuzion.chat.server.plugin.event.chat.MessageEvent;
import infuzion.chat.server.plugin.event.command.PreCommandEvent;
import infuzion.chat.server.plugin.event.connection.JoinEvent;
import infuzion.chat.server.plugin.loader.IPluginManager;
import infuzion.chat.server.plugin.loader.PluginManager;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Server implements Runnable, IServer {
    private final List<DataInputStream> clientInput = new ArrayList<>();
    private ServerSocket socket;
    private List<Socket> clientSockets = new ArrayList<>();
    private Map<IChatClient, Integer> heartbeat = new HashMap<>();
    private IChatRoomManager chatRoomManager;
    private IPluginManager pluginManager;
    private CommandManager commandManager;
    private IEventManager eventManager;
    private int tps = 20;
    private long tpsTotal = 0;
    private long tpsCounter = 0;

    @SuppressWarnings("InfiniteLoopStatement")
    Server(int port) throws IOException {
        socket = new ServerSocket(port, 50, InetAddress.getByName("0.0.0.0"));
        System.out.println("Binded to port " + socket.getLocalPort() + " at " + socket.getInetAddress().toString());

        chatRoomManager = new ChatRoomManager();
        pluginManager = new PluginManager(this);
        commandManager = new CommandManager(this);
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
                for (Iterator<Map.Entry<IChatClient, Integer>> iterator = heartbeat.entrySet().iterator(); iterator.hasNext(); ) {
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
    }

    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public void run() {
        try {
            while (true) {
                long curTime = System.currentTimeMillis();
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
                            IChatClient client = new ChatClient(message, UUID.randomUUID(), clientSockets.get(i));
                            JoinEvent joinEvent = new JoinEvent(client);
                            eventManager.fireEvent(joinEvent);
                            if (joinEvent.isCanceled()) {
                                client.sendData("You were kicked.", DataType.Kick);
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
                            PreCommandEvent event = new PreCommandEvent(command, args, sender);
                            eventManager.fireEvent(event);
                            if (event.isCanceled()) {
                                continue;
                            }
                            System.out.println(sender.getName() + " executed: " + message);
                            commandManager.executeCommand(command, args, sender);
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
            commandManager = new CommandManager(this);
            eventManager.reset();
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

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public int getTps() {
        return tps;
    }

    @Override
    public long getTotalTps() {
        return tpsTotal;
    }
}
