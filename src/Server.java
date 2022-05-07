import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server {
    private int port = 3000;

    public Server() {

    }

    public void start() throws RemoteException, AlreadyBoundException {
        System.setProperty("java.rmi.server.hostname", "127.0.0.1");

        Whiteboard obj = new Whiteboard();
        IWhiteboard stub = (IWhiteboard) UnicastRemoteObject.exportObject(obj, 0);

        Registry registry = LocateRegistry.getRegistry("127.0.0.1", 9100);
        registry.bind("WhiteboardServer", obj);
    }
}
