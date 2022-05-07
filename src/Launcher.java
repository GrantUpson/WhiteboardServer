import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;

public class Launcher {
    public static void main(String[] args) throws AlreadyBoundException, RemoteException {
        Server server = new Server();
        server.start();
    }
}
