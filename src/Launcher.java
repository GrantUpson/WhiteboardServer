/*
 * Name: Grant Upson
 * ID: 1225133
 */

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;

//TODO rmiregistry 1099
public class Launcher {
    private static final int NUM_ARGUMENTS = 3;
    private static final String INCORRECT_ARGUMENTS_RESPONSE =
            "Incorrect number of arguments. Usage: java -jar WhiteboardServer.jar <hostname> <registry-port> <username>";

    public static void main(String[] args) throws AlreadyBoundException, RemoteException {
        //Skins the GUI to a dark theme.
        FlatDarkLaf.setup();

        if(args.length != NUM_ARGUMENTS) {
            JOptionPane.showMessageDialog(null, INCORRECT_ARGUMENTS_RESPONSE);
        } else {
            Server server = new Server(args[0], Integer.parseInt(args[1]), args[2]);
            server.start();
            //GUI gui = new GUI();
        }
    }
}
