package wave.gameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import wave.Mobs.asteroid;
import wave.Mobs.battery;
import wave.Mobs.player;

public class Level {
	player p;
	battery b;
	ArrayList<asteroid> rock = new ArrayList<asteroid>();
	int HP = 5, iaos = 25, dm = 0, dmincrease = 2, odm;
	public long catchuptime, catchuptime2, catchuptime3;
	boolean catchuptimechosen = false;
	BufferedImage sheet;
	
	//variables for distance check
	boolean distancetime = false, distanceupdate = true;
	long timetilldistancecheck, distancetillincreasedm, maxtime;
	
	//variable for making the game work correctly
	boolean gameneedstobefixed = true;
	
	//font setting
	Font font = new Font("Dialog", Font.BOLD, 14);
	
	//power-up timer variables
	boolean pu = false, putrs = false, putrw = false, warping = false, shield = false, obliterate = false, pcanbehurt = true;
	long putrslast, putrwlast;
	
	//obliterate variables
	boolean otime = false, Ooffscreen = false, Ocollide = false, isnotrender = false;
	long otimer = 60;
	
	//highscore
	int hs;
	BufferedWriter saveFile;
	File outputFile;
	
	public Level(BufferedImage sheet, int hs){
		this.hs = hs;
		this.sheet = sheet;
		p = new player(this.sheet, 7/2);
		b = new battery(this.sheet, this, 4);
	}
	
	public void update(){ 
		if(gameneedstobefixed == true){
			gameneedstobefixed = false;
			for(int i = 0; i < 10; i++){
				for(int j = 0; j < rock.size(); j++){
					asteroid m = (asteroid) rock.get(j);
					m.fixspawn();
				}
			}
		}
		if(catchuptimechosen == false){
			catchuptimechosen = true;
			catchuptime = System.currentTimeMillis() + 6000;
			catchuptime2 = System.currentTimeMillis() + 12000;
			catchuptime3 = System.currentTimeMillis() + 18000;
		}
		poweruptimer();
		for(int d = 0; d < rock.size(); d++){
			asteroid m = (asteroid) rock.get(d);
			m.update();
		}
		genrock();
		b.update();
		p.update();
		if(p.exitlvl == true){
			for(int a = 0; a < rock.size(); a++){
				asteroid m = (asteroid) rock.get(a);
				m.x = 800 + m.w;
				m.speedincrease = 5;
				m.pwarping = false;
			}
			HP = 5;
			dm = 0;
			catchuptimechosen = false;
			p.isdead = false;
			p.alive = true;
			p.resetx();
			p.resety();
			dmincrease = 2;
			obliterate = false;
			Ocollide = false;
			otime = false;
			Ooffscreen = false;
			Ocollide = false;
			isnotrender = false;
			pu = false;
			putrs = false;
			putrw = false;
			warping = false;
			shield = false;
			pcanbehurt = true;
			b.newpowerup();
		}
		distanceCheck();
		collisions();
	}
	public void distanceCheck(){
		if(p.isdead == false){
			if(distancetime == false){
				distancetime = true;
				timetilldistancecheck = System.currentTimeMillis() + 175;
			}
			if(distanceupdate == true){
				distanceupdate = false;
				distancetillincreasedm = System.currentTimeMillis() + 6000;
			}
			if(System.currentTimeMillis() > distancetillincreasedm){
				if(warping == false){
					if(dmincrease == 32){
						dmincrease = odm;
					}
					dmincrease += 2;
					if(dmincrease > 16){
						dmincrease = 16;
					}
					distanceupdate = true;
				}
			}
			if(System.currentTimeMillis() > timetilldistancecheck){
				distancetime = false;
				if(warping == true){
					dmincrease = 32;
				}
				dm += dmincrease;
			}
		}
	}
	
	//power-up timer
	//pu = power-up & putr = power-up timer required
	//add a flashing effect to the shield to warn the player that the powerup is about to stop working
	public void poweruptimer(){
		//add timer for warping, seeing as that is the next step in the warp powerup
		if(pu == true){
			if(shield == true){
				if(putrs == true){
					putrs = false;
					putrslast = 5000 + System.currentTimeMillis();
				}
				if(System.currentTimeMillis() >= putrslast){
					pu = false;
					p.shieldrequired = false;
					pcanbehurt = true;
					if(warping = true){
						p.shieldrequired = true;
						pcanbehurt = false;
					}
					shield = false;
				}
			}
			if(warping == true){
				if(putrw == true){
					putrw = false;
					putrwlast = 7000 + System.currentTimeMillis();
				}
				if(System.currentTimeMillis() > putrwlast){
					p.shieldrequired = false;
					for(int i = 0; i < rock.size(); i++){
						asteroid m = (asteroid) rock.get(i);
						m.pwarping = false;
					}
					warping = false;
					pu = false;
					pcanbehurt = true;
					if(shield == true){
						p.shieldrequired = true;
					}
				}
			}
			if(obliterate == true){
				if(isnotrender == false){
					if(otime == false){
						otime = true;
						otimer = 150 + System.currentTimeMillis();
					}
					if(System.currentTimeMillis() >= otimer){
						otime = false;
						for(int i = 0; i < rock.size(); i++){
							asteroid m = (asteroid) rock.get(i);
							m.w -= m.scale;
							m.h -= m.scale;
							m.y += m.scale;
							if(m.w <= 0 && m.h <= 0){
								m.draw = false;
								isnotrender = true;
							}
						}
					}
				}
				if(isnotrender == true){
					if(otime == false){
						otime = true;
						otimer = 2000 + System.currentTimeMillis();
					}
					if(System.currentTimeMillis() >= otimer){
						for(int i = 0; i < rock.size(); i++){
							asteroid m = (asteroid) rock.get(i);
							m.x = 0 - 100;
							m.draw = true;
							m.w = 10 * m.scale;
							m.h = 10 * m.scale;
							m.pwarping = false;
						}
						otime = false;
						isnotrender = false;
						obliterate = false;
						pcanbehurt = true;
						pu = false;
						Ocollide = false;
					}
				}
			}
			if(obliterate == true || warping == true || shield == true){
				pcanbehurt = false;
				pu = true;
			}
		}
	}
	
	public void draw(Graphics g){
		g.setFont(font);
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 800, 600);
		for(int d = 0; d < rock.size(); d++){
			asteroid m = (asteroid) rock.get(d);
			m.draw(g);
		}
		b.draw(g);
		p.draw(g);
		g.setColor(Color.GRAY);
		g.drawString("Health: " + HP, 5, 50);
		g.drawString("Distance: " + dm + "m", 5, 62);
	}
	
	//create new asteroid
	public void genrock(){
		if(rock.size() < iaos){
			asteroid newrock = new asteroid(sheet, this, 3);
			rock.add(newrock);
		}
	}
	
	public void createRock(){
		asteroid newrock = new asteroid(sheet, this, 3);
		rock.add(newrock);
	}
	
	public void collisions(){
		if(Ocollide == false){
			for(int d = 0; d < rock.size(); d++){
				asteroid m = (asteroid) rock.get(d);
				if(warping == true){
					m.pwarping = true;
				}
				if(p.isdead == false){
					if(m.form().intersects(p.bound()) == true && m.exist == true && p.isdead == false){
						m.exist = false;
						rock.remove(d);
						if(pcanbehurt == true){
							HP--;
						}
						if(HP == 0){
							p.isdead = true;
							if(dm > hs){
								try{
									outputFile = new File("saveFile.txt");
									saveFile = new BufferedWriter(new FileWriter(outputFile));
									saveFile.write("\n");
									saveFile.write(dm + "\n");
									saveFile.close();
								}catch(Exception e){
									e.printStackTrace();
								}
								hs = dm;
							}
						}
					}
				}
				if(m.x > 800){
					for(int a = 0; a < rock.size() && a != d; a++){
						asteroid j = (asteroid) rock.get(a);
						if(m.form().intersects(j.form()) == true && m.x > 800){
							j.x += m.w;
						}
						if(m.x == j.x && m.x > 800){
							j.x += m.w;
						}
					}
				}
			}
		}
		if(b.form().intersects(p.bound()) && b.collided == false){
			if(b.powerup == 0){
				shield = true;
				p.shieldrequired = true;
				pcanbehurt = false;
				b.collided = true;
				pu = true;
				putrs = true;
			}
			if(b.powerup == 1){
				p.HPrequired = true;
				b.collided = true;
				HP++;
			}
			if(b.powerup == 2){
				for(int i = 0; i < rock.size(); i++){
					asteroid m = (asteroid) rock.get(i);
					m.pwarping = true;
				}
				p.shieldrequired = true;
				pcanbehurt = false;
				pu = true;
				putrw = true;
				warping = true;
				b.collided = true;
				odm = dmincrease;
			}
			if(b.powerup == 3){
				pu = true;
				obliterate = true;
				pcanbehurt = false;
				Ocollide = true;
				b.collided = true;
			}
		}
	}
	
	//key handler
	public void pressed(KeyEvent e){
		p.pressed(e);
	}
	public void released(KeyEvent e){
		p.released(e);
	}
	
	//mouse movement handler
	public void mousemovement(MouseEvent e){
		p.move(e);
	}
	//mouse input handler
	public void mdragged(MouseEvent e){
		p.dragged(e);
	}
}
