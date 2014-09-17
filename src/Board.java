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
import java.util.concurrent.TimeUnit;

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
    private int foodX;
    private int foodY;
    private int foodCounter = 0;
    private int bonusX;
    private int bonusY;
    private boolean getBonus = false;
    private long startTime = 0;
    private long gameStart = 0;

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;

    private long score = 0;
    
    private Timer timer;
    private Image body;
    private Image food;
    private Image head;
    private Image border;
    private Image bonus;

    public Board() {

        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(windowWidth, windowHeight));
        loadImages();
        whilePlaying();
        gameStart = System.nanoTime();
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

    private void whilePlaying() {

        dots = 3;

        for (int z = 0; z < dots; z++) {
            x[z] = 180 - z*DOT_SIZE;
            y[z] = 180;
        }

        locateFruit();

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

            g.drawImage(food, foodX, foodY, this);

            for (int z = 0; z < dots; z++) {
                if (z == 0) {
                    g.drawImage(head, x[z], y[z], this);
                } else {
                    g.drawImage(body, x[z], y[z], this);
                }
            }

            // draw information field
            for (int j = 1; j < 15; j++) {
            	for (int i = 0; i < windowWidth; i+=17) {
    				g.drawImage(border, i, windowHeight - gameAreaHeight - j*15, this);
    			}
			}
            
            
            if(getBonus == true){
            	
            	long endTime = System.currentTimeMillis();
            	int seconds = (int) TimeUnit.MILLISECONDS.toSeconds(endTime - startTime);
            	
            	if(seconds < 7){
            		
            		String msg = "GET BONUS !!! Time: " + (7 - seconds);
                    Font small = new Font("Arial", Font.BOLD, 20);
                    g.setColor(Color.red);
                    g.setFont(small);
                    g.drawString(msg, 370, 20);
            		
            		g.drawImage(bonus,bonusX,bonusY,this);
            	}
            	else{
            		getBonus = false;
            	}
            	
            }
            
            String msg = "Score: " + score;
            Font small = new Font("Arial", Font.BOLD, 18);
            g.setColor(Color.white);
            g.setFont(small);
            g.drawString(msg, 480, 70);
            
            Toolkit.getDefaultToolkit().sync();

        } else {

            gameOver(g);
        }        
        
        
       long time = System.nanoTime();
        int currentTime = (int) TimeUnit.NANOSECONDS.toSeconds(time - gameStart);
           String timer = "Timer: " + currentTime;
           Font timerFont = new Font("Arial", Font.BOLD, 18);
           g.setColor(Color.green);
           g.setFont(timerFont);
           g.drawString(timer, 50, 70);
        
    }

    private void gameOver(Graphics g) {
        
        String msg = "Game Over";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (gameAreaWidth - metr.stringWidth(msg)) / 2, gameAreaHeight / 2);
    }

    private void checkFruit() {

        if ((x[0] == foodX) && (y[0] == foodY )) {

            dots++;
            locateFruit();
            score += 50;
            foodCounter++;
            
            if(foodCounter == 3){
            	locateBonus();
            }
        } 
    }

    private void checkBonus(){
    	
    	if(getBonus == true && (x[0]  == bonusX && y[0] == bonusY)){

        	getBonus = false;
        	dots++;
        	score += 100;
        }
    	
    }
    
    private void locateBonus() {
    	
    	Random rand = new Random();
    	
    	ImageIcon icon = new ImageIcon("bonus.png");
    	bonus = icon.getImage();
    	
    	bonusX  = rand.nextInt(gameAreaWidth/DOT_SIZE) * DOT_SIZE;
    	bonusY  = rand.nextInt(gameAreaHeight/DOT_SIZE) * DOT_SIZE + (windowHeight - gameAreaHeight);
    	
    	while((bonusX == foodX) || (bonusY == foodY)){
    		bonusX  = rand.nextInt(gameAreaWidth/DOT_SIZE) * DOT_SIZE;
        	bonusY  = rand.nextInt(gameAreaHeight/DOT_SIZE) * DOT_SIZE + (windowHeight - gameAreaHeight);
    	}
    	
    	startTime = System.currentTimeMillis();
    	getBonus = true;
    	foodCounter = 0;
    	
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

    private void locateFruit() {

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
    	
    	foodX  = rand.nextInt(gameAreaWidth/DOT_SIZE) * DOT_SIZE;
    	foodY  = rand.nextInt(gameAreaHeight/DOT_SIZE) * DOT_SIZE + (windowHeight - gameAreaHeight) ;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (inGame) {

            checkCollision();
            checkBonus();
            checkFruit();
            move();
        }

        repaint();
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (!rightDirection) && (key != KeyEvent.VK_RIGHT) && (key != KeyEvent.VK_UP) && (key != KeyEvent.VK_DOWN)) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
                
                ImageIcon headIcon = new ImageIcon("headLeft.png");
                head = headIcon.getImage();
            }
            else if ((key == KeyEvent.VK_RIGHT) && (!leftDirection) && (key != KeyEvent.VK_LEFT) && (key != KeyEvent.VK_UP) && (key != KeyEvent.VK_DOWN)){
                rightDirection = true;
                upDirection = false;
                downDirection = false;
                
                ImageIcon headIcon = new ImageIcon("headRight.png");
                head = headIcon.getImage();
            }
            else if ((key == KeyEvent.VK_UP) && (!downDirection) && (key != KeyEvent.VK_LEFT) && (key != KeyEvent.VK_RIGHT) && (key != KeyEvent.VK_DOWN)) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
                
                ImageIcon headIcon = new ImageIcon("headUp.png");
                head = headIcon.getImage();
            }
            else if ((key == KeyEvent.VK_DOWN) && (!upDirection) && (key != KeyEvent.VK_LEFT) && (key != KeyEvent.VK_RIGHT) && (key != KeyEvent.VK_UP)) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
                
                ImageIcon headIcon = new ImageIcon("headDown.png");
                head = headIcon.getImage();
            }
        }
    }
}