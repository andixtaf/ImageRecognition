package com.and1.model.img.histogram;

import com.and1.Persistance;
import com.and1.model.img.Image;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.image.BufferedImage;

public class HistogramGray
{
	private static final int INT_8_BIT = 256;
	private static final Logger logger = LogManager.getLogger(HistogramRGB.class);

	private float[] histogram = null;

	//TODO path should be read from property file
	private final Persistance persistance = new Persistance("PersistedHistograms");

	public float[] getHistogram(Image image)
	{
		if(histogram == null)
		{
			if(persistance.existsPersistedFile(image.getImageName(), "-Gray.txt"))
			{
				logger.info("load File: " + image.getImageName());
				histogram = persistance.getHistogramGray(image.getFilePath().getName());
			} else {

				histogram = generateHistogram(image.getImage());

				persistance.saveHistogramGray(histogram, image.getImageName());

				logger.info("created and saved file: " + image.getImageName());
			}
		}

		return histogram;
	}


	private float[] generateHistogram(BufferedImage image)
	{
		float[] histogramGray = new float[INT_8_BIT];
		int[] pixel = null;

		int countOfTotalPixel = 0;
		for (int x = 0; x < image.getWidth(); x++)
		{
			for (int y = 0; y < image.getHeight(); y++)
			{
				pixel = image.getRaster().getPixel(x, y, pixel);

				histogramGray[pixel[0]]++;
				countOfTotalPixel++;
			}
		}

		normalize(histogramGray, countOfTotalPixel);

		return histogramGray;

	}


	private float[] normalize(float[] histogram, int countOfTotalPixel)
	{
		for (int i = 0; i < INT_8_BIT; i++)
		{
			histogram[i] = (histogram[i] / countOfTotalPixel) * 100;
		}

		return histogram;
	}

}
