package wave.Mobs;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import wave.gameState.Level;

public class asteroid {
	public int x = 0, y = 0, w = 10, h = 10, scale = 1, speedincrease = 5, maxspeedincrease = 8, frame = 4;
	long speedtime, timetillframe;
	boolean onscreen = true, nospeedtime = true, animationtime = false;
	public boolean exist = true, pwarping = false, draw = true;
	Random r = new Random();
	BufferedImage sheet;
	BufferedImage[] rock = new BufferedImage[8];
	Level lvl;
	
	public void init(){
		rock[0] = sheet.getSubimage(0, 40, w, h);
		rock[1] = sheet.getSubimage(0, 50, w, h);
		rock[2] = sheet.getSubimage(0, 60, w, h);
		rock[3] = sheet.getSubimage(0, 70, w, h);
	}
	public asteroid(BufferedImage sheet, Level lvl, int scale){
		this.lvl = lvl;
		this.sheet = sheet;
		init();
		this.scale = scale;
		w = w * scale;
		h = h * scale;
		x = 800 + (w * 2);
		y = r.nextInt(600) - h;
		y = r.nextInt(600) - h;
		frame = r.nextInt(4);
	}
	public void update(){
		if(exist == true){
			if(y < 35){
				y = 35;
			}
			if(x + w < 0){
				x = 800 + (w * 2);
				y = r.nextInt(600) - h;
			}
			speedIncrease();
			if(onscreen == true){
				x -= speedincrease;
			}
		}
		animation();
	}
	
	//checks to see if asteroid should speed up
	public void speedIncrease(){
		if(speedincrease != maxspeedincrease){
			if(System.currentTimeMillis() > lvl.catchuptime){
				speedincrease = 6;
			}
			if(System.currentTimeMillis() > lvl.catchuptime2){
				speedincrease = 7;
			}
			if(System.currentTimeMillis() > lvl.catchuptime3){
				speedincrease = 8;
			}
		}
		if(pwarping == true){
			speedincrease = 15;
		}
	}
	
	//help fix spawning at beginning of game when first game loads up
	public void fixspawn(){
		x = 0 - w;
	}
	
	public Rectangle form(){
		return new Rectangle(x, y, w, h);
	}
	public void animation(){
		if(animationtime == false){
			animationtime = true;
			timetillframe = System.currentTimeMillis() + 200;
		}
		if(System.currentTimeMillis() > timetillframe){
			frame++;
			if(frame > 3){
				frame = 0;
			}
			animationtime = false;
		}
	}
	public void draw(Graphics g){
		if(exist == true && draw == true){
			g.drawImage(rock[frame], x, y, w, h, null);
		}
	}
}
