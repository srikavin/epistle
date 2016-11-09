package infuzion.chat.server.plugin.event.command;

import infuzion.chat.server.IChatClient;
import infuzion.chat.server.plugin.event.CancelableEvent;

public class CommandEvent extends CancelableEvent {
    private String command;
    private String[] args;
    private IChatClient sender;

    public CommandEvent(String command, String[] args, IChatClient sender) {
        this.command = command;
        this.args = args;
        this.sender = sender;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

    public IChatClient getSender() {
        return sender;
    }
}
