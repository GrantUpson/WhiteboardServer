/*
 * Name: Grant Upson
 * ID: 1225133
 */

import java.awt.*;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


public class Server extends UnicastRemoteObject implements IWhiteboardServer {
    private final GUI gui;

    public Server(String username, Registry registry) throws RemoteException {
        EventQueue.invokeLater(gui = new GUI(username, registry, this));
    }

    @Override
    public synchronized boolean connect(IClientCallback client) throws RemoteException {
       boolean connected = false;

        if(!gui.usernameIsConnected(client)) {
            connected = true;
            gui.onClientConnection(client);
        }

        return connected;
    }

    @Override
    public synchronized void disconnect(IClientCallback client) throws RemoteException {
        gui.onClientDisconnection(client);
    }

    @Override
    public synchronized void sendChatMessage(IClientCallback client, String message) throws RemoteException {
        gui.updateChatRoom(client.getUsername(), message);
    }

    @Override
    public synchronized void sendDrawable(IDrawable drawable) throws RemoteException {
        gui.updateDrawables(drawable);
    }
}
