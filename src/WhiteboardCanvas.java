/*
 * Name: Grant Upson
 * ID: 1225133
 */

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;


public class WhiteboardCanvas extends JPanel {
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 500;

    private List<IDrawable> drawableList;

    public WhiteboardCanvas() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        drawableList = new ArrayList<>();
        this.setBackground(Color.WHITE);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D renderer = (Graphics2D)g;

        for(IDrawable drawable : drawableList) {
            try {
                renderer.setColor(drawable.getColour());
                if(drawable.isShape()) {
                    renderer.fill(drawable.getShape());
                } else {
                    WhiteboardText text = drawable.getWhiteboardText();
                    renderer.drawString(text.getText(), text.getX(), text.getY());
                }
            } catch(RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void addDrawable(IDrawable drawable) {
        drawableList.add(drawable);
        repaint();
    }

    public void addDrawableList(List<IDrawable> drawables) {
        drawableList = drawables;
        repaint();
    }
}
