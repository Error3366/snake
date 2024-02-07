package org.cis1200.snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * GameCourt
 *
 * This class holds the primary game logic for how different objects interact
 * with one another. Take time to understand how the timer interacts with the
 * different methods and how it repaints the GUI on every tick().
 */
public class GameCourt extends JPanel {

    // the state of the game logic
    private GameState gameState = new GameState();
    private boolean playing = false; // whether the game is running
    private final JLabel status; // Current status text, i.e. "Running..."
    private Direction d;

    // Game constants
    public static final int COURT_WIDTH = 750;
    public static final int COURT_HEIGHT = 800;

    // Update interval for timer, in milliseconds
    public static final int INTERVAL = 175;

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public GameCourt(JLabel status) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // The timer is an object which triggers an action periodically with the
        // given INTERVAL. We register an ActionListener with this timer, whose
        // actionPerformed() method is called each time the timer triggers. We
        // define a helper method called tick() that actually does everything
        // that should be done in a single time step.
        Timer timer = new Timer(INTERVAL, e -> tick());
        timer.start(); // MAKE SURE TO START THE TIMER!

        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        // This key listener allows the square to move as long as an arrow key
        // is pressed, by changing the square's velocity accordingly. (The tick
        // method below actually moves the square.)
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    d = Direction.LEFT;
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    d = Direction.RIGHT;
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    d = Direction.DOWN;
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    d = Direction.UP;
                }
            }
        });

        this.status = status;
    }

    /**
     * (Re-)set the game to its initial state.
     */
    public void reset() {
        gameState = new GameState();
        d = null;

        playing = true;
        status.setText("Running...");

        // Make sure that this component has the keyboard focus
        requestFocusInWindow();
    }

    /**
     * This method is called every time the timer defined in the constructor
     * triggers.
     */
    void tick() {
        if (playing && gameState.isAlive()) {
            // advance the square and snitch in their current direction.
            gameState.moveSnakeHead(d);

            // check for the game end conditions
            if (!gameState.isAlive()) {
                playing = false;
                status.setText("SNEK = DED");
            }

            // update the display
            repaint();
        }
    }

    public void save() {
        playing = false;
        d = null;
        gameState.setStartGame(false);

        if (gameState.isAlive()) {
            gameState.saveState();
            final JFrame saved = new JFrame();
            JOptionPane.showMessageDialog(saved, "SAVED!");
        } else {
            final JFrame errorSaved = new JFrame();
            JOptionPane.showMessageDialog(errorSaved, "CANNOT SAVE WHAT'S ALREADY DEAD :(");
        }

        requestFocusInWindow();
    }

    public void load() {
        playing = true;
        status.setText("Running...");
        d = null;
        gameState.setStartGame(false);
        if (!gameState.testLoadState()) {
            final JFrame errorLoad = new JFrame();
            JOptionPane.showMessageDialog(errorLoad, "FILE DID NOT LOAD CORRECTLY!");
        } else {
            gameState = new GameState();
            gameState.loadState();
            final JFrame loaded = new JFrame();
            JOptionPane.showMessageDialog(loaded, "LOADED!");
        }
        requestFocusInWindow();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draws board grid
        for (int i = 50; i <= 750; i += 50) {
            g.drawLine(0, i, 750, i);
            g.drawLine(i, 0, i, 750);
        }

        // Draws snake body
        for (int[] coor : gameState.getSnakePath()) {
            g.setColor(Color.BLACK);
            g.drawRect(GameState.convertInt(coor[0]), GameState.convertInt(coor[1]), 44, 44);
            g.setColor(Color.pink);
            g.fillRect(
                    GameState.convertInt(coor[0]) + 7, GameState.convertInt(coor[1]) + 7, 30, 30
            );
        }

        // Draws snake head
        if (gameState.isAlive()) {
            g.setColor(Color.green);
        } else {
            g.setColor(Color.red);
        }

        g.fillRect(
                GameState.convertInt(gameState.getSnakeHead()[0]) + 5,
                GameState.convertInt(gameState.getSnakeHead()[1]) + 5, 35, 35
        );

        // Draws fruit
        if (gameState.getFruit().getClass().getSimpleName().equals("Fruit")) {
            g.setColor(Color.cyan);
        } else if (gameState.getFruit().getClass().getSimpleName().equals("SideFruit")) {
            g.setColor(Color.blue);
        } else {
            g.setColor(Color.yellow);
        }

        g.fillRect(
                GameState.convertInt(gameState.getFruit().getLoc()[0]) + 5,
                GameState.convertInt(gameState.getFruit().getLoc()[1]) + 5, 35, 35
        );

        g.setColor(Color.BLACK);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 35));
        g.drawString("Score: " + String.valueOf(gameState.getScore()), 15, 785);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }
}