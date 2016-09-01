package com.and1.model.img.histogram;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Histogram
{
	private static final Logger logger = LogManager.getLogger(Histogram.class);

	private static final int INT_8_BIT = 256;

	public void generateHistogramGray(BufferedImage image, String name)
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

		saveHistogramGray(histogramGray, name);
	}

	private void normalize(float[] histogram, int countOfTotalPixel)
	{
		for (int i = 0; i < INT_8_BIT; i++)
		{
			histogram[i] = (histogram[i] / countOfTotalPixel) * 100;
		}
	}

	// TODO save histogram files in a single dir - maybe delete old files
	private void saveHistogramGray(float[] histogram, String name)
	{
		String pathToSave = "PersistedHistograms/";

		File file = new File(pathToSave + name.substring(0, name.length() - 4) + "-Gray.txt");

		pathToSave += file.getName();

		File fileToSave = new File(pathToSave);

		if(!fileToSave.exists())
		{
			try
			{
				//von ImageName.jpg den .jpg abschneiden und mit -Gray.txt ersetzen
				ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(fileToSave.getAbsolutePath()));
				output.writeObject(histogram);
				output.close();
			}
			catch (IOException e)
			{
				logger.error("save Histogram Gray: " + e);
			}
		}
	}
}

