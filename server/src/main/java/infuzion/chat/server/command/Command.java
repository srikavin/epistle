package infuzion.chat.server.command;

public class Command {
    private String name;

    public Command(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean equals(Object o) {
        if (o instanceof Command) {
            if (((Command) o).getName().equals(name)) {
                return true;
            }
        }
        return false;
    }
}
