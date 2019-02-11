package wave.main;

import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.event.MouseInputAdapter;

import wave.gameState.gameState;

public class Main extends JFrame implements Runnable{
	int tickCount = 0;
	int frames, ticks;
	boolean shouldRender, fpsCap = false, canSwitch = true;
	public int x = 800, y = 600;
	BufferedImage sheet;
	String version = "1.3";
	
	gameState gamestate;
	
	ImageIcon Icon = new ImageIcon(getClass().getResource("Picture of me 4.png"));
	
	public void init(){
		try{
			sheet = ImageIO.read(getClass().getResource("Spritesheet 3.0.gif"));
		}catch(Exception e){
			e.printStackTrace();
		}
		gamestate = new gameState(0, sheet);
	}
	
	public Main(){
		//setTitle("Dodge This BY: Trevor Crow || Build: " + version + " || FPS:" + frames + " Ticks:" + ticks);
		setTitle("Dodge This BY: Trevor Crow || FPS:" + frames + " Ticks:" + ticks);
		setSize(x, y);
		setIconImage(Icon.getImage());
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setFocusable(true);
		setResizable(false);
		setLocationRelativeTo(null);
		addKeyListener(new keyboard());
		addMouseMotionListener(new mousemovement());
		addMouseListener(new mouseinput());
		init();
	}
	
	public void run(){
		long lastTime = System.nanoTime();
		double nsPerTick = 1000000000D / 60D;
		
		frames = 0;
		ticks = 0;
		
		long lastTimer = System.currentTimeMillis();
		double delta = 0;
		while(true){
			long now = System.nanoTime();
			delta += (now - lastTime)/ nsPerTick;
			lastTime = now;
			if(fpsCap == true){
				shouldRender = false;
			}
			
			while(delta >= 1){
				ticks++;
				tick();
				delta -= 1;
				shouldRender = true;
			}
			if(shouldRender == true){
				frames++;
				render();
			}
			if(System.currentTimeMillis() - lastTimer >= 1000){
				lastTimer += 1000;
				setTitle("Dodge This BY: Trevor Crow || FPS:" + frames + " Ticks:" + ticks);
				frames = 0;
				ticks = 0;
			}
		}
	}
	
	public void tick(){
		tickCount++;
		update();
	}
	
	public void update(){
		gamestate.update();
	}
	
	public void render(){
		BufferStrategy bs = getBufferStrategy();
		if(bs == null){
			createBufferStrategy(4);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		gamestate.draw(g);
		g.dispose();
		bs.show();
	}
	
	private class keyboard extends KeyAdapter{
		public void keyPressed(KeyEvent e){
			gamestate.pressed(e);
			if(e.getKeyCode() == e.VK_BACK_SPACE){
				if(fpsCap == false && canSwitch == true){
					fpsCap = true;
					canSwitch = false;
				}
				if(fpsCap == true && canSwitch == true){
					fpsCap = false;
					canSwitch = false;
				}
			}
		}
		public void keyReleased(KeyEvent e){
			gamestate.released(e);
			if(e.getKeyCode() == e.VK_BACK_SPACE){
				canSwitch = true;
			}
		}
	}
	private class mousemovement extends MouseInputAdapter{
		public void mouseMoved(MouseEvent e){
			gamestate.mmove(e);
		}
		public void mouseDragged(MouseEvent e){
			gamestate.mdragged(e);
		}
	}
	private class mouseinput implements MouseListener{
		public void mousePressed(MouseEvent e){
			gamestate.mdragged(e);
		}
		public void mouseClicked(MouseEvent e) {
			
		}
		public void mouseEntered(MouseEvent e) {
			
		}
		public void mouseExited(MouseEvent e) {
			
		}
		public void mouseReleased(MouseEvent e) {
			
		}
	}
	
	public static void main(String[] args){
		Main game = new Main();
		Thread gl = new Thread(game);
		gl.start();
	}
}
