import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ClientCallbackInterface extends Remote {
    void sendChatMessage(String message) throws RemoteException;
    void syncDrawables(List<IDrawable> drawables) throws RemoteException;
    void sendDrawable(IDrawable drawable) throws RemoteException;
    void kickFromWhiteboard() throws RemoteException;
    String getUsername() throws RemoteException;
}
