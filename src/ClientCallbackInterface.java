import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientCallbackInterface extends Remote {
    String getUsername() throws RemoteException;
    void message(String message) throws RemoteException;
}
