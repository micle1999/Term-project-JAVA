package entities;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import TileMap.TileMap;

public class Enemy extends MapObject {
	
	//enemy stuff
	private double speed;
	private boolean dead;
	private int damage = 5; //damage when two bots collide
	
	//Wave
	private int maxWave = 1000;
	private int WaveCost = 200;
	private boolean firing;
	private ArrayList<Wave> waves;
	
	//animations
	private BufferedImage[] sprite_right;
	private BufferedImage[] sprite_left;
	private BufferedImage[] sprite_up;
	private BufferedImage[] sprite_down;
	
	//animations actions - enum of all animations 
	private static final int WALKING = 0;
	
	public Enemy(TileMap tm) {
		super(tm);
		right = true;
		width = 30;
		height= 30;
		cwidth = 20;
		cheight = 20;
		speed = 1.3;
		health = 100;
		wave = 1000;
		waves = new ArrayList<Wave>();
		
		//load Sprites
			try {
				
				BufferedImage spriteSheet_right = ImageIO.read(
						getClass().getResourceAsStream("/Sprites/Enemies/enemyBot_sprite_right.png")
						);
						sprite_right = new BufferedImage[1];
						sprite_right[0] = spriteSheet_right.getSubimage(
								0 * width,
								0 * height, 
								width, 
								height);
				BufferedImage spriteSheet_left = ImageIO.read(
						getClass().getResourceAsStream("/Sprites/Enemies/enemyBot_sprite_left.png")
						);
						sprite_left = new BufferedImage[1];
						sprite_left[0] = spriteSheet_left.getSubimage(
								0 * width,
								0 * height, 
								width, 
								height);
				BufferedImage spriteSheet_up = ImageIO.read(
						getClass().getResourceAsStream("/Sprites/Enemies/enemyBot_sprite_up.png")
						);
						sprite_up = new BufferedImage[1];
						sprite_up[0] = spriteSheet_up.getSubimage(
								0 * width,
								0 * height, 
								width, 
								height);
				BufferedImage spriteSheet_down = ImageIO.read(
						getClass().getResourceAsStream("/Sprites/Enemies/enemyBot_sprite_down.png")
						);
						sprite_down = new BufferedImage[1];
						sprite_down[0] = spriteSheet_down.getSubimage(
								0 * width,
								0 * height, 
								width, 
								height);
				
					
			}catch(Exception e) {
				e.printStackTrace();
			}
			animation = new Animation();
			currentAction = WALKING;
			animation.setFrames(sprite_right);
			animation.setDelay(400);
	}
	
	public int getDamage() {
		return damage;
	}
	
	public boolean isDead() {
		return dead;
	}
	
	
	public double getSpeed() {
		return speed;
	}
	
	
	public void setFiring() {
		this.firing = true;
	}

	public void hit() {
		if(dead == true)return;
		health -= damage;
		if(health < 0) health = 0;
		if(health == 0) dead = true;
	}
	public void getNextPosition() {
		//movement
		
		if(isDead() == true) {
			dx = 0;
			dy = 0;
			return;
		}
		
		
		if(left) {
			animation.setFrames(sprite_left);
			dy = getRandomNumberInRange(0,5);
			dx -= speed;
			if(dx < -speed) {
				dx = -speed;
			}
		}
		else if(right) {
			animation.setFrames(sprite_right);
			dy = getRandomNumberInRange(-3,0);
			dx += speed;
			if(dx > speed) {
				dx = speed;
			}
		}
		else if(up) {
			animation.setFrames(sprite_up);
			dx = getRandomNumberInRange(0,2);
			dy -= speed;
			if(dy < speed) {
				dy = -speed;
			}
		}
		else if(down) {
			animation.setFrames(sprite_down);
			dx = getRandomNumberInRange(-5 ,0);
			dy += speed;
			if(dy > speed) {
				dy = speed;
			}
		}
		
	}

	public void update() {
		
		//update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		//Wave attack
		if (wave == 1000) setFiring();
		wave += 1;
		if(wave  > maxWave) wave = maxWave;
		if(firing) {
			if(wave > WaveCost) {
				wave -= WaveCost;
				Wave newWave = new Wave(tileMap, this.getDirection());
				newWave.setPosition(x, y);
				waves.add(newWave);
				firing = false;
			}
		}

		
		for(int i = 0; i < waves.size(); i++) {
			waves.get(i).update();
			if(waves.get(i).shouldRemove()) {
				waves.remove(i);
				i--;
			}
		}
		//setAnimation
		if(right && dx == 0) {
			right = false;
			left  = true;
			up = false;
			down = false;
				
		}
		else if(left && dx == 0) {
			left = false;
			right = true;
			up = false;
			down = false;
		}
		else if(up && dy == 0) {
			left = false;
			right = false;
			up = true;
			down = false;
		}
		else if(down && dy == 0) {
			left = false;
			right = false;
			up = false;
			down = true;
		}
	
		
		animation.update();
		
		//set direction
		if(!isDead()) {
			if(right) {
				facingRight = true;
				facingLeft = false;
				facingUp = false;
				facingDown = false;
			}
			if(left) {
				facingRight = false;
				facingLeft = true;
				facingUp = false;
				facingDown = false;
			}
			if(down) {
				facingRight = false;
				facingLeft = false;
				facingUp = false;
				facingDown = true;
			}
			if(up) {
				facingRight = false;
				facingLeft = false;
				facingUp = true;
				facingDown = false;
			}
		}
		
	}
	
	public void draw(Graphics2D g) {
		
		if(isDead()) return;
		setMapPosition(); 
		
		for (int i = 0; i < waves.size(); i++) {
			waves.get(i).draw(g);
		}
		
		//right image will be the default image 
		g.drawImage(
				animation.getImage(),
				(int)(x + xmap - width/2),
				(int)(y + ymap - height/2),
				null
				);
		//draw player 
		 
		if(facingRight) {
			g.drawImage(
					animation.getImage(),
					(int)(x - width/2),
					(int)(y - height/2),
					null
					);
		}
		else if(facingLeft) {
			g.drawImage(
					animation.getImage(),
					(int)(x  - width/2 + width),
					(int)(y  - height/2),
					-width,
					height,
					null
					);
		}
		else if(facingUp) {
			g.drawImage(
					animation.getImage(),
					// how to set parameters when bot is facing up ?
					(int)(x - width/2 + width),
					(int)(y - height/2),
					-width,
					height,
					null
					);
		}
		else if(facingDown) {
			g.drawImage(
					animation.getImage(),
					//how to set parameters when bot is facing down ?
					(int)(x  - width/2 + width),
					(int)(y  - height/2),
					-width,
					height,
					null
					);
		}
		
	}
	
	public void checkWaveAttack(BattleBot player) {
		for(int i = 0; i < waves.size(); i++) {
			if(this.waves.get(i).intersects(player)) {
				if(this.waves.get(i).getDamageCaused()==false) {
					player.hit();
					this.waves.get(i).setDamageCaused(true);
				}
				waves.get(i).setHit();
				break;
			}
		}
	}
	private static int getRandomNumberInRange(int min, int max) {

		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}

		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
}
