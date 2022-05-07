/*
 * Name: Grant Upson
 * ID: 1225133
 */

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class Whiteboard extends UnicastRemoteObject implements IWhiteboard {
    List<ClientCallbackInterface> clients;

    public Whiteboard() throws RemoteException {
        clients = new ArrayList<>();
    }

    @Override
    public void register(ClientCallbackInterface client) throws RemoteException {
        clients.add(client);
        System.out.println("Client " + client.getUsername() + " registered.");
    }

    @Override
    public void unregister(ClientCallbackInterface client) throws RemoteException {
        clients.remove(client);
        System.out.println("Client " + client.getUsername() + " unregistered.");
    }

    @Override
    public List<ClientCallbackInterface> getClients() throws RemoteException {
        return clients;
    }

    @Override
    public void broadcastMessage(String message) throws RemoteException {
        for(ClientCallbackInterface client: clients) {
            client.message(message);
        }
    }
}
