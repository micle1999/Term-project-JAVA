package entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class HUD {

	private MapObject obj;	
	private BufferedImage image;
	private Font font;
	private int x;
	private int y; 
	
	public HUD(int x ,int y,MapObject obj) {
		this.obj = obj;
		this.x = x;
		this.y = y;
		
		try {
				
			image = ImageIO.read(
					getClass().getResourceAsStream("/HUD/hud.gif")
					);
			font = new Font("Arial",Font.ITALIC,11);
			
			
		}catch(Exception e) {
			
		}
	}
	
	public void draw(Graphics2D g) {
		g.drawImage(image,x,y,null);
		g.setColor(Color.BLACK);
		g.setFont(font);
		g.drawString(obj.getHealth()+ "/" + "100", x + 14, y + 11);
		g.drawString((obj.getWaves()/200)+ "/" + "5", x + 14, y + 26);
	}

}
