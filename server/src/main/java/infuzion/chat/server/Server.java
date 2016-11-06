package infuzion.chat.server;

import infuzion.chat.common.DataType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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

    Server(int port) throws IOException {
        socket = new ServerSocket(port);
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
