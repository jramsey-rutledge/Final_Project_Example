package Final_Project_Example;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Hazard {
    private double x, y;
    private int size = 40;
    private static BufferedImage sprite;
    private int speed = 1;

    static {
         try {
            sprite = ImageIO.read(new File("Final_Project_Example/assets/hazard.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    // constructor 
    public Hazard(double startX, double startY)
    {
        x = startX;
        y = startY;
    }

    public Rectangle getBounds(){
        return new Rectangle((int)x,(int)y, size, size);
    }

    public void draw(Graphics g)
    {
        if (sprite != null)
        {
            g.drawImage(sprite, (int) x, (int) y, size, size, null);
        }
        else {
            g.setColor(Color.RED);
            g.fillRect((int) x, (int) y, size, size);
        }
    }

    public void update(Player p)
    {
        double px = p.getX();
        double py = p.getY();
        double dx = Math.abs(px-x);
        double dy = Math.abs(py-y);
        double distance = Math.sqrt(dx*dx + dy*dy);

        if (distance < 500)
        {
            if (x > px)
                x -= speed;
            if (x < px)
                x += speed;
            if (y < py)
                y += speed;
            if (y > py)
                y -= speed;
        }
    }

    public void handleCollisions(Hazard other) {
        // find centers 
        double thisCenterX = this.x + this.size / 2.0;
        double thisCenterY = this.y + this.size / 2.0;
        double otherCenterX = other.x + other.size / 2.0;
        double otherCenterY = other.y + other.size / 2.0;

        // calculate distances
        double dx = otherCenterX - thisCenterX;
        double dy = otherCenterY - thisCenterY;
        double distance = Math.sqrt(dx * dx + dy * dy);
        
        // sum of radii
        double minDistance = (this.size / 2.0) + (other.size / 2.0);

        //Only resolve if they actually overlap
        if (distance < minDistance && distance > 0) {
            double overlap = 0.5 * (minDistance - distance);

            double nx = dx / distance;
            double ny = dy / distance;

            this.x -= nx * overlap;
            this.y -= ny * overlap;
            other.x += nx * overlap;
            other.y += ny * overlap;
        }
    }

    public void setSpeed(int newSpeed)
    {
        speed = newSpeed;
    }
}
