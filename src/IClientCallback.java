/*
 * Name: Grant Upson
 * ID: 1225133
 */

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;


public interface IClientCallback extends Remote {
    void onConnectionAccepted() throws RemoteException;
    void synchronizeCurrentUsers(List<String> users) throws RemoteException;
    void synchronizeDrawables(List<IDrawable> drawables) throws RemoteException;
    String getUsername() throws RemoteException;
    void sendDrawable(IDrawable drawable) throws RemoteException;
    void sendChatMessage(String message) throws RemoteException;
    void onKick(String message) throws RemoteException;
    void onServerShutdown(String message) throws RemoteException;
}