/*
 * Name: Grant Upson
 * ID: 1225133
 */

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Whiteboard extends UnicastRemoteObject implements IWhiteboard {
    private static final String KICKED_MESSAGE = "You have been kicked from the whiteboard server.";
    private static final String SERVER_SHUTDOWN_MESSAGE = "The whiteboard server has been shutdown.";

    List<ClientCallbackInterface> connectedClients;
    List<IDrawable> drawables;

    public Whiteboard() throws RemoteException {
        connectedClients = new ArrayList<>();
        drawables = new ArrayList<>();
    }


    @Override
    public synchronized boolean register(ClientCallbackInterface client) throws RemoteException {
        boolean connected = false;

        if(!usernameIsConnected(client.getUsername())) {
            connected = true;
            connectedClients.add(client);
            sendChatMessage(client, "has connected" );
            synchronizeClients(client);
            client.onConnectionAccepted();
        }

        return connected;
    }

    @Override
    public synchronized void unregister(ClientCallbackInterface client) throws RemoteException {
        connectedClients.remove(client);
        sendChatMessage(client, "has disconnected");

        List<String> users = new ArrayList<>();
        for(ClientCallbackInterface c : connectedClients) {
            users.add(c.getUsername());
        }

        for(ClientCallbackInterface c : connectedClients) {
            c.synchronizeCurrentUsers(users);
        }
    }

    @Override
    public synchronized void sendChatMessage(ClientCallbackInterface client, String message) throws RemoteException {
        String timeStamp = new SimpleDateFormat("hh:mm aa").format(new Date());
        for(ClientCallbackInterface c : connectedClients) {
            c.sendChatMessage(timeStamp + " | " + client.getUsername() + " - " + message);
        }
    }

    @Override
    public synchronized void sendDrawable(IDrawable drawable) throws RemoteException {
        drawables.add(drawable);

        for(ClientCallbackInterface client : connectedClients) {
            client.sendDrawable(drawable);
        }
    }

    //TODO create a method that can be called for updating connectedUsers in clients?
    private void synchronizeClients(ClientCallbackInterface connectingClient) throws RemoteException {
        connectingClient.synchronizeDrawables(drawables);

        List<String> users = new ArrayList<>();
        for(ClientCallbackInterface c : connectedClients) {
            users.add(c.getUsername());
        }

        for(ClientCallbackInterface c : connectedClients) {
            c.synchronizeCurrentUsers(users);
        }
    }

    private boolean usernameIsConnected(String username) throws RemoteException {
        boolean isConnected = false;

        for(ClientCallbackInterface c : connectedClients) {
            if(c.getUsername().equalsIgnoreCase(username)) {
                isConnected = true;
            }
        }

        return isConnected;
    }
}
