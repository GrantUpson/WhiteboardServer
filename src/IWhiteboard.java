/*
 * Name: Grant Upson
 * ID: 1225133
 */

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IWhiteboard extends Remote {
    void login(IClientConnection client) throws RemoteException;
    void logout(IClientConnection client) throws RemoteException;
    void sendMessage(String message) throws RemoteException;
    List<String> getUsernames() throws RemoteException;
    int messagesSent() throws RemoteException;
}
