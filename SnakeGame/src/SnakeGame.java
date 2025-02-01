import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class SnakeGame extends JPanel implements ActionListener {
    private final int WIDTH = 300; // عرض النافذة
    private final int HEIGHT = 300; // ارتفاع النافذة
    private final int DOT_SIZE = 10; // حجم كل نقطة في الثعبان
    private final int ALL_DOTS = 900; // الحد الأقصى لعدد النقاط
    private final int RAND_POS = 29; // لتحديد موقع عشوائي للفاكهة
    private final int DELAY = 140; // سرعة اللعبة

    private final int x[] = new int[ALL_DOTS]; // إحداثيات x للثعبان
    private final int y[] = new int[ALL_DOTS]; // إحداثيات y للثعبان

    private int dots; // طول الثعبان
    private int appleX; // إحداثيات x للفاكهة
    private int appleY; // إحداثيات y للفاكهة

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;

    private Timer timer;

    public SnakeGame() {
        initGame();
    }

    private void initGame() {
        setBackground(Color.black);
        setFocusable(true);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        addKeyListener(new TAdapter());

        dots = 3; // الطول الابتدائي للثعبان

        // تحديد المواقع الابتدائية للثعبان
        for (int i = 0; i < dots; i++) {
            x[i] = 50 - i * DOT_SIZE;
            y[i] = 50;
        }

        locateApple(); // وضع الفاكهة في مكان عشوائي

        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    private void doDrawing(Graphics g) {
        if (inGame) {
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, DOT_SIZE, DOT_SIZE); // رسم الفاكهة

            for (int i = 0; i < dots; i++) {
                if (i == 0) {
                    g.setColor(Color.green); // رأس الثعبان
                } else {
                    g.setColor(Color.green.darker()); // جسم الثعبان
                }
                g.fillRect(x[i], y[i], DOT_SIZE, DOT_SIZE); // رسم الثعبان
            }

            Toolkit.getDefaultToolkit().sync();
        } else {
            gameOver(g);
        }
    }

    private void gameOver(Graphics g) {
        String msg = "Game Over";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metrics = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (WIDTH - metrics.stringWidth(msg)) / 2, HEIGHT / 2);
    }

    private void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            dots++; // زيادة طول الثعبان
            locateApple(); // وضع فاكهة جديدة
        }
    }

    private void move() {
        for (int i = dots; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        if (leftDirection) {
            x[0] -= DOT_SIZE;
        }

        if (rightDirection) {
            x[0] += DOT_SIZE;
        }

        if (upDirection) {
            y[0] -= DOT_SIZE;
        }

        if (downDirection) {
            y[0] += DOT_SIZE;
        }
    }

    private void checkCollision() {
        for (int i = dots; i > 0; i--) {
            if ((i > 4) && (x[0] == x[i]) && (y[0] == y[i])) {
                inGame = false; // الاصطدام بالجسم
            }
        }

        if (y[0] >= HEIGHT || y[0] < 0 || x[0] >= WIDTH || x[0] < 0) {
            inGame = false; // الاصطدام بالجدران
        }

        if (!inGame) {
            timer.stop(); // إيقاف اللعبة
        }
    }

    private void locateApple() {
        int r = (int) (Math.random() * RAND_POS);
        appleX = r * DOT_SIZE;

        r = (int) (Math.random() * RAND_POS);
        appleY = r * DOT_SIZE;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            checkApple();
            checkCollision();
            move();
        }

        repaint();
    }

    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (!rightDirection)) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_UP) && (!downDirection)) {
                upDirection = true;
                leftDirection = false;
                rightDirection = false;
            }

            if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {
                downDirection = true;
                leftDirection = false;
                rightDirection = false;
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("لعبة الثعبان");
        SnakeGame game = new SnakeGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}