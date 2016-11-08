import infuzion.chat.server.plugin.Plugin;

public class test extends Plugin {

    @Override
    public void onEnable() {
        System.out.println("312");
    }

    @Override
    public void onLoad() {
        System.out.println("123");
    }

    @Override
    public void onDisable() {
        System.out.println("321");
    }
}
