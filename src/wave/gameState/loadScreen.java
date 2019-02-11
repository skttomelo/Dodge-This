package wave.gameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

public class loadScreen {
	//load icon
	int w = 300, h = 30, x = 400 - (w/2), y = 300 + (h/2), lw = 0, lh = h, lx = x, ly = y;
	//loading word
	int loadx = x + (w/2), loady = y - 10;
	String loading = "Loading Game";
	//percentage done
	int percentage = 0, percentagemax = 100, px = x + (w/2), py = y + (h/2);
	String percent = percentage + "/" + percentagemax;
	//boolean for checking if loading has happened
	boolean timechosen = false;
	public boolean gameloaded = false;
	//long for setting time, before percentage increase
	long timetillupdate;
	//boolean for changing color
	boolean switchcolor = false;
	//font measuring
	Font font;
	FontMetrics m;
	int th, tw;
	
	public loadScreen(Font font){
		this.font  = font;
	}
	
	public void update(){
		if(gameloaded == false){
			if(timechosen == false){
				timechosen = true;
				timetillupdate = System.currentTimeMillis() + 50;
			}
			if(System.currentTimeMillis() > timetillupdate){
				timechosen = false;
				percentage++;
				percent = percentage + "/" + percentagemax;
				lw += 3;
				if(percentage == 43){
					switchcolor = true;
				}
				if(lw >= w){
					lw = w;
					if(percentage >= percentagemax){
						percentage = percentagemax;
						gameloaded = true;
					}
				}
			}
		}
	}
	public void draw(Graphics g){
		if(gameloaded == false){
			m = g.getFontMetrics(font);
			g.setColor(Color.gray);
			g.fillRect(x, y, w, h);
			g.setColor(Color.green);
			g.fillRect(lx, ly, lw, lh);
			g.setColor(Color.BLACK);
		if(switchcolor == false){g.setColor(Color.WHITE);}
			tw = m.stringWidth(percent);
			g.drawString(percent, px - (tw/2), py + (th/4));
			tw = m.stringWidth(loading);
			g.setColor(Color.white);
			g.drawString(loading, loadx - (tw/2), loady);
		}
	}
}
