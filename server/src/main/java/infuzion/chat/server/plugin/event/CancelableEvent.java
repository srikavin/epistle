package infuzion.chat.server.plugin.event;

public abstract class CancelableEvent extends Event {
    private boolean canceled = false;

    public final boolean isCanceled() {
        return canceled;
    }

    public final void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }
}
