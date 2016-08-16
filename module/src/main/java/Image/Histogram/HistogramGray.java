package image.Histogram;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class HistogramGray
{
	public static final int INT_8_BIT = 256;

	public void generateHistogramGray(BufferedImage image, String name)
	{
		float[] histogramGray = new float[INT_8_BIT];
		int[] pixel = null;

		int countOfTotalPixel = 0;
		for(int x = 0; x < image.getWidth(); x++)
		{
			for(int y = 0; y < image.getHeight(); y++)
			{
				pixel = image.getRaster().getPixel(x, y, pixel);

				histogramGray[pixel[0]]++;
				//Anzahl der Pixel des gesamten Bildes
				countOfTotalPixel++;
			}
		}

		normalize(histogramGray, countOfTotalPixel);

		saveHistogramGray(histogramGray, name);
	}

	private void normalize(float[] histogram, int countOfTotalPixel)
	{
		float sum = 0;
		for(int i = 0; i < INT_8_BIT; i++)
		{

			histogram[i] = (histogram[i] / countOfTotalPixel) * 100;
			//sum += histogram[i];
		}

		//System.out.println(sum);
	}

	private void saveHistogramGray(float[] histogram, String name)
	{

		File file = new File(name.substring(0, name.length() - 4) + "-Gray.txt");
		if(!file.exists())
		{
			try
			{
				//von ImageName.jpg den .jpg abschneiden und mit -Gray.txt ersetzen
				ObjectOutputStream output = new ObjectOutputStream(
						new FileOutputStream(name.substring(0, name.length() - 4) + "-Gray.txt"));
				output.writeObject(histogram);
				output.close();
			} catch(IOException ex)
			{
				ex.printStackTrace();
			}
		}
	}
}
