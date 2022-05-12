/*
 * Name: Grant Upson
 * ID: 1225133
 */

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface IWhiteboardServer extends Remote {
    boolean register(IClientCallback client) throws RemoteException;
    void unregister(IClientCallback client) throws RemoteException;
    void sendChatMessage(IClientCallback client, String message) throws RemoteException;
    void sendDrawable(IDrawable drawable) throws RemoteException;
}