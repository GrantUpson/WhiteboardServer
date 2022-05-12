/*
 * Name: Grant Upson
 * ID: 1225133
 */

import java.awt.*;


public class Triangle extends Polygon {

    public Triangle(int x, int y, int width, int height) {
        addPoint(x + width / 2, y);
        addPoint(x, y + height);
        addPoint(x + width, y + height);
    }
}
