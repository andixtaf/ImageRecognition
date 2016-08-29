package com.and1.view;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.io.File;

class Main
{
	private static final Logger logger = LogManager.getLogger(Main.class);

	public static void main(String[] args)
	{
		logger.info("start application");

		logger.info("JarPath: " + generateJarPath());
		System.out.println("JarPath: " + generateJarPath());

		ImageSplashScreen ImageSplashScreen = new ImageSplashScreen();
		ImageSplashScreen.setVisible(true);

		Thread splashThread = new Thread(ImageSplashScreen);
		splashThread.start();

		MainFrame frame = new MainFrame(ImageSplashScreen, splashThread);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		logger.info("finish!!!");
	}

	public static String generateJarPath()
	{
		File file = new File(System.getProperty("java.class.path"));
		File dir = file.getAbsoluteFile().getParentFile();

		return dir.toString() + "/";
	}
}
