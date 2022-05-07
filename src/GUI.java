import javax.swing.*;

public class GUI extends JFrame {
    private JPanel guiPanel;
    private JTextField sendMessageField;
    private JButton sendButton;
    private JScrollPane chatScrollPane;
    private JTextPane chatTextArea;
    private JPanel canvasArea;
    private JList usersList;
    private JMenuBar menuBar;
    private JMenu menuDropdown;
    private JMenuItem newWhiteboardItem;
    private JMenuItem openWhiteboardItem;
    private JMenuItem saveWhiteboardItem;
    private JMenuItem saveAsWhiteboardItem;
    private JMenuItem closeWhiteboardItem;

    public GUI() {
        setTitle("Whiteboard");
        setContentPane(guiPanel);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }
}
