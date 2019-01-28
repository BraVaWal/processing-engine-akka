package it.polimi.middleware.processingengine.message;

import java.io.Serializable;

public class AddRemoteMessage implements Serializable {

    private final String remoteSystem;
    private final String host;
    private final int port;

    public AddRemoteMessage(String remoteSystem, String host, int port) {
        this.remoteSystem = remoteSystem;
        this.host = host;
        this.port = port;
    }

    public String getRemoteSystem() {
        return remoteSystem;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
}
