package infuzion.chat.server.plugin.event.command;

import infuzion.chat.server.IChatClient;

public class PreCommandEvent extends CommandEvent {

    public PreCommandEvent(String command, String[] args, IChatClient sender) {
        super(command, args, sender);
    }
}
