package infuzion.chat.server.permission;

import infuzion.chat.common.DataType;
import infuzion.chat.server.IChatClient;
import infuzion.chat.server.IChatRoom;

import java.util.UUID;

/**
 * Created by Sri on 11/22/2016.
 */
public class FakeClient implements IChatClient {
    PermissionAttachment permissions;

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
