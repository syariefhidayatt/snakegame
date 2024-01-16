import java.awt.*;
import javax.swing.*;


public class Main extends JFrame {
    public Main() {
        initUI(); //initializing UI
    }

    private void initUI() {
        add(new Game());

        //window properties
        setTitle("Snake game");
        setSize(800, 610);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            Main ex = new Main();
            ex.setVisible(true);
        });
    }
}