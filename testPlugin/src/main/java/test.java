import infuzion.chat.server.IChatClient;
import infuzion.chat.server.command.Command;
import infuzion.chat.server.plugin.Plugin;
import infuzion.chat.server.plugin.command.ICommandExecutor;
import infuzion.chat.server.plugin.event.IEventListener;
import infuzion.chat.server.plugin.event.chat.MessageEvent;
import infuzion.chat.server.plugin.event.command.PreCommandEvent;
import infuzion.chat.server.plugin.event.reflection.EventHandler;

public class test extends Plugin implements ICommandExecutor, IEventListener {

    @Override
    public void onEnable() {
        System.out.println("312");
        getCommandManager().registerCommand(new Command("abc"), this);
        getEventManager().registerEvents(this, this);
    }

    @Override
    public void onLoad() {
        System.out.println("123");
    }

    @Override
    public void onDisable() {
        System.out.println("321");
    }


    @Override
    public void onCommand(String commandName, String[] args, IChatClient client) {
        client.sendMessage(commandName + " wowow");
        for (String e : args) {
            client.sendMessage(e);
        }
    }

    @Override
    public String[] getHelp() {
        return new String[0];
    }

    @EventHandler
    public void onChat(MessageEvent event) {
        event.setCanceled(true);
        event.getSender().sendMessage("NOPE!");
    }

    @EventHandler
    public void onCommand(PreCommandEvent event) {
        event.setCanceled(true);
        event.getSender().sendMessage("NOPE!!!");
    }
}
