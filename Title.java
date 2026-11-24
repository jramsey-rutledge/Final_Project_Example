package Final_Project_Example;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Title {
    private Image titleImage;
    private double x, y;
    private double vx, vy;
    private int height = 400;
    private int width = 400;

    public Title()
    {
        // add title 
        try {
            // Load the image from file
            titleImage = ImageIO.read(new File("Final_Project_Example/assets/u.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        x = 400.0;
        y = 150.0;
        vx = 4.0;
        vy = 2.0;
    }

    public void update(int width, int height, double gravity) {
        // Apply gravity acceleration
        this.vy += gravity;

        // Apply velocity vector to position coordinates
        this.x += this.vx;
        this.y += this.vy;

        // Wall boundary collision checks with energy loss (0.75 damping)
        double damping = 0.85;
        double halfW = this.width / 2.0;
        double halfH = this.height / 3.0;
        if (this.x - halfW < 0) {
            this.x = halfW;
            this.vx *= -damping;
        } else if (this.x + halfW > width) {
            this.x = width - halfW;
            this.vx *= -damping;
        }

        if (this.y - halfH < 0) {
            this.y = halfH;
            this.vy *= -damping;
        } else if (this.y + halfH > height) {
            this.y = height - halfH;
            this.vy *= -damping;
        }
    }

    // Interactive Force Vector: Push particle away from mouse cursor radius
    public void applyMouseRepulsion(int mouseX, int mouseY, double radiusForce, double strength) {
        double dx = this.x - mouseX;
        double dy = this.y - mouseY;
        double distSq = dx * dx + dy * dy;

        if (distSq < radiusForce * radiusForce && distSq > 0) {
            double dist = Math.sqrt(distSq);
            double force = (1.0 - dist / radiusForce) * strength;
            this.vx += (dx / dist) * force;
            this.vy += (dy / dist) * force;
        }
    }


    public void paintComponent(Graphics g)
    {
        // Draw centered around (x, y)
        int drawX = (int) (x - width / 2.0);
        int drawY = (int) (y - height / 2.0);
        if (titleImage != null)
        {
            g.drawImage(titleImage, drawX, drawY, width, height, null);
        }
        else {
            g.setColor(Color.GRAY);
            g.fillRect(drawX, drawY, width, height);
        }
    }
}
