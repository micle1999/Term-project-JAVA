package GameState;

import java.awt.*;
import java.awt.event.KeyEvent;

import TileMap.BackGround;


public class MenuState extends GameState {
	
	private BackGround bg;
	
	private int currChoice = 0;
	
	private Color titleColor; 
	
	private Font titleFont;
	
	private Font font;
	
	private String[] options = {
			"Start Game",
			"Help",
			"Quit"
	};
	
	public MenuState(GameStateManager manager) {
		
		this.manager = manager;
		
		try {
			
			bg = new BackGround("/BackGround/space.gif", 1);
			bg.setVector(-0.3, 0);
			titleColor = new Color(255,250,220);
			titleFont = new Font("Century Gothic", Font.PLAIN,28);
			font = new Font("Arial", Font.PLAIN, 12);
					
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	@Override
	public void init() {}
	
	@Override
	public void update() { bg.update(); }
	
	@Override
	public void draw(java.awt.Graphics2D g) { 
		bg.draw(g);
		g.setColor(titleColor);
		g.setFont(titleFont);
		g.drawString("BattleBots",100,70);
		g.setFont(font);
		for(int i = 0; i < options.length; i++) {
			if(i == currChoice) {
				g.setColor(Color.CYAN);
			}
			else {
				g.setColor(Color.WHITE);
			}
			g.drawString(options[i], 135, 120 + i * 12);
		}
	}
	
	public void select() {
		if(currChoice == 0) {
			manager.setState(GameStateManager.IN_GAME_STATE);
		}
		
		if(currChoice == 1) {
			
		}
		
		if(currChoice == 2) {
			System.exit(0);
		}
	}
	
	@Override
	public void keyPressed(int k) {
		if(k == KeyEvent.VK_ENTER) {
			select();
		}
		
		if(k == KeyEvent.VK_UP) {
			currChoice --;
			if(currChoice == -1) {
				currChoice = options.length -1;
			}
		}
		
		if(k == KeyEvent.VK_DOWN) {
			currChoice ++;
			if(currChoice == options.length) {
				currChoice = 0;
			}
		}
	}
	
	@Override
	public void keyReleased(int k) {}
}
