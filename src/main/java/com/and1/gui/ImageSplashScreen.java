package com.and1.gui;

import javax.swing.*;
import java.awt.*;

public class ImageSplashScreen extends JWindow implements Runnable
{

	private String message = "Loading...";

	public void run()
	{
		setSize(400, 400);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void paint(Graphics g)
	{
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());

		g.setColor(Color.BLACK);
		g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
		g.drawString("Multimedia Retrieval BTU Base", 15, getHeight() / 2);
		g.drawString(message, 15, this.getHeight() - 15);
	}

	public void setMessage(String msg)
	{
		message = msg;
		repaint();
	}
}