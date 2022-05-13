/*
 * Name: Grant Upson
 * ID: 1225133
 */

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class ServerConnection {
    private final String hostname;
    private final int port;
    private final String username;

    public ServerConnection(String hostname, int port, String username) {
        this.hostname = hostname;
        this.port = port;
        this.username = username;
    }

    public void start() throws RemoteException {
        Registry registry = LocateRegistry.getRegistry(hostname, port);
        Server server = new Server(username, registry);
        registry.rebind("Whiteboard", server);
    }
}
