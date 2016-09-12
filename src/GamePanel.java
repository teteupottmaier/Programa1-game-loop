import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.awt.image.*;
import javax.imageio.ImageIO;


public class GamePanel extends JPanel implements Runnable
{
private static final int PWIDTH = 640;
private static final int PHEIGHT = 480;
private Thread animator;
private boolean running = false;
private boolean gameOver = false; 

private BufferedImage dbImage;
private Graphics2D dbg;



int FPS,SFPS;
int fpscount;

Random rnd = new Random();

BufferedImage imagemcharsets;
BufferedImage fundo;

boolean LEFT, RIGHT,UP,DOWN;

int MouseX,MouseY;

int diftime;

float x,y;
float x2,y2;

int vel = 100;

int vel2 = 80;

int objetivoX = 0;
int objetivoY = 0;



public GamePanel()
{

	setBackground(Color.white);
	setPreferredSize( new Dimension(PWIDTH, PHEIGHT));

	// create game components
	setFocusable(true);

	requestFocus(); // JPanel now receives key events
	
	if (dbImage == null){
		dbImage = new BufferedImage(PWIDTH, PHEIGHT,BufferedImage.TYPE_4BYTE_ABGR);
		if (dbImage == null) {
			System.out.println("dbImage is null");
			return;
		}else{
			dbg = (Graphics2D)dbImage.getGraphics();
			
		}
	}	
	
	
	// Adiciona um Key Listner
	addKeyListener( new KeyAdapter() {
		public void keyPressed(KeyEvent e)
			{ 
				int keyCode = e.getKeyCode();
				
				if(keyCode == KeyEvent.VK_LEFT){
					LEFT = true;
				}
				if(keyCode == KeyEvent.VK_RIGHT){
					RIGHT = true;
				}
				if(keyCode == KeyEvent.VK_UP){
					UP = true;
				}
				if(keyCode == KeyEvent.VK_DOWN){
					DOWN = true;
				}	
			}
		@Override
			public void keyReleased(KeyEvent e ) {
				int keyCode = e.getKeyCode();
				
				if(keyCode == KeyEvent.VK_LEFT){
					LEFT = false;
				}
				if(keyCode == KeyEvent.VK_RIGHT){
					RIGHT = false;
				}
				if(keyCode == KeyEvent.VK_UP){
					UP = false;
				}
				if(keyCode == KeyEvent.VK_DOWN){
					DOWN = false;
				}
			}
	});
	
	addMouseMotionListener(new MouseMotionListener() {
		
		@Override
		public void mouseMoved(MouseEvent e) {
			// TODO Auto-generated method stub
			MouseX = e.getX();
			MouseY = e.getY();
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub
			MouseX = e.getX();
			MouseY = e.getY();
		}
	});
	
	addMouseListener(new MouseListener() {
		
		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			objetivoX = MouseX;
			objetivoY = MouseY;
		}
		
		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
	});
	
	
	try {
		fundo = ImageIO.read( getClass().getResource("4.jpg") );
	}
	catch(IOException e) {
		System.out.println("Load Image error:");
	}
	
	MouseX = MouseY = 0;
	
	x = 10;
	y = 100;
	x2 = 10;
	y2 = 200;

} // end of GamePanel()

public void addNotify()
{
	super.addNotify(); // creates the peer
	startGame(); // start the thread
}

private void startGame()
// initialise and start the thread
{
	if (animator == null || !running) {
		animator = new Thread(this);
		animator.start();
	}
} // end of startGame()

public void stopGame()
// called by the user to stop execution
{ running = false; }


public void run()
/* Repeatedly update, render, sleep */
{
	running = true;
	
	long DifTime,TempoAnterior;
	
	int segundo = 0;
	DifTime = 0;
	TempoAnterior = System.currentTimeMillis();
	
	while(running) {
	
		gameUpdate(DifTime); // game state is updated
		gameRender(); // render to a buffer
		paintImmediately(0, 0, 640, 480); // paint with the buffer
	
		try {
			Thread.sleep(0); // sleep a bit
		}	
		catch(InterruptedException ex){}
		
		DifTime = System.currentTimeMillis() - TempoAnterior;
		TempoAnterior = System.currentTimeMillis();
		
		if(segundo!=((int)(TempoAnterior/1000))){
			FPS = SFPS;
			SFPS = 1;
			segundo = ((int)(TempoAnterior/1000));
		}else{
			SFPS++;
		}
	
	}
System.exit(0); // so enclosing JFrame/JApplet exits
} // end of run()

int timerfps = 0;

private void gameUpdate(long DiffTime)
{ 
	diftime = (int)DiffTime;
	
//	x++;
//	if(x>640){
//		x = 0;
//	}
//	
//	x2 = x2 + 150*DiffTime/1000.0f;
//	
//	if(x2>640){
//		x2 = 0;
//	}
	
	if(LEFT){
		x2 -= vel*DiffTime/1000.0f;
	}
	if(RIGHT){
		x2 += vel*DiffTime/1000.0f;
	}
	if(UP){
		y2 -= vel*DiffTime/1000.0f;
	}
	if(DOWN){
		y2 += vel*DiffTime/1000.0f;
	}
	
	float dx = objetivoX-x;
	float dy = objetivoY-y;
	
	double ang = Math.atan2(dy, dx);
	
	
	x += vel2*Math.cos(ang)*DiffTime/1000.0f;
	y += vel2*Math.sin(ang)*DiffTime/1000.0f;
	
	if(x>640){
		x = 0;
	}
}


private void gameRender()
// draw the current frame to an image buffer
{
	dbg.setColor(Color.white);
	dbg.fillRect(0, 0, PWIDTH, PHEIGHT);
	
	//dbg.drawImage(fundo, MouseX, MouseY, null);
	dbg.drawImage(fundo, MouseX, MouseY, 200, 200, null, null);
	dbg.drawImage(fundo, MouseX, MouseY, 50, 50, null, null);
	

	
	dbg.setColor(Color.BLACK);
	dbg.fillOval((int)objetivoX,(int) objetivoY, 10, 10);
	
	dbg.setColor(Color.GREEN);
	dbg.fillRect((int)x,(int) y, 10, 10);
	
	dbg.setColor(Color.RED);
	dbg.fillRect((int)x2,(int) y2, 10, 10);
	
	
//	dbg.setColor(new Color(125,0,125,150));
//	dbg.fillRect(MouseX, MouseY, 200, 200);
	
//	int xPoints[] = new int[3];
//	int yPoints[] = new int[3];
//	
//	xPoints[0] = 100;
//	xPoints[1] = 200;
//	xPoints[2] = 300;
//	
//	yPoints[0] = 100;
//	yPoints[1] = 200;
//	yPoints[2] = 100;
	
	//int xPoints[] = {200,-200};
	//int yPoints[] = {200,-200};
	
	dbg.drawRect(200, 200, 200, 200);
	
/*	dbg.setColor(Color.LIGHT_GRAY);
	
	Polygon s = new Polygon();
	s.addPoint(100, 200);
	s.addPoint(200, 300);
	s.addPoint(300, 400);
	s.addPoint(400, 100);

	
	dbg.fill(s);*/
	
	
	dbg.setColor(Color.blue);
	dbg.drawString("FPS "+FPS+ " "+MouseX+" "+MouseY+" LEFT "+LEFT+" RIGHT "+RIGHT, 10, 20);
}


public void paintComponent(Graphics g)
{
	super.paintComponent(g);
	if (dbImage != null)
		g.drawImage(dbImage, 0, 0, null);
}


public static void main(String args[])
{
	GamePanel ttPanel = new GamePanel();

  // create a JFrame to hold the timer test JPanel
  JFrame app = new JFrame("Swing Timer Test");
  app.getContentPane().add(ttPanel, BorderLayout.CENTER);
  app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

  app.pack();
  app.setResizable(false);  
  app.setVisible(true);
} // end of main()

} // end of GamePanel class

