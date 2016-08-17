import javax.swing.*;

class Main
{
	public static void main(String[] args)
	{
		MRSplashScreen MRSplashScreen = new MRSplashScreen();
		MRSplashScreen.setVisible(true);

		Thread splashThread = new Thread(MRSplashScreen);
		splashThread.start();

		MainFrame frame = new MainFrame(MRSplashScreen, splashThread);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
}
