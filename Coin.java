package Final_Project_Example;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Coin {
    private double x, y;
    private int size = 25;
    private static BufferedImage sprite;

    static {
         try {
            sprite = ImageIO.read(new File("Final_Project_Example/assets/coin.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    // constructor 
    public Coin(double startX, double startY)
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
            g.setColor(Color.YELLOW);
            g.fillRect((int) x, (int) y, size, size);
        }
    }
    

}
