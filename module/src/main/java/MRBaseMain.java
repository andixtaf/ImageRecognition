import javax.swing.*;

public class MRBaseMain {

	/**
	 * Main entry point for the executable
	 * @param args currently ignored
	 */
	public static void main(String[] args) {
		// display a splash screen
		SplashScreen splash = new SplashScreen();
		splash.setVisible(true);
		Thread splashThread = new Thread(splash);
		splashThread.start();

		MainFrame frame = new MainFrame("Multimedia Retrieval BTU Base",
		                                splash, splashThread);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}
}