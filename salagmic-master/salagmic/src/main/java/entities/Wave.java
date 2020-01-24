package entities;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import TileMap.TileMap;
import main.GamePanel;

/**
 * @author michalsalaga
 *this class specifies one type of weapon
 *parameters: range = width of arena
 *			  damage = 5
 *this class is associated with BattleBot
 */
public class Wave extends MapObject {
	
	private boolean damageCaused = false;
	private boolean remove;
	private int damage;
	private boolean hit;
	private double moveSpeed;
	private BufferedImage[] sprites;	//casting bot with weapon
	private BufferedImage[] hitSprites; //when bot hit another bot 
	
	public Wave(TileMap tm, Direction dir) {
		super(tm);
		
		facingDown = true;
		moveSpeed = 5;
		damage = 5;
		width = 30;
		height = 30;
		cwidth = 14;
		cheight = 14;
		
		
		if(dir == Direction.EAST) {
			dx = moveSpeed;
		}
		else if (dir == Direction.WEST) {
			dx = -moveSpeed;
		}
		else if (dir == Direction.NORTH) {
			dy = -moveSpeed;
		}
		else if(dir == Direction.SOUTH) {
			dy = moveSpeed;
		}
		
		//load sprites
		try {
			
			BufferedImage spriteSheet = ImageIO.read(
					getClass().getResourceAsStream("/Sprites/Player/fireball.gif")
					);
			sprites = new BufferedImage[4];
			for(int i = 0; i < sprites.length; i++) {
				sprites[i] = spriteSheet.getSubimage(
						i * width,
						0,
						width,
						height
						);
			}
			
			hitSprites = new BufferedImage[3];
			for(int i = 0; i < hitSprites.length; i++) {
				hitSprites[i] = spriteSheet.getSubimage(
						i * width,
						height,
						width,
						height
						);
			}
			
			animation = new Animation();
			animation.setFrames(sprites);
			animation.setDelay(70);
			
		}catch(Exception e ) {
			e.printStackTrace();
		}
	}
	
	public boolean getDamageCaused() {return damageCaused;}
	
	public void setDamageCaused(boolean set) {damageCaused = set;}
	
	public void setHit() {
		if (hit) return;
		hit = true;
		animation.setFrames(hitSprites);
		animation.setDelay(70);
		dx = 0;
	}
	
	public boolean shouldRemove() {
		return remove;
	}

	public void update() {
		
		checkTileMapCollision();
		setPosition(xtemp,ytemp);
		
		if(dy == 0 && dx == 0 && !hit) {
			setHit();
		}
		animation.update();
		if(hit && animation.hasPlayedOnce() ) {
			remove = true;
		}
		
	}
	
	public void draw(Graphics2D g) {
		
		setMapPosition();
		
		// draw wave
		
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
					(int)(x - width/2 + width),
					(int)(y - height/2),
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
					(int)(x - width/2 + width),
					(int)(y - height/2),
					-width,
					height,
					null
					);
		}
		
		
	}
}








