import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class HistogramColour
{
	public void generateHistogramRGB(BufferedImage image, String name)
	{
		if(image != null)
		{

			float[][][] histogramRGB = new float[8][8][8];

			for(int x = 0; x < image.getWidth(); x++)
			{
				for(int y = 0; y < image.getHeight(); y++)
				{

					Color rgb = new Color(image.getRGB(x, y));

					int r = (rgb.getRed()) / 32;
					int g = (rgb.getGreen()) / 32;
					int b = (rgb.getBlue()) / 32;

					histogramRGB[r][g][b]++;
				}
			}

			saveHistogramRGB(histogramRGB, name);
		}

	}

	private void saveHistogramRGB(float[][][] histogram, String name)
	{
		//von ImageName.jpg den .jpg abschneiden und mit -RGB.txt ersetzen
		String filename = name.substring(0, name.length() - 4) + "-RGB.txt";
		File file = new File(filename);
		if(!file.exists())
		{
			try
			{
				ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(filename));
				output.writeObject(histogram);
				output.close();
			} catch(IOException ex)
			{
				ex.printStackTrace();
			}
		}
	}
}
