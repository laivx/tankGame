package oop.tankGame;

/*
 * Programmer(s): Talon; Colton
 * Date: Nov 10, 2008
 * Project: Networked
 * Package: good
 * FileName:Player.java
 * Description:
 */

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class Tanks extends JFrame {
	String back, top, base, tread, bullet;

	TankPaint mainPanel;

	public Tanks() {
		top = "top 1";
		base = "base 1";
		tread = "tread 1";
		back = "background.bmp";// "samplelevel.bmp";
		bullet = "bullet.png";

		setBackground(Color.black);

		mainPanel = new TankPaint(back, top + ".gif", base + ".gif", tread
				+ ".gif", bullet);

		Image pointer = Toolkit.getDefaultToolkit().getImage(
				Option.imageDir + Option.FILE_SEPARATOR + "pointer.gif");
		Cursor redicalCursor = Toolkit.getDefaultToolkit().createCustomCursor(
				pointer, new Point(0, 0), "redicalcursor");
		mainPanel.setCursor(redicalCursor);
		this.add(mainPanel);
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