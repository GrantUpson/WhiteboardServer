import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class Whiteboard implements IWhiteboard {
    List<String> usernames;

    public Whiteboard() throws RemoteException {
        usernames = new ArrayList<>();
        usernames.add("Anguish");
        usernames.add("Sorcery");
    }

    @Override
    public void sendMessage(String message) throws RemoteException {
        System.out.println(message);
    }

    @Override
    public List<String> getUsernames() throws RemoteException {
        return usernames;
    }
}
