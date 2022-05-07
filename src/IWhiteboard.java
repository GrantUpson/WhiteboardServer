import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IWhiteboard extends Remote {
    void sendMessage(String message) throws RemoteException;
    List<String> getUsernames() throws RemoteException;
}
