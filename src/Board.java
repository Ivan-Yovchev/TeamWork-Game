import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Board extends JPanel implements ActionListener {

    private final int B_WIDTH = 450;
    private final int B_HEIGHT = 450;
    private final int DOT_SIZE = 15;
    private final int ALL_DOTS = 900;
    private final int DELAY = 150;

    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS];

    private int dots;
    private int apple_x;
    private int apple_y;

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;

    private Timer timer;
    private Image body;
    private Image food;
    private Image head;

    public Board() {

        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        loadImages();
        initGame();
    }

    private void loadImages() {

        ImageIcon iid = new ImageIcon("body.png");
        body = iid.getImage();

        ImageIcon iia = new ImageIcon("apple.png");
        food = iia.getImage();

        ImageIcon iih = new ImageIcon("head.png");
        head = iih.getImage();
    }

    private void initGame() {

        dots = 3;

        for (int z = 0; z < dots; z++) {
            x[z] = 75 - z*DOT_SIZE;
            y[z] = 75;
        }

        locateApple();

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

            g.drawImage(food, apple_x, apple_y, this);

            for (int z = 0; z < dots; z++) {
                if (z == 0) {
                    g.drawImage(head, x[z], y[z], this);
                } else {
                    g.drawImage(body, x[z], y[z], this);
                }
            }

            Toolkit.getDefaultToolkit().sync();

        } else {

            gameOver(g);
        }        
    }

    private void gameOver(Graphics g) {
        
        String msg = "Game Over";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2);
    }

    private void checkApple() {

        if ((x[0] == apple_x) && (y[0] == apple_y )) {

            dots++;
            locateApple();
        }
//        else {
//			System.out.println("apple x: " + apple_x + " apple y: "+apple_y);
//			System.out.println("snake x: " + x[0] + " snake y: " + y[0]);
//		}
    }

    private void move() {

        for (int z = dots; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
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

        for (int z = dots; z > 0; z--) {

            if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
                inGame = false;
            }
        }

        if (y[0] >= B_HEIGHT) {
            //inGame = false;
        	y[0] = 0;
        }

        if (y[0] < 0) {
            //inGame = false;
        	y[0] = B_HEIGHT;
        }

        if (x[0] >= B_WIDTH) {
            //inGame = false;
        	x[0] = 0;
        }

        if (x[0] < 0) {
            //inGame = false;
        	x[0] = B_WIDTH;
        }
        
        if(!inGame) {
            timer.stop();
        }
    }

    private void locateApple() {

    	Random rand = new Random();
    	
    	int choosePicture = rand.nextInt(3);
    	ImageIcon icon;
    	
    	switch (choosePicture) {
    		case 0: icon = new ImageIcon("apple.png"); food = icon.getImage(); break;
    		case 1: icon = new ImageIcon("cherry.png"); food = icon.getImage(); break;
    		case 2: icon = new ImageIcon("pear.png"); food = icon.getImage(); break;
		}
    	
    	apple_x  = rand.nextInt(B_WIDTH/15) * 15;
    	apple_y  = rand.nextInt(B_HEIGHT/15) * 15;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (inGame) {

            checkCollision();
            checkApple();
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
                rightDirection = false;
            }
            else if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
                leftDirection = false;
            }
            else if ((key == KeyEvent.VK_UP) && (!downDirection)) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
                downDirection = false;
            }
            else if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
                upDirection = false;
            }
        }
    }
}