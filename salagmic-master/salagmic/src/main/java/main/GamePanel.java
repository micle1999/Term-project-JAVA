package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import GameState.GameStateManager;

import GameState.GameStateManager;


/**
 * 
 * @author michalsalaga
 * class that initialized JPanel for a game ,
 * create thread for the game and method run maintain rendering 
 * of the game.
 */
public class GamePanel extends JPanel implements Runnable,KeyListener {
	
	//======STATIC VARIABLES======//
	public static int WIDTH = 330;
	public static int HEIGHT = 240;
	public static int SCALE = 2;
	
	
	//======PRIVATE CLASS VARIABLES======//
	private Thread thread;
	private boolean running;
	private int FPS = 60;
	private long targetTime = 1000/FPS;
	
	private BufferedImage image;
	private Graphics2D g;
	
	private GameStateManager manager;
	
	/**
	 * class constructor
	 */
	public GamePanel() {
		super();
		setPreferredSize(new Dimension(WIDTH * SCALE , HEIGHT * SCALE));
		setFocusable(true);
		requestFocus();
	}
	
	/**
	 * Notifies this component that it now has a parent component. 
	 * When this method is invoked, the chain of parent components 
	 * is set up with KeyboardAction event listeners. 
	 * This method is called by the toolkit internally and 
	 * should not be called directly by programs.
	 */
	public void addNotify() {
		super.addNotify();
		if(thread == null) {
			thread = new Thread(this);
			addKeyListener(this);
			thread.start();
		}
	}
	
	/**
	 * function for initialization of BufferedImage
	 */
	public void init() {
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		running = true;
		manager = new GameStateManager();
	}
	
	
	/**
	 * Function that maintain rendering 
	 */
	public void run() {
		init();
		
		long start;
		long elapsed;
		long wait;
		
		//game loop
		while(running) {
			
			start = System.nanoTime();
			
			update();
			draw();
			drawToScreen();
			
			elapsed = System.nanoTime() - start;
			wait = targetTime - elapsed / 1000000;
			
			if(wait < 0) {
				wait = 5;
			}
			
			try {
				thread.sleep(wait);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * Use Graphics to draw actual image on the screen
	 */
	private void drawToScreen() {
		Graphics g2 = getGraphics();
		g2.drawImage(image, 0, 0,
		WIDTH * SCALE, HEIGHT * SCALE,
		null);
		g2.dispose();
	}

	private void draw() {
		manager.draw(g);
	}

	private void update() {
		manager.update();
	}

	public void keyTyped(KeyEvent key) {
		
	}
	
	public void keyPressed(KeyEvent key) {
		manager.keyPressed(key.getKeyCode());
	}

	public void keyReleased(KeyEvent key) {
		manager.keyReleased(key.getKeyCode());
	}
	
}
