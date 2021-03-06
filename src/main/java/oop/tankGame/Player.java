package main.java.oop.tankGame;

/*
 * Programmer(s): Talon; Colton
 * Date: Nov 10, 2008
 * Project: Networked
 * Package: good
 * FileName:Player.java
 * Description:
 */

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class Player {
	public int eX = 0; // explosion coords
	public int eY = 0;
	public int x = 200;
	public int y = 200;
	public int width = 30; // default value
	public int height = 30; // default value
	public boolean reachedGoal = true;
	public boolean bulletReachGoal = true;
	private int speed = 1;
	public int speedMod = 5;
	private int bulletSpeed = 28;
	private Point self;
	private int xRestriction;
	private int yRestriction;

	public int bulletXCur = 0;
	public int bulletYCur = 0;
	public boolean isShooting = false;
	public boolean explosion = false;

	/**
	 * @param xRes
	 *            the X restriction, usually a getWidth()
	 * @param yRes
	 *            the Y restriction, usually a getHeight()
	 */
	public Player(int x, int y, int xRes, int yRes) {
		this.x = x;
		this.y = y;
		self = new Point(x, y);
		xRestriction = xRes;
		yRestriction = yRes;

	}

	public void shootToPoint(Point a, Point b) {
		if (!bulletReachGoal) {
			bulletReachGoal = false;
			double distance = a.distance(b);
			double steps = Math.round(distance / (bulletSpeed));
			double deltaY = Math.round((b.getY() - a.getY()) / steps);
			double deltaX = Math.round((b.getX() - a.getX()) / steps);
			int nextX = bulletXCur;
			int nextY = bulletYCur;
			if (Double.isNaN(deltaX) || Double.isNaN(deltaY)) {

			} else if (deltaX > xRestriction) {
				deltaX = 1;
			} else if (deltaY > yRestriction) {
				deltaY = 1;
			}

			if (checkMove((int) (bulletXCur + deltaX),
					(int) (bulletYCur + deltaY))) {
				nextX = ((int) (bulletXCur + deltaX));
				nextY = ((int) (bulletYCur + deltaY));

			} else {
				bulletReachGoal = true;
				destroyObject((int) (bulletXCur + deltaX),
						(int) (bulletYCur + deltaY));
				isShooting = false;
				explosion = true; // hit something, hence, blow up
				eX = (int) (bulletXCur + deltaX * 3);
				eY = (int) (bulletYCur + deltaY * 3);
			}

			/*
			 * if(deltaX>0 && deltaY>0) TankPaint.deg1 = 0; if(deltaX<0 &&
			 * deltaY>0) TankPaint.deg1 = 90; if(deltaX<0 && deltaY<0)
			 * TankPaint.deg1 = 180; if(deltaX>0 && deltaY<0) TankPaint.deg1 =
			 * 270;
			 */
			bulletXCur = nextX;
			bulletYCur = nextY;

			double distToGoal = new Point(bulletXCur, bulletYCur).distance(b);
			if (distToGoal <= 30) {
				bulletReachGoal = true;
				isShooting = false;
				explosion = true; // reach goal hence blow up
			}
		}
	}

	public void moveToPoint(Point a, Point b) {
		if (!reachedGoal) {
			reachedGoal = false;
			self = a;// new Point(x, y);
			double distance = a.distance(b);
			double steps = Math.round(distance / (speedMod * speed));
			double deltaY = Math.round((b.getY() - a.getY()) / steps);
			double deltaX = Math.round((b.getX() - a.getX()) / steps);
			int nextX = this.x;
			int nextY = this.y;
			if (Double.isNaN(deltaX) || Double.isNaN(deltaY)) {

			} else if (deltaX > xRestriction) {
				deltaX = 1;
			} else if (deltaY > yRestriction) {
				deltaY = 1;
			}
			if (checkMove((int) (this.x + deltaX), (int) (this.y + deltaY))) {
				nextX = ((int) (this.x + deltaX));
				nextY = ((int) (this.y + deltaY));

				if (deltaX >= 0)
					TankPaint.baseAngle = Math.toDegrees(Math.atan(deltaY
							/ deltaX)) - 90;

				else
					TankPaint.baseAngle = Math.toDegrees(Math.atan(deltaY
							/ deltaX)) + 90;

			} else
				reachedGoal = true;

			/*
			 * if(deltaX>0 && deltaY>0) TankPaint.deg1 = 0; if(deltaX<0 &&
			 * deltaY>0) TankPaint.deg1 = 90; if(deltaX<0 && deltaY<0)
			 * TankPaint.deg1 = 180; if(deltaX>0 && deltaY<0) TankPaint.deg1 =
			 * 270;
			 */
			this.x = nextX;
			this.y = nextY;

			double distToGoal = new Point(this.x, this.y).distance(b);
			if (distToGoal <= 80) {
				reachedGoal = true;
			}
			System.out.println(distToGoal); // debgugging
		}
	}

	public void destroyObject(int x, int y) {
		System.out.println("about to destroy object at " + x + ", " + y);
		for (int xx = x - TankPaint.backgroundX - 20; xx < x
				- TankPaint.backgroundX + 50; xx++) {
			for (int yy = y - TankPaint.backgroundY - 20; yy < y
					- TankPaint.backgroundY + 60; yy++) {
				try {
					if (TankPaint.Instance.CheckTargetObject(xx, yy)) {
						TankPaint.Instance.DestroyTargetObject(xx, yy);
						return;
					}

				} catch (Exception rapist) {
				}

			}
		}

	}

	public boolean checkMove(int x, int y) {
		boolean wall = true;

		for (int xx = x - TankPaint.backgroundX - 20; xx < x
				- TankPaint.backgroundX + 50; xx++) {
			for (int yy = y - TankPaint.backgroundY - 20; yy < y
					- TankPaint.backgroundY + 60; yy++) {
				try {
					if (TankPaint.Instance.CheckTargetObject(xx, yy))
						wall = false;

					int aux = TankPaint.background.getRGB(xx, yy);

					int red = ((aux & 0x00FF0000) >>> 16); // Red level
					int green = ((aux & 0x0000FF00) >>> 8); // Green level
					int blue = (aux & 0x000000FF); // Blue level

					if (red <= 0 && green == 0 && blue == 0) {

						xx = x + 1000;
						yy = y + 1000;

						wall = false;

					}
				} catch (Exception rapist) {
				}
			}

		}
		return wall;
	}

	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public void setSpeedModifier(int speedMod) {
		this.speedMod = speedMod;
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
