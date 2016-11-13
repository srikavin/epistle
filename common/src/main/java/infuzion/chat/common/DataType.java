/*
 *  Copyright 2016 Infuzion
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package infuzion.chat.common;

public enum DataType {
    Message((byte) 0x1),
    EndOfData((byte) 0x2),
    ColorChange((byte) 0x3),
    ClientHello((byte) 0x4),
    Heartbeat((byte) 0x5),
    Command((byte) 0x6),
    Kick((byte) 0x7);

    public byte byteValue;
    DataType(byte i) {
        byteValue = i;
    }

    public static DataType valueOf(byte messageType) {
        for(DataType e: values()){
            if(e.byteValue == messageType){
                return e;
            }
        }
        return null;
    }
}
