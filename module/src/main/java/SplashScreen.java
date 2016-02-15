import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class SplashScreen extends JWindow implements Runnable {

	private String message = "Loading...";
	private BufferedImage image = null;

	public void run() {
		setSize(400, 400);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void paint(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());

		if (image != null) {
			g.drawImage(image, 0, 0, this);
		}
		//Image splashImage = getToolkit().getImage("h:/h-ff-dl.png");
		//g.drawImage(splashImage, 0, 0, this);
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
		g.drawString("Multimedia Retrieval BTU Base", 15, getHeight() / 2);
		g.drawString(message, 15, this.getHeight() - 15);
	}

	public void setMessage(String msg) {
		message = msg;
		repaint();
	}

	public void setImage(BufferedImage img) {
		image = img;
	}
}