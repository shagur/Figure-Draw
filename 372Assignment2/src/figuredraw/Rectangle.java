package figuredraw;

import java.awt.Color;
import java.awt.Graphics;

public class Rectangle extends Figure {

    private int x, y, w, h;

    public Rectangle(Color color) {
        super(color);
    }

    public void draw(Graphics graphics, int x, int y, int w, int h) {
        graphics.setColor(color);
        graphics.fillRect(x, y, w, h);
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    @Override
    public String toString() {
        return "Rectangle [x=" + x + ", y=" + y + ", Width=" + (w) + ", Height=" + (h) + ", Color=" + color + " ]\n";
    }

}
