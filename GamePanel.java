package Final_Project_Example;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;

public class GamePanel extends JPanel implements ActionListener {
    private final boolean[] keys = new boolean[256];
    private Player player;
    private final Timer timer;
    private ArrayList<Coin> coins = new ArrayList<>();
    private int score = 0;
    private ArrayList<Hazard> hazards = new ArrayList<>();
    private double hazardSpeed = 1;

    public enum GameState {MENU, PLAYING, GAME_OVER, WIN}
    private GameState currentState = GameState.MENU;
    private static int WIDTH = 800;
    private static int HEIGHT = 600;
    private Image backgroundImage;
    private Title t = new Title();
    private long startTime;
    private final double GRAVITY = 0.25;
    private int mouseX = -1000;
    private int mouseY = -1000;

    // constructor 
    public GamePanel()
    {
        // add background image
        try {
            // Load the image from file
            backgroundImage = ImageIO.read(new File("Final_Project_Example/assets/background.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(new Color(154, 247, 100));
        setFocusable(true); // required for keyboard inputs

        resetGame();

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();
                if (code < keys.length)
                    keys[code] = true;

                if (currentState == GameState.MENU && code == KeyEvent.VK_ENTER)
                    currentState = GameState.PLAYING;
                else if ((currentState == GameState.GAME_OVER || currentState == GameState.WIN) && code == KeyEvent.VK_R)
                {
                    resetGame();
                    currentState = GameState.PLAYING;
                }
            }

            public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() < keys.length) 
                    keys[e.getKeyCode()] = false;
            }
        }
        );

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
            }
        });
        
        timer = new Timer(16,this);
        timer.start();
        
    }

    private void resetGame() {
        player = new Player(400,300,"Final_Project_Example/assets/sprite.png");
        score = 0;
        // spawns coins
        coins.clear();
        for (int i = 0; i < 8; i++)
        {
            double rx = Math.random() * 800;
            double ry = Math.random() * 600;
            coins.add(new Coin(rx,ry));
        }
        // spawn hazards
        hazards.clear();
        hazardSpeed = 1;
        for (int i = 0; i < 8; i++)
        {
            double rx = Math.random() * WIDTH;
            double ry = Math.random() * HEIGHT;
            hazards.add(new Hazard(rx,ry,hazardSpeed));
        }

        startTime = System.currentTimeMillis();
    }

    public void actionPerformed(ActionEvent e)
    {
        if (currentState == GameState.MENU) {
            t.applyMouseRepulsion(mouseX, mouseY, 200.0, 2.5);
            t.update(WIDTH, HEIGHT, GRAVITY); 
        } 
        else if (currentState == GameState.PLAYING) {
            player.update(keys, GRAVITY, HEIGHT, WIDTH);
            for (int i = 0; i < hazards.size(); i++) 
            {
                hazards.get(i).update(player);
            }

            // avoid collisions of hazards
            for (int i = 0; i < hazards.size(); i++) {
                for (int j = i+1; j < hazards.size(); j++) {
                    hazards.get(i).handleCollisions(hazards.get(j));
                }
            }

            // change hazard speed over time
            long now = System.currentTimeMillis();
            long elapsed = now - startTime;
            if (elapsed >= (long) 10000)
            {
                hazardSpeed += 0.5;
                hazards.add(new Hazard(Math.random() * WIDTH, Math.random() * HEIGHT, hazardSpeed));   
                for (int i = 0; i < hazards.size(); i++) {
                    hazards.get(i).setSpeed(hazardSpeed);
                }
                startTime = now;
            }

            // collect coins
            for (int i = coins.size()-1; i >= 0; i--)
            {
                if (player.getBounds().intersects(coins.get(i).getBounds()))
                {
                    coins.remove(i);
                    score += 10;
                }
            }
        
            // respawn coins
            if (coins.size() <8 )
                coins.add(new Coin(Math.random() * WIDTH, Math.random() * HEIGHT));

            // despawn hazards
            for (int i = hazards.size()-1; i >= 0; i--)
            {
                if (player.getBounds().intersects(hazards.get(i).getBounds()))
                {
                    hazards.remove(i);
                    score -= 5;
                }
            }

            // end condition
            if (score < 0)
            {
                currentState = GameState.GAME_OVER;
            }
            
            // win condition 
            if (score >= 100)
            {
                currentState = GameState.WIN;
            }
            
        }

        
        repaint(); 
    }

    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        switch (currentState) {
            case MENU :
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, WIDTH, HEIGHT, this);
                }
                t.paintComponent(g);
                
                g.setColor(Color.RED);
                g.drawString("Please press enter to start game",300,300);
                break;
            case PLAYING :
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, WIDTH, HEIGHT, this);
                }
                player.draw(g);

                for (int i = 0; i < coins.size(); i++)
                {
                    coins.get(i).draw(g);
                }

                for (int i = 0; i < hazards.size(); i++)
                {
                    hazards.get(i).draw(g);
                }


                g.setColor(Color.BLUE);
                g.setFont(new Font("Arial",Font.BOLD,35));
                g.drawString("Score: "+score,20,35);

                
                break;
            
            case GAME_OVER :
                g.setColor(Color.RED);
                g.drawString("Please press R to restart game",300,300);
                break;

            case WIN :
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial",Font.BOLD,50));
                g.drawString("GOOD JOB!",275,250);
                g.setColor(Color.RED);
                g.setFont(new Font("Arial",Font.PLAIN,25));
                g.drawString("Please press R to restart game",250,300);
                break;
        }

        
    }
}
