/*
 *
 *  *  Copyright 2016 Infuzion
 *  *
 *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *    you may not use this file except in compliance with the License.
 *  *    You may obtain a copy of the License at
 *  *
 *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *    Unless required by applicable law or agreed to in writing, software
 *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *    See the License for the specific language governing permissions and
 *  *    limitations under the License.
 *
 */

package infuzion.chat.server.permission;

import infuzion.chat.common.DataType;
import infuzion.chat.server.IChatClient;
import infuzion.chat.server.IChatRoom;

import java.util.UUID;

public class FakeClient implements IChatClient {
    private PermissionAttachment permissions;

    @Override
    public void kick(String message) {

    }

    @Override
    public UUID getUuid() {
        return UUID.fromString("d74b0076-1b16-481c-9a7c-f3bd84c659a1");
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void sendMessage(String message) {

    }

    @Override
    public IChatRoom getChatRoom() {
        return null;
    }

    @Override
    public void setChatRoom(IChatRoom IChatRoom) {

    }

    @Override
    public void sendData(String data, DataType id) {

    }

    @Override
    public String getPrefix() {
        return null;
    }

    @Override
    public void setPrefix(String prefix) {

    }

    @Override
    public PermissionAttachment getPermissionAttachment() {
        return permissions;
    }

    @Override
    public void setPermissionAttachment(PermissionAttachment attachment) {
        this.permissions = attachment;
    }

    @Override
    public boolean isConsole() {
        return false;
    }
}
