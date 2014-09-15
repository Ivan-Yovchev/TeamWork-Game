import java.awt.EventQueue;

import javax.swing.JFrame;


@SuppressWarnings("serial")
public class Application extends JFrame {

    public Application() {

        add(new Board());
        
        setResizable(false);
        pack();
        
        setTitle("Snakes");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    

    public static void main(String[] args) {
        
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {                
                JFrame ex = new Application();
                ex.setVisible(true);                
            }
        });
    }
}
