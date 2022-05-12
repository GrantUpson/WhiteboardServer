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
import java.rmi.RemoteException;
import java.util.*;
import java.util.List;


public class GUI extends JFrame implements Runnable {
    private static final String EMPTY_STRING_MESSAGE = "You cannot send an empty chat message.";
    private static final int SHAPE_OFFSET = 35;

    private Map<String, Color> colours;
    private final IWhiteboardServer server;
    private final IClientCallback client;
    private boolean connectionAccepted;

    private JPanel guiPanel;
    private JTextField sendMessageField;
    private JButton sendButton;
    private JScrollPane chatScrollPane;
    private JTextPane chatTextArea;
    private JPanel whiteboardContainer;
    private JComboBox<String> shapeSelector;
    private JComboBox<String> colourSelector;
    private JTextField textToDraw;
    private JButton disconnectButton;
    private JList<Object> connectedUsers;
    private JLabel connectedUsersLabel;
    private JScrollPane connectedUsersScrollbar;
    private final WhiteboardCanvas canvas;

    public GUI(IWhiteboardServer server, IClientCallback client) {
        this.server = server;
        this.client = client;
        canvas = new WhiteboardCanvas();
        connectionAccepted = false;

        initializeColourSelector();
        createShapeSelector();

        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                try {
                    if(shapeSelector.getSelectedItem() == "Text") {
                        server.sendDrawable(new Drawable(null, colours.get((String)colourSelector.getSelectedItem()),
                                new WhiteboardText(textToDraw.getText(), e.getX(), e.getY())));
                    } else {
                        server.sendDrawable(new Drawable(getSelectedShape(e.getX() - SHAPE_OFFSET,
                                e.getY() - SHAPE_OFFSET), colours.get((String)colourSelector.getSelectedItem()), null));
                    }
                } catch(RemoteException ex) {
                    ex.printStackTrace();
                }
            }
        });

        disconnectButton.addActionListener(e -> {
            try {
                exitApplication();
            } catch(RemoteException ex) {
                ex.printStackTrace();
            }
        });

        sendButton.addActionListener(e -> {
            if(!sendMessageField.getText().trim().equalsIgnoreCase("")) {
                try {
                    server.sendChatMessage(client, sendMessageField.getText());
                    sendMessageField.setText("");
                } catch(RemoteException ex) {
                    ex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(null, EMPTY_STRING_MESSAGE);
            }
        });

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    exitApplication();
                } catch(RemoteException ex) {
                    ex.printStackTrace();
                }
            }
        });

        disableServerCommunication();
        whiteboardContainer.add(canvas);
    }

    @Override
    public void run() {
        setTitle("Whiteboard");
        setContentPane(guiPanel);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        pack();
        setVisible(true);
    }

    public void addDrawableFromServer(IDrawable drawable) {
        canvas.addDrawable(drawable);
    }

    public void updateDrawables(List<IDrawable> drawables) {
        canvas.addDrawableList(drawables);
    }

    public void addChatMessage(String message) {
        chatTextArea.setText(chatTextArea.getText() + "\n" + message);
    }

    public void updateConnectedUsers(List<String> users) {
        connectedUsers.setListData(users.toArray());
    }

    public void enableServerCommunication() {
        sendMessageField.setEnabled(true);
        sendButton.setEnabled(true);
        chatTextArea.setText("");
        shapeSelector.setEnabled(true);
        colourSelector.setEnabled(true);
        textToDraw.setEnabled(true);
        connectionAccepted = true;
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
        shapeSelector.addItem("Rectangle");
        shapeSelector.addItem("Triangle");
        shapeSelector.addItem("Circle");
        shapeSelector.addItem("Line");
        shapeSelector.addItem("Text");
    }

    private Shape getSelectedShape(int x, int y) {
        return switch((String) Objects.requireNonNull(shapeSelector.getSelectedItem())) {
            case "Rectangle" -> new Rectangle(x, y, 100, 50);
            case "Circle" -> new Ellipse2D.Double(x, y, 75, 75);
            case "Triangle" -> new Triangle(x, y, 75, 75);
            case "Line" -> new Rectangle(x, y, 200, 4);
            default -> null;
        };
    }

    private void disableServerCommunication() {
        sendMessageField.setEnabled(false);
        sendButton.setEnabled(false);
        chatTextArea.setText("Connection pending approval..");
        shapeSelector.setEnabled(false);
        colourSelector.setEnabled(false);
        textToDraw.setEnabled(false);
    }

    private void exitApplication() throws RemoteException {
        if(connectionAccepted) {
            server.unregister(client);
        }
        System.exit(0);
    }
}
