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

public class MessagePacket extends NetworkPacket {
    public static final DataType signature = DataType.Message;
    private final String message;

    public MessagePacket(String command) {
        super(signature);
        this.message = command;
    }

    public MessagePacket(byte[] bytes) {
        this(ByteBuffer.wrap(bytes));
    }

    public MessagePacket(ByteBuffer buffer) {
        super(signature);
        int commandLength = buffer.getInt();
        byte[] bytes = new byte[commandLength];

        buffer.get(bytes);
        this.message = new String(bytes, StandardCharsets.UTF_8);
    }

    @Override
    public byte[] asBytes() {
        byte[] ret = new byte[message.length() + 4];
        ByteBuffer buffer = ByteBuffer.wrap(ret);

        buffer.putInt(message.length());
        buffer.put(message.getBytes(StandardCharsets.UTF_8));

        return ret;
    }

    public String getMessage() {
        return message;
    }
}
