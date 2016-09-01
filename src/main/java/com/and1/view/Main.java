package com.and1.view;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

class Main
{
	private static final Logger logger = LogManager.getLogger(Main.class);

	private static final Properties properties = new Properties();

	public static void main(String[] args)
	{
		logger.info("start application");

		Properties properties = loadConfig();

		ImageSplashScreen ImageSplashScreen = new ImageSplashScreen();
		ImageSplashScreen.setVisible(true);

		Thread splashThread = new Thread(ImageSplashScreen);
		splashThread.start();

		MainFrame frame = new MainFrame(ImageSplashScreen, splashThread, properties);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		logger.info("finish!!!");
	}

	private static Properties loadConfig()
	{
		String configFile = "Main.properties";

		Properties properties = new Properties();

		InputStream inputStream = null;

		try {

			inputStream = new FileInputStream(configFile);

			properties.load(inputStream);

		} catch (IOException ex) {
			logger.error("loading Config: " + ex);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					logger.error("closing Input Stream: " + e);
				}
			}
		}

		return properties;
	}

	public static String generateJarPath()
	{
		File file = new File(System.getProperty("java.class.path"));
		File dir = file.getAbsoluteFile().getParentFile();

		return dir.toString() + "/";
	}

	public static Properties getProperties()
	{
		return properties;
	}
}
