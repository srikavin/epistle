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

package infuzion.chat.common.network.packet;

import infuzion.chat.common.DataType;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class ClientHelloPacket extends NetworkPacket {
    public static final DataType signature = DataType.ClientHello;
    private final byte[] usernameBytes;
    private final String username;
    private final UUID uuid;

    public ClientHelloPacket(String username, UUID uuid) {
        super(signature);
        this.usernameBytes = username.getBytes(StandardCharsets.UTF_8);
        this.username = username;
        this.uuid = uuid;
    }

    public ClientHelloPacket(byte[] bytes) {
        this(ByteBuffer.wrap(bytes));
    }

    public ClientHelloPacket(ByteBuffer buffer) {
        super(signature);
        int usernameLength = buffer.getInt();

        byte[] usernameBytes = new byte[usernameLength];
        buffer.get(usernameBytes);

        long uuidMostSignificantBits = buffer.getLong();
        long uuidLeastSignificantBits = buffer.getLong();

        this.usernameBytes = usernameBytes;
        this.username = new String(usernameBytes, StandardCharsets.UTF_8);
        this.uuid = new UUID(uuidMostSignificantBits, uuidLeastSignificantBits);
    }

    public UUID getUuid() {
        return uuid;
    }

    @Override
    public byte[] asBytes() {
        long uuidLeastSignificantBits = uuid.getLeastSignificantBits();
        long uuidMostSignificantBits = uuid.getMostSignificantBits();

        byte[] ret = new byte[4 + usernameBytes.length + 8 + 8];
        ByteBuffer buffer = ByteBuffer.wrap(ret);

        buffer.putInt(usernameBytes.length);        // 4 bytes
        buffer.put(usernameBytes);                  // x bytes
        buffer.putLong(uuidMostSignificantBits);    // 8 bytes
        buffer.putLong(uuidLeastSignificantBits);   // 8 bytes

        return ret;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ClientHelloPacket packet = (ClientHelloPacket) o;

        if (!username.equals(packet.username)) {
            return false;
        }
        return uuid.equals(packet.uuid);
    }

    @Override
    public int hashCode() {
        int result = username.hashCode();
        result = 31 * result + uuid.hashCode();
        return result;
    }
}
