/*
 * Name: Grant Upson
 * ID: 1225133
 */

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class Whiteboard implements IWhiteboard {
    private ArrayList<IClientConnection> connections;
    private List<String> usernames;
    private int messagesSent = 0;

    public Whiteboard() throws RemoteException {
        usernames = new ArrayList<>();
        usernames.add("Anguish");
        usernames.add("Sorcery");
    }

    @Override
    public void login(IClientConnection client) throws RemoteException {
        System.out.println("Client connected!");
        connections.add(client);
        usernames.add(client.getUsername());
    }

    @Override
    public void logout(IClientConnection client) throws RemoteException {
        usernames.remove(client.getUsername());
    }

    @Override
    public void sendMessage(String message) throws RemoteException {
        for(IClientConnection c : connections) {
            c.sendMessage("Message from server");
        }
    }

    @Override
    public List<String> getUsernames() throws RemoteException {
        return usernames;
    }

    @Override
    public int messagesSent() throws RemoteException {
        return messagesSent;
    }
}
