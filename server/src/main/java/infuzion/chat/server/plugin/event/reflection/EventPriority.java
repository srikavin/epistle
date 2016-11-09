package infuzion.chat.server.plugin.event.reflection;

public enum EventPriority {
    LOW(0),
    NORMAL(1),
    HIGH(2),
    VERY_HIGH(3),
    MONITOR(4);

    final int priority;

    EventPriority(int priority) {
        this.priority = priority;
    }
}
