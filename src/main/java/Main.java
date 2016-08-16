import GuiElements.MainFrame;

import javax.swing.*;

class Main
{
	public static void main(String[] args)
	{
		SplashScreen splash = new SplashScreen();
		splash.setVisible(true);

		Thread splashThread = new Thread(splash);
		splashThread.start();

		MainFrame frame = new MainFrame(splash, splashThread);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
}
