package main;

import javax.swing.JFrame;

/**
 * 
 * @author michalsalaga
 * main clas for initializating game
 * setting main frame for the game 
 */
public class Game {
	
	public static void main(String[] args) {
		JFrame window = new JFrame("Battle Bots");
		window.setContentPane(new GamePanel());
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.pack();
		window.setVisible(true);
		
		
	}
	
}
