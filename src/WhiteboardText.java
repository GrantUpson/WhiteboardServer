/*
 * Name: Grant Upson
 * ID: 1225133
 */

import java.io.Serializable;


public class WhiteboardText implements Serializable {
    private final String text;
    private final int x;
    private final int y;

    public WhiteboardText(String text, int x, int y) {
        this.text = text;
        this.x = x;
        this.y = y;
    }

    public String getText() {
        return text;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
