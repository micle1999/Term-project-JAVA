package GameState;


import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import TileMap.BackGround;
import TileMap.TileMap;
import entities.BattleBot;
import entities.Direction;
import entities.Enemy;
import entities.Explosion;
import entities.HUD;
import entities.Wave;

public class InGameState extends GameState{
	
	private TileMap tileMap;
	
	private boolean enemyExploded = false;
	private boolean playerExploded = false;
	
	private BattleBot player;
	private Enemy enemy;
	private HUD playerHud;
	private HUD enemyHud;
	private ArrayList<Explosion> explosions;
	
	
	public InGameState(GameStateManager manager) {
		this.manager = manager;
		init();
	}

	@Override
	public void init() {
		tileMap = new TileMap(30);
		tileMap.loadTiles("/Arena_textures/my_tileset.gif");
		tileMap.loadMap("/map/test.map");
		tileMap.setPosition(0, 0);
		player = new BattleBot(tileMap);
		enemy = new Enemy(tileMap);
		playerHud = new HUD(0,0,player);
		enemyHud = new HUD(270,210,enemy);
		
		
		
		//find out the position of actual player starting position 
		player.setPosition(165, 40);
		enemy.setPosition(165, 120);
		
		explosions = new ArrayList<Explosion>();
		
		
	}

	@Override
	public void update() {
		
		player.update();
		enemy.update();
		System.out.println("num of waves : " + enemy.getWaves());
		//attack enemies
		player.checkAttack(enemy);
		enemy.checkWaveAttack(player);

		if(enemy.isDead() == true && enemyExploded == false) {
			
			explosions.add(new Explosion(enemy.getx(),enemy.gety()));
			enemyExploded = true;
		}
		
		if(player.isDead() == true && playerExploded == false) {
			
			explosions.add(new Explosion(player.getx(),player.gety()));
			playerExploded = true;
		}
		
		//update explosions
		for(int i = 0; i < explosions.size(); i++) {
			explosions.get(i).update();
			if(explosions.get(i).shouldRemove()) {
				explosions.remove(i);
				i--;
			}
		}
	
	}

	@Override
	public void draw(Graphics2D g) {
		
		
		//draw tileMap
		tileMap.draw(g);
		
		//draw player
		player.draw(g);
		
		//draw enemy
		enemy.draw(g);
		
		//draw HUD
		playerHud.draw(g);
		enemyHud.draw(g);
		
		//draw explosion
		for(int i = 0 ; i < explosions.size(); i++) {
			Explosion e = explosions.get(i);
			e.draw(g);
		}
	}

	@Override
	public void keyPressed(int k) {
		if( k == KeyEvent.VK_LEFT) {
			player.setLeft(true);
		}
		else if( k == KeyEvent.VK_RIGHT) {
			player.setRight(true);
		}
		else if( k == KeyEvent.VK_UP) {
			player.setUp(true);
		}
		else if( k == KeyEvent.VK_DOWN) {
			player.setDown(true);
		}
		else if( k == KeyEvent.VK_SPACE) {
			player.setFiring();
		}
		
	}

	@Override
	public void keyReleased(int k) {
		if( k == KeyEvent.VK_LEFT) {
			player.setLeft(false);
		}
		else if( k == KeyEvent.VK_RIGHT) {
			player.setRight(false);
		}
		else if( k == KeyEvent.VK_UP) {
			player.setUp(false);
		}
		else if( k == KeyEvent.VK_DOWN) {
			player.setDown(false);
		}
		
	}
}
