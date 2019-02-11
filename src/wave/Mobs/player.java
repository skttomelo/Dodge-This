package wave.Mobs;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class player {
	public boolean exitlvl = false, alive = true, isdead = false, shieldrequired = false, HPrequired = false;
	int x = 20, w = 16, sw = 18,h = 10, sh = 12, y = 300 - (h/2), tx = x,ty = y, scale = 1;
	BufferedImage paddle, sheet;
	BufferedImage[] player = new BufferedImage[4];
	BufferedImage[] shield = new BufferedImage[6];
	BufferedImage[] HP = new BufferedImage[5];
	
	//animation booleans
	boolean rotationdone = false, animationtime = false, notreversing = true, animationtimes = false;
	//animation LONGS
	long timetill, timetills;
	//animation ints
	int frame = 0, sframe = 0, HPframe = 0;
	
	public void init(){
		paddle = sheet.getSubimage(14, 0, 16, 10);
		player[0] = sheet.getSubimage(14, 0, 16, 10);
		player[1] = sheet.getSubimage(14, 10, 16, 10);
		player[2] = sheet.getSubimage(14, 20, 16, 10);
		player[3] = sheet.getSubimage(14, 30, 16, 10);
		//shield
		shield[0] = sheet.getSubimage(13, 40, 18, 12);
		shield[1] = sheet.getSubimage(31, 40, 18, 12);
		shield[2] = sheet.getSubimage(13, 52, 18, 12);
		shield[3] = sheet.getSubimage(31, 52, 18, 12);
		shield[4] = sheet.getSubimage(13, 64, 18, 12);
		shield[5] = sheet.getSubimage(31, 64, 18, 12);
		//HP
		HP[0] = sheet.getSubimage(0, 80, 16, 10);
		HP[1] = sheet.getSubimage(16, 80, 16, 10);
		HP[2] = sheet.getSubimage(32, 80, 16, 10);
		HP[3] = sheet.getSubimage(0, 90, 16, 10);
		HP[4] = sheet.getSubimage(16, 90, 16, 10);
		//warp
	}
	public player(BufferedImage sheet, int scale){
		this.scale = scale;
		w = w * scale;
		h = h * scale;
		sw = sw * scale;
		sh = sh * scale;
		y = 300 - (h/2);
		this.sheet = sheet;
		init();
	}
	
	public void update(){
		animation();
		if(isdead == true){
			x++;
			y += 6;
			if(y > 600){
				alive = false;
			}
		}
	}
	
	public void resetx(){
		x = tx;
	}
	public void resety(){
		y = ty;
	}
	
	public Rectangle bound(){
		return new Rectangle(x, y, w, h);
	}
	public void animation(){
		if(animationtime == false){
			animationtime = true;
			timetill = System.currentTimeMillis() + 100;
		}
		if(animationtimes == false){
			timetills = System.currentTimeMillis() + 1000;
			animationtimes = true;
		}
		if(System.currentTimeMillis() > timetill){
			if(notreversing == true){
				frame++;
				if(frame > 3){
					frame = 3;
				}
			}
			if(notreversing == false){
				frame--;
				if(frame < 0){
					frame = 0;
				}
			}
			if(frame == 3 && notreversing == true){
				notreversing = false;
			}
			if(frame == 0 && notreversing == false){
				notreversing = true;
			}
			//shield animation
			if(shieldrequired == true){
				sframe++;
				if(sframe > 5){
					sframe = 5;
				}
				if(System.currentTimeMillis() > timetills){
					sframe = 0;
					animationtimes = false;
				}
			}
			//shield animation end
			//HP animation
			if(HPrequired == true){
				HPframe++;
				if(HPframe > 4){
					HPframe = 0;
					HPrequired = false;
				}
			}
			//HP animation end
			animationtime = false;
		}
	}
	public void draw(Graphics g){
		if(alive == true){
			g.drawImage(player[frame], x, y, w, h, null);
			if(shieldrequired == true){
				g.drawImage(shield[sframe], x - scale, y - scale, sw, sh, null);
			}
			if(HPrequired == true){
				g.drawImage(HP[HPframe], x, y, w, h, null);
			}
		}
	}
	
	@SuppressWarnings("static-access")
	public void pressed(KeyEvent e){
		int key = e.getKeyCode();
		if(key == e.VK_ESCAPE){
			exitlvl = true;
		}
	}
	public void released(KeyEvent e){
		@SuppressWarnings("unused")
		int key = e.getKeyCode();
	}
	
	public void move(MouseEvent e){
		if(isdead == false){
			y = e.getY() - (h/2);
			if(y - 26 <= 0){
				y = 26;
			}
			if(y + h >= 600){
				y = 600 - h - 3;
			}
		}
	}
	public void dragged(MouseEvent e){
		if(isdead == false){
			y = e.getY() - (h/2);
			if(y - 26 <= 0){
				y = 26;
			}
			if(y + h >= 600){
				y = 600 - h - 3;
			}
		}
	}
}
