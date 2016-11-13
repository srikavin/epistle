package infuzion.chat.client.util;

import java.io.Serializable;

public class SerializableServer implements Serializable {
    public static long serialVersionUID = 1L;
    private final String serverName;
    private final String ip;
    private final int port;
    private final String clientName;

    public SerializableServer(String serverName, String ip, int port, String clientName) {
        this.serverName = serverName;
        this.ip = ip;
        this.port = port;
        this.clientName = clientName;
    }

    public String getClientName() {
        return clientName;
    }

    public int getPort() {
        return port;
    }

    public String getIp() {
        return ip;
    }

    public String getServerName() {
        return serverName;
    }
}
