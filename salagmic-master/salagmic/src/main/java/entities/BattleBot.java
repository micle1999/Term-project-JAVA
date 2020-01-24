package entities;

import java.awt.*;
import TileMap.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 * 
 * @author michalsalaga
 *class BattleBot represents one of the robots that fight in arena
 */

public class BattleBot extends MapObject {

	//Bot stuff
	private int counter = 0;
	private double speed;
	private double 	stopSpeed = 0.4;
	private boolean dead;
	private int damage = 5;
	
	
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

	
	public BattleBot(TileMap tm) {
		super(tm);
		wave = 1000;
		width = 30;
		height= 30;
		cwidth = 20;
		cheight = 20;
		speed = 1.5;
		health = 100;
		waves = new ArrayList<Wave>();
		
		
		//load Sprites
		try {
			
			BufferedImage spriteSheet_right = ImageIO.read(
					getClass().getResourceAsStream("/Sprites/Player/bot_sprite_right.png")
					);
					sprite_right = new BufferedImage[1];
					sprite_right[0] = spriteSheet_right.getSubimage(
							0 * width,
							0 * height, 
							width, 
							height);
			BufferedImage spriteSheet_left = ImageIO.read(
					getClass().getResourceAsStream("/Sprites/Player/bot_sprite_left.png")
					);
					sprite_left = new BufferedImage[1];
					sprite_left[0] = spriteSheet_left.getSubimage(
							0 * width,
							0 * height, 
							width, 
							height);
			BufferedImage spriteSheet_up = ImageIO.read(
					getClass().getResourceAsStream("/Sprites/Player/bot_sprite_up.png")
					);
					sprite_up = new BufferedImage[1];
					sprite_up[0] = spriteSheet_up.getSubimage(
							0 * width,
							0 * height, 
							width, 
							height);
			BufferedImage spriteSheet_down = ImageIO.read(
					getClass().getResourceAsStream("/Sprites/Player/bot_sprite_down.png")
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
	

	
	public double getSpeed() {
		return speed;
	}
	public int getNumOfWaves() {
		return waves.size();
	}
	
	public void setFiring() {
		this.firing = true;
	}
	
	public boolean isDead() {
		return dead;
	}
	
	public void getNextPosition() {
		//movement
		if(isDead()== true) {
			dx = 0;
			dy = 0;
			return;
		}
		if(left) {
			animation.setFrames(sprite_left);
			dx -= speed;
			if(dx < -speed) {
				dx = -speed;
			}
		}
		else if(right) {
			animation.setFrames(sprite_right);
			dx += speed;
			if(dx > speed) {
				dx = speed;
			}
		}
		else if(up) {
			animation.setFrames(sprite_up);
			dy -= speed;
			if(dy < speed) {
				dy = -speed;
			}
		}
		else if(down) {
			animation.setFrames(sprite_down);
			dy += speed;
			if(dy > speed) {
				dy = speed;
			}
		}
		
		//stop
		else {
			if(dx > 0) {
				dx -= stopSpeed;
				if(dx < 0) {
					dx = 0;
				}
			}
			else if(dx < 0) {
				dx += stopSpeed;
				if(dx > 0) {
					dx = 0;
				}
			}
			else if(dy > 0) {
				dy -= stopSpeed;
				if(dy < 0) {
					dy = 0;
				}
			}
			else if(dy < 0) {
				dy += stopSpeed; 
				if(dy > 0) {
					dy = 0;
				}
			}
		}
	
	}
	
	
	public void update() {
		
		//update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		//Wave attack
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
		
				
			
		if(right || left) {
			 if(currentAction != WALKING) {
				 currentAction = WALKING;
				 animation.setFrames(sprite_right);
				 animation.setDelay(40);
				 width = 30; 
			 }
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
					(int)(x + xmap - width/2),
					(int)(y + ymap - height/2),
					null
					);
			
			
		}
		else if(facingLeft) {
			g.drawImage(
					animation.getImage(),
					(int)(x + xmap - width/2 + width),
					(int)(y + ymap - height/2),
					-width,
					height,
					null
					);
			
		}
		else if(facingUp) {
			g.drawImage(
					animation.getImage(),
					// how to set parameters when bot is facing up ?
					(int)(x + xmap - width/2 + width),
					(int)(y + ymap - height/2),
					-width,
					height,
					null
					);
		}
		else if(facingDown) {
			g.drawImage(
					animation.getImage(),
					//how to set parameters when bot is facing down ?
					(int)(x + xmap - width/2 + width),
					(int)(y + ymap - height/2),
					-width,
					height,
					null
					);
		}
		
	}
	
	public void checkAttack(Enemy enemy) {
		
		//check ram attack
		if(facingRight) {
			if(enemy.getx() > x
				&& enemy.getx() < x + 18
				&& enemy.gety() > y - height/2
				&& enemy.gety() < y + height/2) {
					enemy.hit();
					x -= 15;
					if(enemy.x + 45 >= tileMap.getWidth() && enemy.y + 45 >= tileMap.getHeight()){
						enemy.y -= 15;
					}
					else if(enemy.x + 45 >= tileMap.getWidth() && enemy.y - 45 <= tileMap.getHeight()){
						enemy.y += 15;
					}
					else {
						enemy.x += 15;
					}
			}
			
		}
		else if(facingLeft) {
			if(enemy.getx() < x
				&& enemy.getx() > x - 19
				&& enemy.gety() > y - height/2
				&& enemy.gety() < y + height/2) {
					enemy.hit();
					x += 15;
					if(enemy.x - 45 <= tileMap.getWidth() && enemy.y + 45 >= tileMap.getHeight()){
						enemy.y -= 15;
					}
					else if(enemy.x - 45 <= tileMap.getWidth() && enemy.y - 45 <= tileMap.getHeight()){
						enemy.y += 15;
					}
					else {
						enemy.x -= 15;
					}
			}
			
		}
		else if(facingUp) {
			if(enemy.gety() < y
				&& enemy.gety() > y - 19
				&& enemy.getx() > x - width/2
				&& enemy.getx() < x + width/2) {
					enemy.hit();
					y += 15;
					if(enemy.y - 45 <= tileMap.getHeight() && enemy.x + 45 >= tileMap.getWidth()){
						enemy.x -= 15;
					}
					else if(enemy.y - 45 <= tileMap.getHeight() && enemy.x - 45 <= tileMap.getWidth()){
						enemy.x += 15;
					}
					else {
						enemy.y -= 15;
					}
			}
			
		}
		else if(facingDown) {
			if(enemy.gety() > y
				&& enemy.gety() < y + 18
				&& enemy.getx() > x - width/2
				&& enemy.getx() < x + width/2) {
					enemy.hit();
					y -= 15;
					if(enemy.y + 45 >= tileMap.getHeight() && enemy.x + 45 >= tileMap.getWidth()){
						enemy.x -= 15;
					}
					else if(enemy.y + 45 >= tileMap.getHeight() && enemy.x - 45 <= tileMap.getWidth()){
						enemy.x += 15;
					}
					else {
						enemy.y += 15;
					}
			}

		}
		
		//check wave attack
		for(int i = 0; i < waves.size(); i++) {
			if(this.waves.get(i).intersects(enemy)) {
				if(this.waves.get(i).getDamageCaused()==false) {
					enemy.hit();
					this.waves.get(i).setDamageCaused(true);
				waves.get(i).setHit();
				break;
				}
			}	
		}
		
		//check for enemy collision
		if(intersects(enemy)) {
			if(ramDamageCaused == false) {
				hit();
				ramDamageCaused = true;
				counter = 0;
			}
		}
		counter++;
		if(counter == 20) {
			ramDamageCaused = false;
		}
	}
	
	public void hit(){
		if(dead == true)return;
		health -= damage;
		if(health < 0) {health = 0;}
		if(health ==0) {dead = true;}
	}
	
	
}






