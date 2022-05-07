/*
 * Name: Grant Upson
 * ID: 1225133
 */

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class Server {
    private static final String RMI_PROPERTY = "java.rmi.server.hostname";
    private String hostname;
    private int port;
    private String username;

    public Server(String hostname, int port, String username) {
        this.hostname = hostname;
        this.port = port;
        this.username = username;
    }

    public void start() throws RemoteException {
        System.setProperty(RMI_PROPERTY, hostname);
        Registry registry = LocateRegistry.getRegistry(hostname, port); //Must be the same port assigned to registry on startup.

        Whiteboard whiteboard = new Whiteboard();
        registry.rebind("Whiteboard", whiteboard);
    }
}
