/*
 * Name: Grant Upson
 * ID: 1225133
 */

import java.awt.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class Drawable extends UnicastRemoteObject implements IDrawable {
    private final Shape shape;
    private final Color colour;
    private final WhiteboardText text;
    private boolean isShape = false;

    public Drawable(Shape shape, Color colour, WhiteboardText text) throws RemoteException {
        super();
        this.shape = shape;
        this.colour = colour;
        this.text = text;

        if(text == null) { isShape = true; }
    }

    @Override
    public Shape getShape() throws RemoteException {
        return shape;
    }

    @Override
    public Color getColour() throws RemoteException {
        return colour;
    }

    @Override
    public WhiteboardText getWhiteboardText() throws RemoteException {
        return text;
    }

    @Override
    public boolean isShape() throws RemoteException {
        return isShape;
    }
}
