/*
 * Name: Grant Upson
 * ID: 1225133
 */

import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;
import java.rmi.RemoteException;


public class Launcher {
    private static final int NUM_ARGUMENTS = 3;
    private static final String INCORRECT_ARGUMENTS_RESPONSE =
            "Incorrect number of arguments. Usage: java -jar WhiteboardServer.jar <registry-hostname> <registry-port> <username>";

    public static void main(String[] args) throws RemoteException {
        //Skins the GUI to a dark theme.
        FlatDarkLaf.setup();

        if(args.length != NUM_ARGUMENTS) {
            JOptionPane.showMessageDialog(null, INCORRECT_ARGUMENTS_RESPONSE);
        } else {
            ServerConnection server = new ServerConnection(args[0], Integer.parseInt(args[1]), args[2]);
            server.start();
        }
    }
}
