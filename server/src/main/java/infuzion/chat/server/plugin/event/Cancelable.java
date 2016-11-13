package infuzion.chat.server.plugin.event;

public interface Cancelable {

    boolean isCanceled();

    void setCanceled(boolean canceled);
}
