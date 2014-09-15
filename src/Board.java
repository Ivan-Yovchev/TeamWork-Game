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

    private final int gameAreaWidth = 600;
    private final int gameAreaHeight = 600;
    private final int windowWidth = 600;
    private final int windowHeight = 700;
    private final int DOT_SIZE = 20;
    private final int ALL_DOTS = 900;
    private final int speed = 200;

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
    private Image border;

    public Board() {

        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(windowWidth, windowHeight));
        loadImages();
        initGame();
    }

    private void loadImages() {

        ImageIcon bImg = new ImageIcon("body.png");
        body = bImg.getImage();

        ImageIcon fImg = new ImageIcon("apple.png");
        food = fImg.getImage();

        ImageIcon hImg = new ImageIcon("headRight.png");
        head = hImg.getImage();
        
        ImageIcon borderImg = new ImageIcon("border.png");
        border = borderImg.getImage();
    }

    private void initGame() {

        dots = 3;

        for (int z = 0; z < dots; z++) {
            x[z] = 180 - z*DOT_SIZE;
            y[z] = 180;
        }

        locateApple();

        timer = new Timer(speed, this);
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

            for (int i = 0; i < windowWidth; i++) {
				g.drawImage(border, i, windowHeight - gameAreaHeight - 20, this);
			}
            
            g.setColor(Color.red);
            g.drawString("Draw game Info board here", 20, 40);
            
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
        g.drawString(msg, (gameAreaWidth - metr.stringWidth(msg)) / 2, gameAreaHeight / 2);
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

        if (y[0] == (windowHeight - gameAreaHeight) + gameAreaHeight) {
            //inGame = false;
        	y[0] = windowHeight - gameAreaHeight;
        }

        if (y[0] < windowHeight - gameAreaHeight) {
            //inGame = false;
        	y[0] = (windowHeight - gameAreaHeight) + gameAreaHeight;
        }

        if (x[0] >= gameAreaWidth) {
            //inGame = false;
        	x[0] = 0;
        }

        if (x[0] < 0) {
            //inGame = false;
        	x[0] = gameAreaWidth;
        }
        
        if(!inGame) {
            timer.stop();
        }
    }

    private void locateApple() {

    	Random rand = new Random();
    	
    	int choosePicture = rand.nextInt(6);
    	ImageIcon icon;
    	
    	switch (choosePicture) {
    		case 0: icon = new ImageIcon("lemon.png"); food = icon.getImage(); break;
    		case 1: icon = new ImageIcon("cherry.png"); food = icon.getImage(); break;
    		case 2: icon = new ImageIcon("pear.png"); food = icon.getImage(); break;
    		case 3: icon = new ImageIcon("banana.png"); food = icon.getImage(); break;
    		case 4: icon = new ImageIcon("apple.png"); food = icon.getImage(); break;
    		case 5: icon = new ImageIcon("strawberry.png"); food = icon.getImage(); break;
		}
    	
    	apple_x  = rand.nextInt(gameAreaWidth/DOT_SIZE) * DOT_SIZE;
    	apple_y  = rand.nextInt(gameAreaHeight/DOT_SIZE) * DOT_SIZE + (windowHeight - gameAreaHeight) ;
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
                
                ImageIcon headIcon = new ImageIcon("headLeft.png");
                head = headIcon.getImage();
            }
            else if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
                
                ImageIcon headIcon = new ImageIcon("headRight.png");
                head = headIcon.getImage();
            }
            else if ((key == KeyEvent.VK_UP) && (!downDirection)) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
                
                ImageIcon headIcon = new ImageIcon("headUp.png");
                head = headIcon.getImage();
            }
            else if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
                
                ImageIcon headIcon = new ImageIcon("headDown.png");
                head = headIcon.getImage();
            }
        }
    }
}