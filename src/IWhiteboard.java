/*
 * Name: Grant Upson
 * ID: 1225133
 */

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface IWhiteboard extends Remote {
    boolean register(ClientCallbackInterface client) throws RemoteException;
    void unregister(ClientCallbackInterface client) throws RemoteException;
    void sendChatMessage(ClientCallbackInterface client, String message) throws RemoteException;
    void sendDrawable(IDrawable drawable) throws RemoteException;
}