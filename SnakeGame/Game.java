import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.Timer;

class Game extends JPanel {
    //declaring variables
    private java.util.Timer timer;
    private Snake snake;
    private Point apple;
    private int points = 0; //points earned in round
    private final int[] best = new int[3]; //best score in each difficulty
    private GameStatus status;
    private final String[] Difficulty = new String[]{"EASY", "MEDIUM", "HARD"};
    private int diff = 1;// difficulty set to medium
    private static int DELAY = 50;//speed set to medium


    //fonts used
    private static final Font FONT_M = new Font("Serif", Font.PLAIN, 24);
    private static final Font FONT_M_ITALIC = new Font("Serif", Font.ITALIC, 24);
    private static final Font FONT_L = new Font("Serif", Font.PLAIN, 84);
    private static final Font FONT_XL = new Font("SansSerif", Font.PLAIN, 150);

    //size of board
    private static final int WIDTH = 760;
    private static final int HEIGHT = 520;


    // Constructor
    public Game() {

        addKeyListener(new KeyListener());
        setFocusable(true);
        setBackground(Color.BLACK);
        setDoubleBuffered(true);

        snake = new Snake(WIDTH / 2, HEIGHT / 2);
        status = GameStatus.NOT_STARTED;
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        render(g);

        Toolkit.getDefaultToolkit().sync();
    }

    // Render the game
    private void update() {
        snake.move();

        if (apple != null && snake.getHead().intersects(apple, 20)) {
            snake.addTail();
            apple = null;
            points++;
        }
        if (apple == null) {
            spawnApple();
        }

        checkForGameOver();
    }
//reset the game
    private void reset() {
        points = 0;
        apple = null;
        snake = new Snake(WIDTH / 2, HEIGHT / 2);
        setStatus(GameStatus.RUNNING);
    }
//check status of the game
    private void setStatus(GameStatus newStatus) {
        switch (newStatus) {
            case RUNNING:
                //if game started then begin the timer with set speed according to difficulty
                timer = new Timer();
                timer.schedule(new GameLoop(), 0, DELAY);
                break;
            case PAUSED:
                //pause the timer
                timer.cancel();
            case GAME_OVER:
                //stop timer and check the best score
                timer.cancel();
                best[diff] = Math.max(points, best[diff]);
                points = 0;
                break;
        }
        //update status of the game
        status = newStatus;
    }

    private void togglePause() {
        //logistics of pausing and unpausing the game
        setStatus(status == GameStatus.PAUSED ? GameStatus.RUNNING : GameStatus.PAUSED);
        paintComponent(getGraphics());
    }

    // Check if the snake has hit the wall or itself
    private void checkForGameOver() {
        Point head = snake.getHead();
        boolean hitBoundary = head.getX() <= 20
                || head.getX() >= WIDTH + 10
                || head.getY() <= 40
                || head.getY() >= HEIGHT + 30;

        boolean ateItself = false;

        for (Point t : snake.getTail()) {
            ateItself = ateItself || head.equals(t);
        }

        if (hitBoundary || ateItself) {
            setStatus(GameStatus.GAME_OVER);
        }
    }

    // Spawn an apple at a random location
    public void drawCenteredString(Graphics g, String text, Font font, int y) {
        FontMetrics metrics = g.getFontMetrics(font);
        int x = (WIDTH - metrics.stringWidth(text)) / 2;
        g.setFont(font);
        g.drawString(text, x, y);
    }


//rendering display of game
    private void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        //logic of displaying text before game started
        g2d.setColor(Color.BLACK);
        g2d.setFont(FONT_M);
        if (status == GameStatus.NOT_STARTED) {
            g2d.setColor(Color.WHITE);
            drawCenteredString(g2d, "SNAKE", FONT_XL, 200);
            drawCenteredString(g2d, "GAME", FONT_XL, 300);
            drawCenteredString(g2d, "Press  any  key  to  begin", FONT_M_ITALIC, 330);
            drawCenteredString(g2d, "Difficulty: " + Difficulty[diff], FONT_M, 470);
            drawCenteredString(g2d, "press 1,2,3 to change difficulty", FONT_M_ITALIC, 500);

            return;
        }


        g2d.setColor(Color.WHITE);
        //logic of displaying text while playing
        g2d.drawString("SCORE: " + String.format("%02d", points), 20, 30);
        g2d.drawString("BEST: " + String.format("%02d", best[diff]), 650, 30);
        drawCenteredString(g2d, "Difficulty: " + Difficulty[diff], FONT_M, 30);

        //display apple
        if (apple != null) {
            g2d.setColor(Color.YELLOW);
            g2d.fillOval(apple.getX(), apple.getY(), 15, 15);
            g2d.setColor(Color.YELLOW);
        }
        //display snake
        Point p = snake.getHead();
        g2d.setColor(new Color(33, 70, 199));
        g2d.fillRect(p.getX(), p.getY(), 10, 10);
        for (int i = 0, size = snake.getTail().size(); i < size; i++) {
            Point t = snake.getTail().get(i);
            g2d.fillRect(t.getX(), t.getY(), 10, 10);
        }

        //display text after losing
        if (status == GameStatus.GAME_OVER) {
            g2d.setColor(Color.WHITE);

            drawCenteredString(g2d, "Press  enter  to  start  again", FONT_M_ITALIC, 330);
            drawCenteredString(g2d, "GAME OVER", FONT_L, 300);

            drawCenteredString(g2d, "Difficulty: " + Difficulty[diff], FONT_M, 470);
            drawCenteredString(g2d, "press 1,2,3 to change difficulty", FONT_M_ITALIC, 500);
        }

        //display text when pausing
        if (status == GameStatus.PAUSED) {
            g2d.setColor(Color.WHITE);
            drawCenteredString(g2d, "Paused", FONT_L, 300);
        }

        //display borders
        g2d.setColor(Color.RED);
        g2d.setStroke(new BasicStroke(4));
        g2d.drawRect(12, 40, WIDTH, HEIGHT);
    }

    // spawn an apple in random position
    public void spawnApple() {
        apple = new Point((new Random()).nextInt(WIDTH - 60) + 20,
                (new Random()).nextInt(HEIGHT - 60) + 40);
        if (snake.getTail().contains(apple)) {
            spawnApple();
        }
        if (snake.getHead().getX() == apple.getX() && snake.getHead().getY() == apple.getY()) {
            spawnApple();
        }
    }

    // game loop
    private class KeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            //controls for the snake
            if (status == GameStatus.RUNNING) {
                switch (key) {
                    case KeyEvent.VK_LEFT -> snake.turn(Direction.LEFT);
                    case KeyEvent.VK_RIGHT -> snake.turn(Direction.RIGHT);
                    case KeyEvent.VK_UP -> snake.turn(Direction.UP);
                    case KeyEvent.VK_DOWN -> snake.turn(Direction.DOWN);
                }
            }

            if (status == GameStatus.NOT_STARTED) {
            //check if setting difficulty or starting game
                if ("123".indexOf(key) >= 0) {
                    setDifficulty(key);
                } else
                    setStatus(GameStatus.RUNNING);

                //pause
            } else if (key == KeyEvent.VK_SPACE) {
                // g2d.drawString("Paused", 600, 14);
                if (status == GameStatus.RUNNING || status == GameStatus.PAUSED) {
                    togglePause();
                }
            }

            //check if setting difficulty or restarting
            if (status == GameStatus.GAME_OVER) {
                if ("123".indexOf(key) >= 0) {
                    setDifficulty(key);
                } else if (key == KeyEvent.VK_ENTER)
                    reset();
            }


        }
    }

    //setting the difficulty and according speed
    private void setDifficulty(int key) {
        switch (key) {
            case '1' -> {
                DELAY = 100;
                diff = 0;
                repaint();
            }
            case '2' -> {
                DELAY = 50;
                diff = 1;
                repaint();
            }
            case '3' -> {
                DELAY = 25;
                diff = 2;
                repaint();
            }
        }
    }

    private class GameLoop extends java.util.TimerTask {
        public void run() {
            update();
            repaint();
        }
    }
}
