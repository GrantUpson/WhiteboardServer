/*
 * Name: Grant Upson
 * ID: 1225133
 */

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server {
    private String hostUsername;
    private int port;

    public Server(String username, int port) {
        this.hostUsername = username;
        this.port = port;
    }

    public void start() throws RemoteException, AlreadyBoundException {
        Whiteboard whiteboard = new Whiteboard();
        IWhiteboard stub = (IWhiteboard) UnicastRemoteObject.exportObject(whiteboard, 3000); //The port to connect to the server.

        Registry registry = LocateRegistry.getRegistry("localhost", 1099); //Must be the same port assigned to registry on startup.
        registry.rebind("Whiteboard", whiteboard);

        while(true) {
            double timer = 0;
            timer++;

            if(timer >= 2000) {
                timer = 0;
                whiteboard.sendMessage("Boo!");
            }
        }
    }
}
