/*
 * Name: Grant Upson
 * ID: 1225133
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Ellipse2D;
import java.io.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class GUI extends JFrame implements Runnable {
    private static final String SERVER_TITLE = "Whiteboard Server";
    private static final String FILE_CORRUPT = "Cannot load whiteboard file, it may be empty or corrupt";
    private static final String WHITEBOARD_LOADED = " was loaded";
    private static final String WHITEBOARD_SAVED = " was saved";
    private static final String SERVER_SHUTDOWN_MESSAGE = "The whiteboard server has been shutdown";
    private static final String EMPTY_STRING_MESSAGE = "You cannot send an empty chat message";
    private static final String CLEAR_WHITEBOARD_MESSAGE = "The whiteboard has been cleared";
    private static final String KICKED_MESSAGE = "You have been kicked from the whiteboard server";
    private static final String USER_CONNECTED_MESSAGE = "Has connected";
    private static final String USER_DISCONNECTED_MESSAGE = "Has disconnected";
    private static final String NOTIFY_CHAT_USER_KICKED_MESSAGE = "Has been kicked from the server";

    private static final int SHAPE_OFFSET = 35;
    private static final String RECTANGLE_SELECTOR = "Rectangle";
    private static final String CIRCLE_SELECTOR = "Circle";
    private static final String TRIANGLE_SELECTOR = "Triangle";
    private static final String LINE_SELECTOR = "Line";
    private static final String TEXT_SELECTOR = "Text";

    private Map<String, Color> colours;
    private final List<IClientCallback> clientConnections;
    private final List<IClientCallback> pendingConnections;
    private final String username;
    private final Registry registry;
    private final Server server;
    private String currentWhiteboardLocation;

    private JPanel guiPanel;
    private JTextField sendMessageField;
    private JButton sendButton;
    private JScrollPane chatScrollPane;
    private JTextPane chatTextArea;
    private JPanel whiteboardContainer;
    private JComboBox<String> shapeSelector;
    private JComboBox<String> colourSelector;
    private JTextField textToDraw;
    private JList<Object> connectedUsers;
    private JLabel connectedUsersLabel;
    private JScrollPane connectedUsersScrollbar;
    private final WhiteboardCanvas canvas;
    private JMenuBar menuBar;
    private JMenu menuDropdown;
    private JMenuItem newWhiteboard;
    private JMenuItem openWhiteboard;
    private JMenuItem saveWhiteboard;
    private JMenuItem saveAsWhiteboard;
    private JMenuItem closeWhiteboard;
    private JLabel pendingUsersLabel;
    private JList<Object> pendingConnectionsList;
    private JScrollPane pendingConnectionsPane;

    public GUI(String username, Registry registry, Server server) throws RemoteException {
        this.username = username;
        this.registry = registry;
        this.server = server;

        clientConnections = new CopyOnWriteArrayList<>();
        pendingConnections = new CopyOnWriteArrayList<>();
        canvas = new WhiteboardCanvas();
        currentWhiteboardLocation = "";

        initializeColourSelector();
        createShapeSelector();
        initializeListeners();
        synchronizeConnectedUsers();

        whiteboardContainer.add(canvas);
    }

    @Override
    public void run() {
        setTitle(SERVER_TITLE);
        setContentPane(guiPanel);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        pack();
        setVisible(true);
    }

    public synchronized void onClientConnection(IClientCallback client) throws RemoteException {
        pendingConnections.add(client);

        List<String> users = new ArrayList<>();

        for(IClientCallback c : pendingConnections) {
            users.add(c.getUsername());
        }

        pendingConnectionsList.setListData(users.toArray());
    }

    public synchronized void onClientTerminateRequest(IClientCallback client) throws RemoteException {
        pendingConnections.remove(client);
        updatePendingConnections();
    }

    public synchronized void onClientAccepted(IClientCallback client) throws RemoteException {
        pendingConnections.remove(client);
        clientConnections.add(client);
        synchronizeDrawables(client);
        synchronizeConnectedUsers();
        client.onConnectionAccepted();
        updateChatRoom(client.getUsername(), USER_CONNECTED_MESSAGE);
        updatePendingConnections();
    }

    public synchronized void onClientDisconnection(IClientCallback client) throws RemoteException {
        clientConnections.remove(client);
        synchronizeConnectedUsers();
        updateChatRoom(client.getUsername(), USER_DISCONNECTED_MESSAGE);
    }

    public synchronized boolean usernameIsConnected(IClientCallback client) throws RemoteException {
        boolean isConnected = false;

        for(IClientCallback c : clientConnections) {
            if(c.getUsername().equalsIgnoreCase(client.getUsername())) {
                isConnected = true;
            }
        }

        return isConnected;
    }

    public synchronized void updateDrawables(IDrawable drawable) throws RemoteException {
        canvas.addDrawable(drawable);

        for(IClientCallback c : clientConnections) {
            c.sendDrawable(drawable);
        }
    }

    public synchronized void synchronizeDrawables(IClientCallback client) throws RemoteException {
        client.synchronizeDrawables(canvas.getDrawables());
    }

    public synchronized void updateChatRoom(String username, String message) throws RemoteException {
        String timeStamp = new SimpleDateFormat("hh:mm aa").format(new Date());
        String convertedMessage = timeStamp + " | " + username + ": " + message;
        chatTextArea.setText(chatTextArea.getText() + "\n" + convertedMessage);

        for(IClientCallback c : clientConnections) {
            c.sendChatMessage(convertedMessage);
        }
    }

    private synchronized void synchronizeConnectedUsers() throws RemoteException {
        List<String> users = new ArrayList<>();
        users.add(username);

        for(IClientCallback c : clientConnections) {
            users.add(c.getUsername());
        }

        connectedUsers.setListData(users.toArray());

        for(IClientCallback c : clientConnections) {
            c.synchronizeCurrentUsers(users);
        }
    }

    private synchronized void updatePendingConnections() throws RemoteException {
        List<String> users = new ArrayList<>();

        for(IClientCallback c : pendingConnections) {
            users.add(c.getUsername());
        }

        pendingConnectionsList.setListData(users.toArray());
    }

    private void initializeColourSelector() {
        colours = new HashMap<>();
        colours.put("Black", new Color(0, 0, 0));
        colourSelector.addItem("Black");
        colours.put("Dark Grey", new Color(64, 64, 64));
        colourSelector.addItem("Dark Grey");
        colours.put("Grey", new Color(128, 128, 128));
        colourSelector.addItem("Grey");
        colours.put("Silver", new Color(192, 192, 192));
        colourSelector.addItem("Silver");
        colours.put("Aqua", new Color(0, 255, 255));
        colourSelector.addItem("Aqua");
        colours.put("Navy", new Color(0, 0, 128));
        colourSelector.addItem("Navy");
        colours.put("Blue", new Color(0, 0, 255));
        colourSelector.addItem("Blue");
        colours.put("Lime", new Color(0, 255, 0));
        colourSelector.addItem("Lime");
        colours.put("Green", new Color(0, 128, 0));
        colourSelector.addItem("Green");
        colours.put("Olive", new Color(128, 128, 0));
        colourSelector.addItem("Olive");
        colours.put("Teal", new Color(0, 128, 128));
        colourSelector.addItem("Teal");
        colours.put("Fuchsia", new Color(255, 0, 255));
        colourSelector.addItem("Fuchsia");
        colours.put("Purple", new Color(128, 0, 128));
        colourSelector.addItem("Purple");
        colours.put("Maroon", new Color(128, 0, 0));
        colourSelector.addItem("Maroon");
        colours.put("Red", new Color(255, 0, 0));
        colourSelector.addItem("Red");
        colours.put("Yellow", new Color(255, 255, 0));
        colourSelector.addItem("Yellow");
    }

    private void createShapeSelector() {
        shapeSelector.addItem(RECTANGLE_SELECTOR);
        shapeSelector.addItem(TRIANGLE_SELECTOR);
        shapeSelector.addItem(CIRCLE_SELECTOR);
        shapeSelector.addItem(LINE_SELECTOR);
        shapeSelector.addItem(TEXT_SELECTOR);
    }

    private Shape getSelectedShape(int x, int y) {
        return switch((String) Objects.requireNonNull(shapeSelector.getSelectedItem())) {
            case RECTANGLE_SELECTOR -> new Rectangle(x, y, 100, 50);
            case CIRCLE_SELECTOR -> new Ellipse2D.Double(x, y, 75, 75);
            case TRIANGLE_SELECTOR -> new Triangle(x, y, 75, 75);
            case LINE_SELECTOR -> new Rectangle(x, y, 200, 4);
            default -> null;
        };
    }

    private void initializeListeners() {
        EventQueue.invokeLater(() -> canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                try {
                    Drawable newDrawable;

                    if(shapeSelector.getSelectedItem() == TEXT_SELECTOR) {
                        newDrawable = new Drawable(null, colours.get((String)colourSelector.getSelectedItem()),
                                new WhiteboardText(textToDraw.getText(), e.getX(), e.getY()));
                    } else {
                        newDrawable = new Drawable(getSelectedShape(e.getX() - SHAPE_OFFSET,
                                e.getY() - SHAPE_OFFSET), colours.get((String)colourSelector.getSelectedItem()), null);
                    }

                    updateDrawables(newDrawable);
                } catch(RemoteException ex) {
                    ex.printStackTrace();
                }
            }
        }));

        EventQueue.invokeLater(() -> closeWhiteboard.addActionListener(e -> {
            try {
                exitApplication();
            } catch(RemoteException | NotBoundException ex) {
                ex.printStackTrace();
            }
        }));

        EventQueue.invokeLater(() -> sendButton.addActionListener(e -> {
            if(!sendMessageField.getText().trim().isEmpty()) {
                try {
                    updateChatRoom(username, sendMessageField.getText());
                    sendMessageField.setText("");
                } catch(RemoteException ex) {
                    ex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(null, EMPTY_STRING_MESSAGE);
            }
        }));

        EventQueue.invokeLater(() -> newWhiteboard.addActionListener(e -> {
            canvas.clear();

            for(IClientCallback c : clientConnections) {
                try {
                    c.synchronizeDrawables(canvas.getDrawables());
                    updateChatRoom(username, CLEAR_WHITEBOARD_MESSAGE);
                } catch(RemoteException ex) {
                    ex.printStackTrace();
                }
            }
        }));

        EventQueue.invokeLater(() -> saveWhiteboard.addActionListener(e -> {
            new Thread(() -> {
                if(!currentWhiteboardLocation.isEmpty()) {
                       saveFile(new File(currentWhiteboardLocation));
                } else {
                    try {
                        saveAsFile();
                    } catch(RemoteException ex) {
                        ex.printStackTrace();
                    }
                }
            }).start();
        }));

        EventQueue.invokeLater(() -> saveAsWhiteboard.addActionListener(e -> {
            new Thread(() -> {
                try {
                    saveAsFile();
                } catch(RemoteException ex) {
                    ex.printStackTrace();
                }
            }).start();
        }));

        EventQueue.invokeLater(() -> openWhiteboard.addActionListener(e -> {
            new Thread(() -> {
                final JFileChooser fc = new JFileChooser();
                int chosenOption = fc.showOpenDialog(null);

                if(chosenOption == JFileChooser.APPROVE_OPTION) {
                    File whiteboardFile = fc.getSelectedFile();
                    try(FileInputStream streamInput = new FileInputStream(whiteboardFile);
                    ObjectInputStream objectSteam = new ObjectInputStream(streamInput)) {
                        List<IDrawable> loadedDrawables = (List<IDrawable>) objectSteam.readObject();
                        canvas.loadCanvas(loadedDrawables);

                        for(IClientCallback c : clientConnections) {
                            c.synchronizeDrawables(loadedDrawables);
                        }

                        updateChatRoom(username, whiteboardFile.getName() + WHITEBOARD_LOADED);

                    } catch(IOException | ClassNotFoundException ex) {
                        JOptionPane.showMessageDialog(null, FILE_CORRUPT);
                    }
                }
            }).start();
        }));

        EventQueue.invokeLater(() -> {
            connectedUsers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            connectedUsers.addListSelectionListener(e -> {
                String selectedUser = (String)connectedUsers.getSelectedValue();
                for(IClientCallback c : clientConnections) {
                    try {
                        if(c.getUsername().equalsIgnoreCase(selectedUser)) {
                            clientConnections.remove(c);
                            c.onForcedDisconnect(KICKED_MESSAGE);
                            updateChatRoom(c.getUsername(), NOTIFY_CHAT_USER_KICKED_MESSAGE);
                            synchronizeConnectedUsers();
                        }
                    } catch(RemoteException ex) {
                        ex.printStackTrace();
                    }
                }
            });
        });

        EventQueue.invokeLater(() -> pendingConnectionsList.addListSelectionListener(e -> {
            pendingConnectionsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            String selectedUser = (String)pendingConnectionsList.getSelectedValue();
            for(IClientCallback c : pendingConnections) {
                try {
                    if(c.getUsername().equalsIgnoreCase(selectedUser)) {
                        onClientAccepted(c);
                        synchronizeConnectedUsers();
                    }
                } catch(RemoteException ex) {
                    ex.printStackTrace();
                }
            }
        }));

        EventQueue.invokeLater(() -> addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    exitApplication();
                } catch(RemoteException | NotBoundException ex) {
                    ex.printStackTrace();
                }
            }
        }));
    }

    private void saveAsFile() throws RemoteException {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showSaveDialog(null);
        if(option == JFileChooser.APPROVE_OPTION) {
            File whiteboardFile = fileChooser.getSelectedFile();
            currentWhiteboardLocation = whiteboardFile.getPath();
            saveFile(whiteboardFile);
            updateChatRoom(username, whiteboardFile.getName() + WHITEBOARD_SAVED);
        }
    }

    private void saveFile(File file) {
        try(FileOutputStream writer = new FileOutputStream(file);
            ObjectOutputStream objectWriter = new ObjectOutputStream(writer)) {

            List<IDrawable> drawables = canvas.getDrawables();
            List<IDrawable> notReferencedDrawables = new ArrayList<>();

            //This must be done, or there will be reference errors due to RMI
            for(IDrawable c : drawables) {
                notReferencedDrawables.add(new Drawable(c.getShape(), c.getColour(), c.getWhiteboardText()));
            }

            objectWriter.writeObject(notReferencedDrawables);
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    private void exitApplication() throws RemoteException, NotBoundException {
        for(IClientCallback c : clientConnections) {
            c.onForcedDisconnect(SERVER_SHUTDOWN_MESSAGE);
        }

        for(IClientCallback c : pendingConnections) {
            c.onForcedDisconnect(SERVER_SHUTDOWN_MESSAGE);
        }

        registry.unbind("Whiteboard");
        UnicastRemoteObject.unexportObject(server, true);

        System.exit(0);
    }
}
