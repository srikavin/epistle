package infuzion.chat.server;

import java.io.IOException;

public class Main {
    public static void main(String args[]) throws IOException {
        if (args.length == 0) {
            new Thread(new Server(7776)).start();
        } else {
            new Thread(new Server(Integer.valueOf(args[0]))).start();
        }
    }
}
