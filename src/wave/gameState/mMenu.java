package wave.gameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class mMenu {
	//coordinates
	int x = 0, y = 0, b1x = 0, b1y = 0, b2x = 0, b2y = 0;
	//width of objects
	int b1w = 150, b1h = 50, b2w = b1w, b2h = b1h;
	//Selected Box coords and width
	int sbx, sby, sbw = b1w, sbh = b1h, bon = 0;
	//check if can move true/false statement
	boolean canSwitch = true;
	//change gamestate true/false statement
	boolean itemclicked = false;
	//loadscreen
	loadScreen ls;
	//font setting
	Font font = new Font("Dialog", Font.BOLD, 14);
	FontMetrics measure;
	int th, tw;
	//highscore
	int hs;
	
	gameState gs;
	
	public mMenu(int x, int y, int hs, gameState gs){
		this.gs = gs;
		this.hs = hs;
		this.x = x;
		this.y = y;
		b1x = (x/2) - (b1w/2);
		b1y = (y/2) - b1h;
		b2x = (x/2) - (b2w/2);
		b2y = (y/2) + b2h;
		sbx = b1x;
		sby = b1y;
		bon = 0;
		ls = new loadScreen(font);
	}
	
	//Collisions
	public Rectangle Button1(){
		return new Rectangle(b1x, b1y, b1w, b1h);
	}
	
	public Rectangle Button2(){
		return new Rectangle(b2x, b2y, b2w, b2h);
	}
	
	public Rectangle selectedBox(){
		return new Rectangle(sbx, sby, sbw, sbh);
	}
	
	//button click control
	public void pressed(KeyEvent e){
		int key = e.getKeyCode();
		if(ls.gameloaded == true){
		if(canSwitch == true){
			if(key == e.VK_UP){
				bon++;
				if(bon > 1){
					bon = 0;
				}
				canSwitch = false;
			}
			if(key == e.VK_DOWN){
				bon--;
				if(bon < 0){
					bon = 1;
				}
				canSwitch = false;
			}
			if(key == e.VK_SPACE){
				itemclicked = true;
				canSwitch = false;
			}
		}
		}
	}
	public void released(KeyEvent e){
		int key = e.getKeyCode();
		if(ls.gameloaded == true){
		if(key == e.VK_UP){
			canSwitch = true;
		}
		if(key == e.VK_DOWN){
			canSwitch = true;
		}
		if(key == e.VK_SPACE){
			itemclicked = false;
			canSwitch = true;
		}
		}
	}
	public void moved(MouseEvent e){
		if(Button1().contains(e.getPoint()) == true){
			bon = 0;
		}
		if(Button2().contains(e.getPoint()) == true){
			bon = 1;
		}
	}
	public void mpressed(MouseEvent e){
		int mbp = e.getButton();
		if(ls.gameloaded == true){
			if(bon == 0 && mbp == 1 && Button1().contains(e.getPoint()) == true){
				itemclicked = true;
			}
			if(bon == 1 && mbp == 1 && Button2().contains(e.getPoint()) == true){
				itemclicked = true;
			}
		}
	}
	
	//check and see if something has happened
	public void update(){
		ls.update();
		gs.init();
		hs = gs.highscore;
		if(bon == 0){
			sbx = b1x;
			sby = b1y;
			sbw = b1w;
			sbh = b1h;
		}
		if(bon == 1){
			sbx = b2x;
			sby = b2y;
			sbw = b2w;
			sbh = b2h;
		}
	}
	
	//draw what i see
	public void draw(Graphics g){
		//font
		g.setFont(font);
		measure = g.getFontMetrics(font);
		th = measure.getHeight();
		ls.th = th;
		//background
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, x, y);
		ls.draw(g);
		if(ls.gameloaded == true){
			//HS
			g.setColor(Color.WHITE);
			font = new Font("Times New Roman", font.BOLD, 14);
			measure = g.getFontMetrics(font);
			th = measure.getHeight();
			tw = measure.stringWidth("Highscore: " + hs + "m");
			g.setFont(font);
			g.drawString("Highscore: " + hs + "m", 5, 50);
			//Title
			font = new Font("Times New Roman", Font.BOLD, 100);
			measure = g.getFontMetrics(font);
			th = measure.getHeight();
			tw = measure.stringWidth("Dodge This");
			g.setFont(font);
			g.drawString("Dodge This", b1x + (b1w/2) - (tw/2), 150);
			//////////////////
			font = new Font("Times New Roman", Font.BOLD, 14);
			measure = g.getFontMetrics(font);
			th = measure.getHeight();
			g.setFont(font);
			//start button
			g.setColor(Color.WHITE);
			g.fillRect(b1x, b1y, b1w, b1h);
			g.setColor(Color.BLACK);
			tw = measure.stringWidth("Start");
			g.drawString("Start", b1x + (b1w/2) - (tw/2), b1y + (b1h/2) + (th/4));
			//exit button
			g.setColor(Color.WHITE);
			g.fillRect(b2x, b2y, b2w, b2h);
			g.setColor(Color.BLACK);
			tw = measure.stringWidth("Exit");
			g.drawString("Exit", b2x + (b2w/2) - (tw/2), b2y + (b2h/2) + (th/4));
			//box selecting
			g.setColor(Color.RED);
			g.drawRect(sbx, sby, sbw, sbh);
			g.drawRect(sbx + 1, sby + 1, sbw - 2, sbh - 2);
		}
	}
}
