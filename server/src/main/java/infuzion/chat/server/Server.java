package infuzion.chat.server;

import infuzion.chat.common.DataType;
import infuzion.chat.server.command.CommandManager;
import infuzion.chat.server.plugin.PluginManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server implements Runnable {
    private final List<DataInputStream> clientInput = new ArrayList<>();
    private ServerSocket socket;
    private List<ChatClient> clients = new ArrayList<>();
    private List<Socket> clientSockets = new ArrayList<>();
    private List<DataOutputStream> clientOutput = new ArrayList<>();
    private Map<ChatClient, Integer> heartbeat = new HashMap<>();
    private ChatRoomManager chatRoomManager;
    private PluginManager pluginManager = new PluginManager();
    private CommandManager commandManager = new CommandManager();

    @SuppressWarnings("InfiniteLoopStatement")
    Server(int port) throws IOException {
        System.out.println();
        socket = new ServerSocket(port, 50, InetAddress.getByName("0.0.0.0"));
        System.out.println("Binded to port " + socket.getLocalPort() + " at " + socket.getInetAddress().toString());
        chatRoomManager = new ChatRoomManager();
        new Thread(() -> {
            while(true) {
                try {
                    Socket sock = socket.accept();
                    clientSockets.add(sock);
                    clientOutput.add(new DataOutputStream(sock.getOutputStream()));
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
                for (Map.Entry<ChatClient, Integer> e : heartbeat.entrySet()) {
                    e.setValue(e.getValue() - 1000);
                    if (e.getValue() <= 0) {
                        heartbeat.remove(e.getKey());
                        chatRoomManager.removeClient(e.getKey());
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
            pluginManager.addAllPlugins(new File("plugins"));
            pluginManager.load();
            pluginManager.enable();
            pluginManager.disable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
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
                                ChatClient client = ChatClient.fromSocket(clientSockets.get(i));
                                client.sendData("beat", DataType.Heartbeat);
                                heartbeat.replace(client, 10000);
                            }
                        }
                        if (mType.equals(DataType.ClientHello)) {
                            ChatClient client = new ChatClient(message, UUID.randomUUID(), clientSockets.get(i));
                            heartbeat.put(client, 10000);
                            clients.add(client);
                            client.sendData(client.getUuid().toString(), DataType.UUIDAssign);
                            chatRoomManager.addClient(client);

                            System.out.println("Client connected: ");
                            System.out.println("UUID: " + client.getUuid());
                            System.out.println("Name: " + client.getName());
                            System.out.println();

                        } else if (mType.equals(DataType.Message)) {
                            ChatClient client = ChatClient.fromSocket(clientSockets.get(i));
                            if (client == null) {
                                continue;
                            }
                            chatRoomManager.sendMessageAll(message, client, client.getChatRoom());
                            System.out.println(client.getPrefix() + message);
                        } else if (mType.equals(DataType.Command)) {
                            System.out.println(message);
                            String[] split = message.split(" ");
                            if (split.length > 1) {
                                String command = split[0].replace("/", "");
                                String[] args = Arrays.copyOfRange(split, 1, command.length());
                                commandManager.executeCommand(command, args, ChatClient.fromSocket(clientSockets.get(i)));
                            } else {
                                commandManager.executeCommand(message.replace("/", ""), new String[]{}, ChatClient.fromSocket(clientSockets.get(i)));
                            }
                        }

                    }
                }
                Thread.sleep(250);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
