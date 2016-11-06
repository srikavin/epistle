package infuzion.chat.server;

import infuzion.chat.common.DataType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Server implements Runnable {
    private ServerSocket socket;
    private List<ChatRoom> chatRooms = new ArrayList<>();
    private List<ChatClient> clients = new ArrayList<>();
    private List<Socket> clientSockets = new ArrayList<>();
    private List<DataOutputStream> clientOutput = new ArrayList<>();
    private final List<DataInputStream> clientInput = new ArrayList<>();
    private ChatRoomManager chatRoomManager;

    Server(int port) throws IOException {
        socket = new ServerSocket(port);
        chatRoomManager = new ChatRoomManager();
        chatRooms.add(new ChatRoom("default"));
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
                int counter = 0;
                synchronized (clientInput) {
                    for (DataInputStream e : clientInput) {
                        if (e.available() <= 0) {
//                        System.out.println("skipping");
                            continue;
                        }
                        byte messageType = e.readByte();
                        String message = e.readUTF();
                        byte end = e.readByte();
                        System.out.println(message);

                        if (end != DataType.EndOfData.byteValue) {
                            continue;
                        }
                        DataType mType = DataType.valueOf(messageType);
                        if (mType == null) {
                            continue;
                        }

                        if (mType.equals(DataType.ClientHello)) {
                            ChatClient client = new ChatClient(message, UUID.randomUUID(), clientSockets.get(counter));
                            clients.add(client);
                            client.sendData(client.getUuid().toString(), DataType.UUIDAssign);
                        } else if (mType.equals(DataType.Message)) {
                            ChatClient client = ChatClient.fromSocket(clientSockets.get(counter));
                            if (client == null) {
                                continue;
                            }
                            client.sendMessage(message);
                            System.out.println(message);
                        }

                        counter++;
                    }
                }
                Thread.sleep(250);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
