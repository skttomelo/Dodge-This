package wave.Mobs;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

import wave.gameState.Level;

public class battery {
	int x, y, w = 12, h = 5, scale = 1, speed = 5, maxspeed = 8, c1 = 250, c2 = 500, c3 = 750, c4 = 1000, chancetospawn, spawn;
	public int powerup;
	BufferedImage[] batteryobj = new BufferedImage[4];
	BufferedImage[] shield = new BufferedImage[6];
	BufferedImage sheet;
	boolean notused = true;
	public boolean collided = false;
	Level lvl;
	Random r = new Random();
	
	public void init(){
		batteryobj[0] = sheet.getSubimage(38, 0, w, h); //shield battery
		batteryobj[1] = sheet.getSubimage(38, 5, w, h); //HP battery
		batteryobj[2] = sheet.getSubimage(38, 10, w, h); //Warp battery
		batteryobj[3] = sheet.getSubimage(38, 15, w, h); //obliterate battery
		x = 800 + w;
	}
	public battery(BufferedImage item, Level lvl, int scale){
		this.lvl = lvl;
		sheet = item;
		init();
		this.scale = scale;
		w = scale * w;
		h = scale * h;
		x = 800 + w;
		y = r.nextInt(600);
		if(y + h >= 600){
			y -= h;
		}
		if(y - 26 <= 0){
			y += 26;
		}
		powerup = r.nextInt(4);
		itemspawnnum();
	}
	
	public void update(){
		if(speed <= maxspeed){
			if(System.currentTimeMillis() > lvl.catchuptime){
				speed = 6;
			}
			if(System.currentTimeMillis() > lvl.catchuptime2){
				speed = 7;
			}
			if(System.currentTimeMillis() > lvl.catchuptime3){
				speed = 8;
			}
		}
		if(notused == true){
			if(powerup == 0){
				spawn = r.nextInt(c1);
			}
			if(powerup == 1){
				spawn = r.nextInt(c2);
			}
			if(powerup == 2){
				spawn = r.nextInt(c3);
			}
			if(powerup == 3){
				spawn = r.nextInt(c4);
			}
			if(spawn == chancetospawn){
				notused = false;
			}
		}
		if(notused == false && collided == false){
			x -= speed;
		}
		if(x + w <= 0){
			offscreen();
		}
		newpowerup();
	}
	
	public void itemspawnnum(){
		if(powerup == 0){
			chancetospawn = r.nextInt(250);
		}
		if(powerup == 1){
			chancetospawn = r.nextInt(500);
		}
		if(powerup == 2){
			chancetospawn = r.nextInt(750);
		}
		if(powerup == 3){
			chancetospawn = r.nextInt(1000);
		}
	}
	
	//resets the powerup for when it goes offscreen
	public void offscreen(){
		x = 800 + w;
		y = r.nextInt(600);
		if(y + h >= 600){
			y -= h;
		}
		if(y - 26 <= 0){
			y += 26;
		}
		notused = true;
		powerup = r.nextInt(4);
		itemspawnnum();
	}
	
	//creates new powerup for when it collides with the player
	public void newpowerup(){
		if(collided == true){
			offscreen();
			collided = false;
		}
	}
	
	public Rectangle form(){
		return new Rectangle(x, y, w, h);
	}
	
	public void draw(Graphics g){
		if(notused == false && collided == false){
			g.drawImage(batteryobj[powerup], x, y, w, h, null);
		}
	}
}
