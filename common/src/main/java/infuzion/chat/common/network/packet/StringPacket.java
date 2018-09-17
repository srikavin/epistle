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

public abstract class StringPacket extends NetworkPacket {
    protected final String string;

    protected StringPacket(short signature, byte[] bytes) {
        this(signature, ByteBuffer.wrap(bytes));
    }

    protected StringPacket(DataType signature, byte[] bytes) {
        this(signature.value, ByteBuffer.wrap(bytes));
    }

    protected StringPacket(DataType signature, ByteBuffer buffer) {
        this(signature.value, buffer);
    }

    protected StringPacket(DataType signature, String message) {
        this(signature.value, message);
    }

    protected StringPacket(short signature, String message) {
        super(signature);
        this.string = message;
    }

    protected StringPacket(short signature, ByteBuffer buffer) {
        super(signature);
        int commandLength = buffer.getInt();
        byte[] bytes = new byte[commandLength];

        buffer.get(bytes);
        this.string = new String(bytes, StandardCharsets.UTF_8);
    }

    protected String getString() {
        return string;
    }

    @Override
    public byte[] asBytes() {
        byte[] ret = new byte[string.length() + 4];
        ByteBuffer buffer = ByteBuffer.wrap(ret);

        buffer.putInt(string.length());
        buffer.put(string.getBytes(StandardCharsets.UTF_8));

        return ret;
    }

}
