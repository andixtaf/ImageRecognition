package com.and1.model.img.histogram;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class HistogramColour implements HistogramInterface
{
	private static final Logger logger = LogManager.getLogger(HistogramColour.class);

	public void generateHistogramRGB(BufferedImage image, String name)
	{
		if (image != null)
		{

			float[][][] histogramRGB = new float[8][8][8];

			for (int x = 0; x < image.getWidth(); x++)
			{
				for (int y = 0; y < image.getHeight(); y++)
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
		File file = new File(name.substring(0, name.length() - 4) + "-RGB.txt");
		if (!file.exists())
		{
			try
			{
				ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(file));
				output.writeObject(histogram);
				output.close();
			}
			catch (IOException e)
			{
				logger.error("save Histogram RGB: " + e);
			}
		}
	}
}
