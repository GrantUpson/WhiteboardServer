/*
 * Name: Grant Upson
 * ID: 1225133
 */

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;


public interface IWhiteboard extends Remote {
    void register(ClientCallbackInterface client) throws RemoteException;
    void unregister(ClientCallbackInterface client) throws RemoteException;
    List<ClientCallbackInterface> getClients() throws RemoteException;
    void broadcastMessage(String message) throws RemoteException;
}