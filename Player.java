package Final_Project_Example;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Player {
    private double x, y;
    private int width = 50; 
    private int height = 50;
    private double speed = 4.5;
    private BufferedImage sprite;
    private int hitboxPadding = 6;

    public Player(double startX, double startY, String filePath)
    {
        x = startX;
        y = startY;

        try {
            sprite = ImageIO.read(new File(filePath));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    public void draw(Graphics g)
    {
        if (sprite != null)
        {
            g.drawImage(sprite, (int) x, (int) y, width, height, null);
        }
        else {
            g.setColor(Color.GRAY);
            g.fillRect((int) x, (int) y, width, height);
        }
    }

    public void update(boolean[] keys)
    {
        if (keys[KeyEvent.VK_UP]) {y -= speed;}
        if (keys[KeyEvent.VK_DOWN]) {y += speed;}
        if (keys[KeyEvent.VK_RIGHT]) {x += speed;}
        if (keys[KeyEvent.VK_LEFT]) {x -= speed;}
    }

    public Rectangle getBounds() {
        return new Rectangle((int) x + hitboxPadding, (int) y + hitboxPadding, width - (hitboxPadding *2), height - (hitboxPadding*2));
    }

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }
}
