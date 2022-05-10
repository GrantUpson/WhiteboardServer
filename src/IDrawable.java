import java.awt.*;
import java.rmi.Remote;
import java.rmi.RemoteException;


public interface IDrawable extends Remote {
    Shape getShape() throws RemoteException;
    Color getColour() throws RemoteException;
    WhiteboardText getWhiteboardText() throws RemoteException;
    boolean isShape() throws RemoteException;
}
