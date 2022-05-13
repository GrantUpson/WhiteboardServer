/*
 * Name: Grant Upson
 * ID: 1225133
 */

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface IWhiteboardServer extends Remote {
    boolean connect(IClientCallback client) throws RemoteException;
    void disconnect(IClientCallback client) throws RemoteException;
    void sendChatMessage(IClientCallback client, String message) throws RemoteException;
    void sendDrawable(IDrawable drawable) throws RemoteException;
}