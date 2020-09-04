package figuredraw;

import java.awt.Color;
import java.awt.Graphics;


public class Circle extends Figure {

    private int x, y, w, h;

    public Circle(Color color) {
        super(color);
    }

    public void draw(Graphics graphics, int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        graphics.setColor(color);
        graphics.fillOval(x, y, w, h);
    }

    @Override
    public String toString() {
        return "Circle [x=" + x + ", y=" + y + ", radius=" + (w / 2) + ", Color=" + color + " ]\n";
    }

}
