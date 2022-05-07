import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IClientConnection extends Remote {
    void ping() throws RemoteException;
    void sendMessage(String message) throws RemoteException;
    String getUsername() throws RemoteException;
}
