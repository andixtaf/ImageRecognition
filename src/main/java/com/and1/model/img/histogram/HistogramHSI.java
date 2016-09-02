package com.and1.model.img.histogram;

import com.and1.Persistance;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.image.BufferedImage;

public class HistogramHSI
{
	private static final Logger logger = LogManager.getLogger(HistogramHSI.class);

	private static final float MAX_COLOUR_VALUE = 255f;

	private float[][][] histogram = null;

	//TODO path should be read from property file
	private final Persistance persistance = new Persistance("PersistedHistograms");

	public float[][][] getHistogram(com.and1.model.img.Image image)
	{
		if(histogram == null)
		{
			if(persistance.existsPersistedFile(image.getImageName(), "-HSI.txt"))
			{
				logger.info("load File: " + image.getImageName());
				histogram = persistance.getHistogramHSI(image.getFilePath().getName());
			} else {

				histogram = generateHistogram(image.getImage());

				persistance.saveHistogramRGB(histogram, image.getImageName());

				logger.info("created and saved file: " + image.getImageName());
			}
		}

		return histogram;
	}

	public float[][][] generateHistogram(BufferedImage image)
	{
		float[][][] histogramHSI = new float[18][3][3];

		if (image != null)
		{

			for (int i = 0; i < image.getWidth(); i++)
			{
				for (int j = 0; j < image.getHeight(); j++)
				{
					int Sq;
					int Iq;
					Color RGB = new Color(image.getRGB(i, j));

					float R = RGB.getRed() / MAX_COLOUR_VALUE;
					float G = RGB.getBlue() / MAX_COLOUR_VALUE;
					float B = RGB.getGreen() / MAX_COLOUR_VALUE;

					//Berechnung Hue
					float x = ((R - G) + (R - B)) / 2;
					float y = (float) (Math.sqrt(((R - G) * (R - G)) + ((R - B) * (G - B))));

					float H = 0;

					if (B <= G)
					{
						H = (float) ((Math.acos(x / y)));
					}
					else if (B > G)
					{
						H = 360f - ((float) ((Math.acos(x / y))));
					}

					//Berechnung Saturation
					float minRGB = Math.min(Math.min(R, G), B);

					float S = 1 - ((3 * minRGB) / (R + G + B));

					//Berechnung Intensity
					float I = ((R + G + B) / 3);

					int Hq = (int) ((H) / 20f);

					if (S * 3 < (1 / 3f))
					{
						Sq = 0;
					}
					else if (S * 3 < (2 / 3f))
					{
						Sq = 1;
					}
					else
					{
						Sq = 2;
					}

					if (I * 3 < 85)
					{
						Iq = 0;
					}
					else if (I * 3 < 170)
					{
						Iq = 1;
					}
					else
					{
						Iq = 2;
					}

					histogramHSI[Hq][Sq][Iq]++;

				}
			}
//			saveHistogramHSI(histogramHSI, image.get);

		}

		return histogramHSI;
	}

}
