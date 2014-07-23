package oop.tankGame;

/*
 * Programmer(s): Talon; Colton
 * Date: Nov 10, 2008
 * Project: Networked
 * Package: good
 * FileName:Player.java
 * Description:
 */

import java.awt.image.BufferedImage;
import javax.swing.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;

public class TankPaint extends JPanel implements MouseListener, ActionListener,
		MouseMotionListener {
	static BufferedImage background;
	private BufferedImage top, base, tread, bullet;
	Image explode = Toolkit.getDefaultToolkit().getImage(
			Option.imageDir + Option.FILE_SEPARATOR + "ex1.gif");
	private Graphics g;

	static int backgroundX, backgroundY;
	static double baseAngle, topAngle;
	int treadNum = 1;

	private int beginX;
	private int beginY;

	// ///
	Player player;

	Timer t;
	public int goalX;
	public int goalY;
	int tempXstart = 0;
	int tempYstart = 0;
	int bulletXStart = 0;
	int bulletYStart = 0;

	private Point cursorLoc = new Point(-20, -20); // local variable for being
													// able to get cursor
													// location anywhere

	int blowupRange;

	// ///

	public TankPaint(String back, String top, String base, String tread,
			String bullet) {
		super();

		try {
			String backgroundPath = Option.backgroundDir
					+ Option.FILE_SEPARATOR;
			String imagePath = Option.imageDir + Option.FILE_SEPARATOR;
			this.background = ImageIO.read(new File(backgroundPath + back));
			this.top = ImageIO.read(new File(imagePath + top));
			this.base = ImageIO.read(new File(imagePath + base));
			this.tread = ImageIO.read(new File(imagePath + tread));
			this.bullet = ImageIO.read(new File(imagePath + bullet));
		} catch (IOException ex) {
			System.out.println("Error as loading images!" + ex);
		}
		this.backgroundX = 0;
		this.backgroundY = 0;
		this.baseAngle = 0;
		this.topAngle = 0;
		addMouseListener(this);
		// ////
		t = new Timer(20, this);
		loadBeginPoints();
		// player = new Player(beginX, beginY, this.background.getWidth(),
		// this.background.getHeight());
		player = new Player(400, 600, this.background.getWidth(),
				this.background.getHeight());
		player.setSpeedModifier(5);

		addMouseMotionListener(this);
		// ////
	}

	private void loadBeginPoints() {
		// 181, 165, 213 is rgb of beginning location

		for (int i = background.getWidth() - 1; i >= 0; i--) {
			for (int j = background.getHeight(); j >= 0; j--) {
				try {
					int aux = TankPaint.background.getRGB(i, j);

					int red = ((aux & 0x00FF0000) >>> 16); // Red level
					int green = ((aux & 0x0000FF00) >>> 8); // Green level
					int blue = (aux & 0x000000FF); // Blue level

					if (red == 181 && green == 165 && blue == 213) {
						beginX = i;
						beginY = j;
					}
				} catch (Exception pedophile) {
				}
			}

		}

	}

	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform transform = new AffineTransform();

		g.drawImage(background, backgroundX, backgroundY, this);

		// drawing bullet part
		if (player.isShooting) {
			transform.setToTranslation(player.bulletXCur, player.bulletYCur);
			g2d.drawImage(bullet, transform, this);
		}

		transform.setToTranslation(player.x - tread.getWidth() / 2 + 15,
				player.y - 5 - tread.getHeight() / 2 + 25);
		transform.rotate(Math.toRadians(baseAngle), tread.getWidth() / 2,
				tread.getHeight() / 2 - 10);
		g2d.drawImage(tread, transform, this);
		g2d.drawImage(base, transform, this);

		transform.setToTranslation(player.x - tread.getWidth() / 2 + 15,
				player.y - tread.getHeight() / 2 + 21);
		transform.rotate(Math.toRadians(topAngle), top.getWidth() / 2,
				top.getHeight() / 2 - 25);
		g2d.drawImage(top, transform, this);
		g.drawLine(player.x, player.y, goalX, goalY);
		if (player.explosion) {
			g.drawImage(explode, player.eX, player.eY, this);
		}

		// debugging: area where explosion may occur
		int tempDist = (int) (new Point(player.x, player.y).distance(cursorLoc));
		int radius = (int) (.0007628459470397 * Math.pow(tempDist, 2)
				- .09954558648855 * tempDist + 120.939080166846);
		g.drawOval((int) (cursorLoc.x - (.43 * radius)),
				(int) (cursorLoc.y - (.43 * radius)), radius, radius);

		blowupRange = radius / 2;
		// </debugging>
	}

	private void move() {
		player.moveToPoint(new Point(tempXstart, tempYstart), new Point(goalX,
				goalY));
		if (player.reachedGoal && player.bulletReachGoal) {
			t.stop();
		}

		if (!player.reachedGoal) {
			try {
				treadNum++;
				if (treadNum == 4)
					treadNum = 1;
				this.tread = ImageIO
						.read(new File(Option.imageDir + Option.FILE_SEPARATOR
								+ "tread " + treadNum + ".gif"));
			} catch (IOException ex) {
				System.out.println("Error loading images: " + ex.getMessage());
			}

		}

		repaint();
	}

	private void bulletMove() {
		player.shootToPoint(new Point(bulletXStart, bulletYStart), new Point(
				player.eX, player.eY));
		if (player.reachedGoal && player.bulletReachGoal) {
			t.stop();
		}
		repaint();
	}

	public void start() {
		t.start();
	}

	public void actionPerformed(ActionEvent arg0) {
		// System.out.println("Got into actionperformed");
		if (!player.reachedGoal)
			move();
		if (!player.bulletReachGoal)
			bulletMove();
		repaint();
	}

	public void stop() {
		t.stop();
	}

	public void mouseClicked(MouseEvent m) {

	}

	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void mousePressed(MouseEvent m) {
		if (m.getButton() == m.BUTTON3) {
			player.reachedGoal = false;

			goalX = m.getX();
			goalY = m.getY();
			tempXstart = player.x;
			tempYstart = player.y;
			Point temp1 = new Point(player.x, player.y);
			Point temp2 = new Point(goalX, goalY);
			if (temp1.distance(temp2) > player.speedMod * 2) {
				goalX = m.getX();
				goalY = m.getY();
				t.start();
			} else {
				goalX = player.x;
				goalY = player.y;
			}
			t.start();
			// System.out.println("Timer started? " + t.isRunning());
		} else if (m.getButton() == m.BUTTON2) {
			t.stop();
			if (player.explosion) {
				player.explosion = false;

			}
		}

		else if (m.getButton() == m.BUTTON1) {

			player.bulletReachGoal = false;
			player.explosion = false;
			// where explosion x and y get set
			Point explodePoint = getExplosionPoint(new Point(m.getX(), m.getY()));
			player.eX = (int) explodePoint.getX();// m.getX() - 20;
			player.eY = (int) explodePoint.getY();// m.getY() - 20;
			bulletXStart = player.x;
			bulletYStart = player.y;
			player.bulletXCur = bulletXStart;
			player.bulletYCur = bulletYStart;
			player.isShooting = true;
			t.start();
			// eX = m.getX() - 20;
			// eY = m.getY() - 30;
			repaint();

		}

	}

	private Point getExplosionPoint(Point center) {
		Point finalSelection = null;// new Point();
		int centerX = (int) center.getX();
		int centerY = (int) center.getY();
		Random rand = new Random();

		float vecX = rand.nextFloat() * 2 - 1;
		float vecY = rand.nextFloat() * 2 - 1;
		float vecLeng = (float) Math.sqrt(vecX * vecX + vecY * vecY);
		if (vecLeng < 0.01f) {
			vecX = 1;
			vecLeng = (float) Math.sqrt(vecX * vecX + vecY * vecY);
		}
		float normVecX = vecX / vecLeng;
		float normVecY = vecY / vecLeng;

		float randRadius = blowupRange * rand.nextFloat();
		int finalX = (int) (normVecX * randRadius + centerX);
		int finalY = (int) (normVecY * randRadius + centerY);
		finalSelection = new Point(finalX - 20, finalY - 30);
		return finalSelection;

	}

	public void mouseReleased(MouseEvent m) {

	}

	// /////

	public void mouseMoved(MouseEvent e) {

		double x1 = e.getX();
		double y1 = e.getY();
		cursorLoc = new Point(e.getX(), e.getY());
		x1 = x1 - player.x;
		y1 = y1 - player.y;

		if (x1 >= 0) {
			topAngle = Math.toDegrees(Math.atan(y1 / x1)) - 90;
		} else {
			topAngle = Math.toDegrees(Math.atan(y1 / x1)) + 90; // tan^-1(slopeAsRatio)
		}
		repaint();

	}

	public void mouseDragged(MouseEvent e) {
	}

	public static void main(String[] args) {
		Tanks f = new Tanks();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(1280, 1025);
		f.setResizable(false);
		f.setTitle("Battle Tanks!");
		f.setLocation(0, 0);
		f.setVisible(true);
	}
}
