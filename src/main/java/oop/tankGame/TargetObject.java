package main.java.oop.tankGame;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import javax.swing.*;

public class TargetObject {

	// public String name;
	public boolean isDestroyable;
	public boolean isVisible;
	public BufferedImage image;
	public int posX, posY, lengX, lengY;

	public TargetObject(BufferedImage pImage, boolean pDestroyable, int pPosX,
			int pPosY, int lx, int ly) {
		// name = pName;
		image = pImage;
		isDestroyable = pDestroyable;
		isVisible = true;
		posX = pPosX;
		posY = pPosY;
		lengX = lx;
		lengY = ly;
	}

	public void Draw(Graphics g, ImageObserver imageObserver) {
		if (isVisible)
			g.drawImage(image, posX, posY, imageObserver);
	}

}
