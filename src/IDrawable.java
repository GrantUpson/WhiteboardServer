/*
 * Name: Grant Upson
 * ID: 1225133
 */

import java.awt.*;
import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;


public interface IDrawable extends Remote, Serializable {
    Shape getShape() throws RemoteException;
    Color getColour() throws RemoteException;
    WhiteboardText getWhiteboardText() throws RemoteException;
    boolean isShape() throws RemoteException;
}
