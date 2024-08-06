import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import javax.swing.Timer;

public class SnakeGame extends JPanel implements ActionListener {

    private final int GRID_SIZE = 20;  // Size of the grid
    private final int TILE_SIZE = 30; // Size of each tile
    private final int WIDTH = GRID_SIZE * TILE_SIZE;  // Width of the game window
    private final int HEIGHT = GRID_SIZE * TILE_SIZE; // Height of the game window

    private final int[] x = new int[GRID_SIZE * GRID_SIZE];
    private final int[] y = new int[GRID_SIZE * GRID_SIZE];
    private int length;
    private int foodX;
    private int foodY;
    private int direction;
    private boolean running;
    private Timer timer;

    public SnakeGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_LEFT && direction != 1) {
                    direction = 3;
                } else if (key == KeyEvent.VK_RIGHT && direction != 3) {
                    direction = 1;
                } else if (key == KeyEvent.VK_UP && direction != 2) {
                    direction = 0;
                } else if (key == KeyEvent.VK_DOWN && direction != 0) {
                    direction = 2;
                }
            }
        });

        initGame();
    }

    private void initGame() {
        length = 3;
        for (int i = 0; i < length; i++) {
            x[i] = (GRID_SIZE / 2) - i;
            y[i] = GRID_SIZE / 2;
        }

        direction = 1;
        running = true;

        spawnFood();

        timer = new Timer(100, this); // Timer to update the game state
        timer.start();
    }

    private void spawnFood() {
        foodX = (int) (Math.random() * GRID_SIZE);
        foodY = (int) (Math.random() * GRID_SIZE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkCollision();
            checkFood();
            repaint();
        }
    }

    private void move() {
        for (int i = length - 1; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case 0: y[0]--; break;
            case 1: x[0]++; break;
            case 2: y[0]++; break;
            case 3: x[0]--; break;
        }
    }

    private void checkCollision() {
        if (x[0] < 0 || x[0] >= GRID_SIZE || y[0] < 0 || y[0] >= GRID_SIZE) {
            running = false;
        }

        for (int i = length; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) {
                running = false;
            }
        }
    }

    private void checkFood() {
        if (x[0] == foodX && y[0] == foodY) {
            length++;
            spawnFood();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        if (running) {
            g.setColor(Color.RED);
            g.fillRect(foodX * TILE_SIZE, foodY * TILE_SIZE, TILE_SIZE, TILE_SIZE);

            for (int i = 0; i < length; i++) {
                if (i == 0) {
                    g.setColor(Color.GREEN); // Head
                } else {
                    g.setColor(Color.YELLOW); // Body
                }
                g.fillRect(x[i] * TILE_SIZE, y[i] * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }

            g.setColor(Color.WHITE);
            g.setFont(new Font("Helvetica", Font.BOLD, 14));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + (length - 3), WIDTH / 2 - metrics.stringWidth("Score: " + (length - 3)) / 2, g.getFont().getSize());
        } else {
            gameOver(g);
        }
    }

    private void gameOver(Graphics g) {
        String msg = "Game Over";
        g.setColor(Color.WHITE);
        g.setFont(new Font("Helvetica", Font.BOLD, 20));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString(msg, WIDTH / 2 - metrics.stringWidth(msg) / 2, HEIGHT / 2);
    }

    public static void main(String[] args) {
        javax.swing.JFrame frame = new javax.swing.JFrame();
        SnakeGame snakeGame = new SnakeGame();
        frame.add(snakeGame);
        frame.pack();
        frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
