package infuzion.chat.server;

import infuzion.chat.common.DataType;
import infuzion.chat.server.command.CommandManager;
import infuzion.chat.server.plugin.event.EventManager;
import infuzion.chat.server.plugin.event.IEventManager;
import infuzion.chat.server.plugin.event.chat.MessageEvent;
import infuzion.chat.server.plugin.event.command.PreCommandEvent;
import infuzion.chat.server.plugin.loader.IPluginManager;
import infuzion.chat.server.plugin.loader.PluginManager;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server implements Runnable, IServer {
    private final List<DataInputStream> clientInput = new ArrayList<>();
    private ServerSocket socket;
    private List<Socket> clientSockets = new ArrayList<>();
    private Map<IChatClient, Integer> heartbeat = new HashMap<>();
    private IChatRoomManager IChatRoomManager;
    private IPluginManager IPluginManager = new PluginManager(this);
    private CommandManager commandManager = new CommandManager(this);
    private IEventManager IEventManager = new EventManager(this);

    @SuppressWarnings("InfiniteLoopStatement")
    Server(int port) throws IOException {
        System.out.println();
        socket = new ServerSocket(port, 50, InetAddress.getByName("0.0.0.0"));
        System.out.println("Binded to port " + socket.getLocalPort() + " at " + socket.getInetAddress().toString());
        IChatRoomManager = new ChatRoomManager();
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
                        IChatRoomManager.removeClient(e.getKey());
                        System.out.println("Removed client: ");
                        System.out.println("UUID: " + e.getKey().getUuid());
                        System.out.println("Name: " + e.getKey().getName());
                        System.out.println();
                    }
                }
            }
        }, 10, 1000);

        try {
            System.out.println(new File(".").getAbsoluteFile());
            IPluginManager.addAllPlugins(new File("plugins"));
            IPluginManager.enable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public void run() {
        try {
            while (true) {
                synchronized (clientInput) {
                    for (int i = 0, clientInputSize = clientInput.size(); i < clientInputSize; i++) {
                        DataInputStream e = clientInput.get(i);
                        if (e.available() <= 0) {
                            continue;
                        }
                        byte messageType = e.readByte();
                        String message = e.readUTF();
                        byte end = e.readByte();

                        if (end != DataType.EndOfData.byteValue) {
                            continue;
                        }
                        DataType mType = DataType.valueOf(messageType);
                        if (mType == null) {
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
                            heartbeat.put(client, 10000);
                            client.sendData(client.getUuid().toString(), DataType.UUIDAssign);
                            IChatRoomManager.addClient(client);

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
                            IEventManager.fireEvent(messageEvent);
                            if (messageEvent.isCanceled()) {
                                continue;
                            }
                            IChatRoomManager.sendMessageAll(message, client, client.getChatRoom());
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
                            IEventManager.fireEvent(event);
                            if (event.isCanceled()) {
                                continue;
                            }
                            System.out.println(sender.getName() + " executed: " + message);
                            commandManager.executeCommand(command, args, sender);
                        }
                    }

                }
                Thread.sleep(250);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        IPluginManager.disable();
        IPluginManager.addAllPlugins(new File("plugins"));
        IPluginManager.load();
        IPluginManager.enable();
    }

    public void stop() {
        IPluginManager.disable();
        System.exit(0);
    }

    public IPluginManager getPluginManager() {
        return IPluginManager;
    }

    public IEventManager getEventManager() {
        return IEventManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }
}
