package infuzion.chat.server;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomManager {
    List<ChatRoom> chatRooms = new ArrayList<>();

    ChatRoomManager() {

    }

    void addChatRoom(ChatRoom chatRoom){
        chatRooms.add(chatRoom);
    }

    void sendMessageAll(String message){

    }

    ChatRoom getChatRoom(ChatClient client){
        for(ChatRoom e: chatRooms){
            if(e.getClients().contains(client)){
                return e;
            }
        }
        return null;
    }

}
