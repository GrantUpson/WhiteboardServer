/*
 * Name: Grant Upson
 * ID: 1225133
 */

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class Whiteboard extends UnicastRemoteObject implements IWhiteboard {
    List<ClientCallbackInterface> connectedClients;
    List<IDrawable> drawables;

    public Whiteboard() throws RemoteException {
        connectedClients = new ArrayList<>();
        drawables = new ArrayList<>();
    }


    @Override
    public boolean register(ClientCallbackInterface client) throws RemoteException {
        /*boolean alreadyConnected = false;

        for(ClientCallbackInterface c : connectedClients) {
            if(c.getUsername().equalsIgnoreCase(client.getUsername())) {
                alreadyConnected = true;
            }
        }

        return alreadyConnected;*/
        connectedClients.add(client);
        System.out.println("Client added");
        client.syncDrawables(drawables);
        System.out.println("Drawables synced!");

        return true;
    }

    @Override
    public void unregister(ClientCallbackInterface client) throws RemoteException {
        //TODO
    }

    @Override
    public List<ClientCallbackInterface> getClients() throws RemoteException {
        return null; //TODO
    }

    @Override
    public void sendChatMessage(String message) throws RemoteException {
        //TODO
    }

    @Override
    public void sendDrawable(IDrawable drawable) throws RemoteException {
        drawables.add(drawable);

        for(ClientCallbackInterface client : connectedClients) {
            client.sendDrawable(drawable);
        }
    }
}
