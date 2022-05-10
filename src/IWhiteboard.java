/*
 * Name: Grant Upson
 * ID: 1225133
 */

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;


public interface IWhiteboard extends Remote {
    boolean register(ClientCallbackInterface client) throws RemoteException;
    void unregister(ClientCallbackInterface client) throws RemoteException;
    List<ClientCallbackInterface> getClients() throws RemoteException;
    void sendChatMessage(String message) throws RemoteException;
    void sendDrawable(IDrawable drawable) throws RemoteException;
}