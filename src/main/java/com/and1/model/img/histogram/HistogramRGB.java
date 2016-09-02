package com.and1.model.img.histogram;

import com.and1.Persistance;
import com.and1.model.img.Image;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.image.BufferedImage;

public class HistogramRGB
{
	private static final Logger logger = LogManager.getLogger(HistogramRGB.class);

	private float[][][] histogram = null;

	//TODO path should be read from property file
	private final Persistance persistance = new Persistance("PersistedHistograms");

	public float[][][] getHistogram(Image image)
	{
		if(histogram == null)
		{
			if(persistance.existsPersistedFile(image.getImageName(), "-RGB.txt"))
			{
				logger.info("load File: " + image.getImageName());
				histogram = persistance.getHistogramRGB(image.getFilePath().getName());
			} else {

				histogram = generateHistogram(image.getImage());

				persistance.saveHistogramRGB(histogram, image.getImageName());

				logger.info("created and saved file: " + image.getImageName());
			}
		}

		return histogram;
	}

	private float[][][] generateHistogram(BufferedImage image)
	{
		float[][][] histogramRGB = new float[8][8][8];

		if (image != null)
		{
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
		}

		return histogramRGB;
	}
}
