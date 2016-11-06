package infuzion.chat.client;

import infuzion.chat.common.DataType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.UUID;

public class Client implements Runnable {
    private Scanner scanner = new Scanner(System.in);
    private Socket sock;
    private DataInputStream input;
    private DataOutputStream output;

    public Client(String ip, int port) throws IOException {
        sock = new Socket(ip, port);
        input = new DataInputStream(sock.getInputStream());
        output = new DataOutputStream(sock.getOutputStream());
        output.writeByte(DataType.ClientHello.byteValue);
        output.writeUTF(UUID.randomUUID().toString());
        output.writeByte(DataType.EndOfData.byteValue);
        new Thread(() -> {
            try {
                while (true) {
                    if (input.available() <= 0) {
                        continue;
                    }
                    byte messageType = input.readByte();
                    String message = input.readUTF();
                    byte end = input.readByte();
                    if (end != DataType.EndOfData.byteValue) {
                        continue;
                    }
                    DataType mType = DataType.valueOf(messageType);
                    if (mType == null) {
                        continue;
                    }

                    if (mType.equals(DataType.Message)) {
                        System.out.println(message);
                    }
                    Thread.sleep(250);
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
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
    public void run() {
        while (true) {
            try {
                if (scanner.hasNext()) {
                    output.writeByte(DataType.Message.byteValue);
                    output.writeUTF(scanner.nextLine());
                    output.writeByte(DataType.EndOfData.byteValue);
                }
                Thread.sleep(250);
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}
