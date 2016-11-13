package infuzion.chat.server.plugin.event.connection;

import infuzion.chat.server.IChatClient;
import infuzion.chat.server.plugin.event.Cancelable;

public class KickEvent extends QuitEvent implements Cancelable {
    boolean canceled = false;

    public KickEvent(IChatClient client) {
        super(client);
    }

    @Override
    public boolean isCanceled() {
        return canceled;
    }

    @Override
    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }
}
