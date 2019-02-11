package wave.gameState;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.*;


public class gameState {
	//menu
	mMenu menu;
	
	//level
	Level lvl;
	
	int[] state = new int[3];
	int currentstate = 0;
	
	int mbp = 0;
	
	BufferedImage sheet;
	BufferedWriter saveFile;
	BufferedReader readFile;
	File outputFile;
	int highscore = 0;
	
	//Initializes the game states
	public void init(){
		try{
			outputFile = new File("saveFile.txt");
			readFile = new BufferedReader(new FileReader(outputFile));
			readFile.readLine();
			highscore = Integer.parseInt(readFile.readLine());
		}catch(Exception e){
			e.printStackTrace();
		}
		state[0] = 0; //main menu
		state[1] = 1; //game
		state[2] = 2; //exit
	}
	
	public gameState(int cstate, BufferedImage sheet){
		init();
		currentstate = cstate;
		this.sheet = sheet;
		menu = new mMenu(800, 600, highscore, this);
		lvl = new Level(sheet, highscore);
	}
	
	//game state update
	public void update(){
		if(currentstate == 0){
			menu.update();
			if(menu.itemclicked == true){
				if(menu.bon == 0){
					currentstate = 1;
					menu.itemclicked = false;
				}
				if(menu.bon == 1){
					currentstate = 2;
					try{
						outputFile = new File("saveFile.txt");
						saveFile = new BufferedWriter(new FileWriter(outputFile));
						saveFile.write("\n");
						saveFile.write(lvl.hs + "\n");
						saveFile.close();
					}catch(Exception e){
						e.printStackTrace();
					}
					
					System.exit(0);
				}
			}
		}
		if(currentstate == 1){
			lvl.update();
			if(lvl.p.exitlvl == true){
				lvl.p.exitlvl = false;
				currentstate = 0;
			}
			if(lvl.gameneedstobefixed == true){
				lvl.gameneedstobefixed = false;
				currentstate = 1;
			}
		}
	}
	
	//game state draw
	public void draw(Graphics g){
		if(currentstate == 0){
			menu.draw(g);
		}
		if(currentstate == 1){
			lvl.draw(g);
		}
	}
	
	//game state key handler
	public void pressed(KeyEvent e){
		if(currentstate == 0){
			menu.pressed(e);
		}
		if(currentstate == 1){
			lvl.pressed(e);
		}
	}
	public void released(KeyEvent e){
		if(currentstate == 0){
			menu.released(e);
		}
		if(currentstate == 1){
			lvl.released(e);
		}
	}
	public void mmove(MouseEvent e){
		if(currentstate == 1){
			lvl.mousemovement(e);
		}
		if(currentstate == 0){
			menu.moved(e);
		}
	}
	public void mdragged(MouseEvent e){
		if(currentstate == 0){
			menu.mpressed(e);
		}
		if(currentstate == 1){
			lvl.mdragged(e);
		}
	}
	
}
