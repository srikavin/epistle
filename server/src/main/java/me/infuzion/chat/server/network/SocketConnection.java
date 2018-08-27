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

package me.infuzion.chat.server.network;

import infuzion.chat.common.DataType;
import infuzion.chat.common.network.packet.NetworkPacket;
import me.infuzion.chat.server.api.network.ClientConnection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class SocketConnection implements ClientConnection {
    public static final int MAX_MESSAGE_SIZE = 8192;
    private final Socket socket;
    private final DataOutputStream outStream;
    private final DataInputStream inStream;

    public SocketConnection(Socket socket) throws IOException {
        this.socket = socket;
        socket.setSoTimeout(5 * 1000);
        this.outStream = new DataOutputStream(socket.getOutputStream());
        this.inStream = new DataInputStream(socket.getInputStream());
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public int available() throws IOException {
        return inStream.available();
    }

    @Override
    public void close() throws IOException {
        socket.shutdownInput();
        socket.shutdownOutput();
        socket.close();
    }

    @Override
    public void write(byte[] bytes) throws IOException {
        outStream.write(bytes);
    }

    @Override
    public ByteBuffer read(int amount) throws IOException {
        byte[] buffer = new byte[amount];
        int bytesRead = inStream.read(buffer);
        return ByteBuffer.wrap(buffer, 0, bytesRead);
    }

    private void read(byte[] message) throws IOException {
        for (int i = 0; i < message.length; i++) {
            message[i] = inStream.readByte();
        }
    }

    @Override
    public ByteBuffer readMessage() throws IOException {
        if (available() < 1) {
            throw new RuntimeException("Invalid amount of data written into socket");
        }

        short messageType = inStream.readShort();
        int length = inStream.readInt();

        if (length > MAX_MESSAGE_SIZE) {
            throw new RuntimeException("Packet sent exceeds maximum allowed packet size");
        }

        byte[] message = new byte[length];

        read(message);

        byte end = inStream.readByte();
        if (end != DataType.EndOfData.byteValue) {
            System.out.println(end);
            throw new RuntimeException("Packet received had invalid DataEnd value");
        }

        ByteBuffer buffer = ByteBuffer.allocate(length + 21);
        buffer.putShort(messageType);
        buffer.put(message);
        buffer.rewind();
        buffer.order(ByteOrder.BIG_ENDIAN);
        return buffer;
    }

    @Override
    public void sendMessage(DataType header, byte[] message) throws IOException {
        outStream.writeByte(header.byteValue);
        outStream.writeInt(message.length);
        outStream.write(message);
        outStream.writeByte(DataType.EndOfData.byteValue);
    }

    @Override
    public void sendMessage(DataType header, String message) throws IOException {
        outStream.writeByte(header.byteValue);
        outStream.writeUTF(message);
        outStream.writeByte(DataType.EndOfData.byteValue);
    }

    @Override
    public void sendPacket(NetworkPacket packet) throws IOException {
        byte[] data = packet.asBytes();

        // [ Signature ] [  Length  ] [  data  ]  [ End of Packet ]
        //    4 bytes      2 bytes     x bytes         1 byte
        byte[] ret = new byte[data.length + 7];
        ByteBuffer buffer = ByteBuffer.wrap(ret);

        buffer.putShort(packet.getSignature());
        buffer.putInt(data.length);
        buffer.put(data);
        buffer.put(DataType.EndOfData.byteValue);

        outStream.write(ret);
    }
}
