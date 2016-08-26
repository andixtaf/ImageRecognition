package com.and1.view;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;

class Main
{
	private static final Logger logger = LogManager.getRootLogger();

	public static void main(String[] args)
	{
		logger.info("start application");

		ImageSplashScreen ImageSplashScreen = new ImageSplashScreen();
		ImageSplashScreen.setVisible(true);

		Thread splashThread = new Thread(ImageSplashScreen);
		splashThread.start();

		MainFrame frame = new MainFrame(ImageSplashScreen, splashThread);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		logger.info("finish!!!");
	}

}
