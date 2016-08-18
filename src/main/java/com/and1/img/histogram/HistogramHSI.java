package com.and1.img.histogram;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class HistogramHSI
{
	public static final float MAX_COLOUR_VALUE = 255f;

	public void generateHistogramHSI(BufferedImage image, String name)
	{
		float[][][] histogramHSI = new float[18][3][3];

		if(image != null)
		{

			for(int i = 0; i < image.getWidth(); i++)
			{
				for(int j = 0; j < image.getHeight(); j++)
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

					if(B <= G)
					{
						H = (float) ((Math.acos(x / y)));
					} else if(B > G)
					{
						H = 360f - ((float) ((Math.acos(x / y))));
					}

					//Berechnung Saturation
					float minRGB = Math.min(Math.min(R, G), B);

					float S = 1 - ((3 * minRGB) / (R + G + B));

					//Berechnung Intensity
					float I = ((R + G + B) / 3);

					int Hq = (int) ((H) / 20f);

					if(S * 3 < (1 / 3f))
					{
						Sq = 0;
					} else if(S * 3 < (2 / 3f))
					{
						Sq = 1;
					} else
					{
						Sq = 2;
					}

					if(I * 3 < 85)
					{
						Iq = 0;
					} else if(I * 3 < 170)
					{
						Iq = 1;
					} else
					{
						Iq = 2;
					}

					histogramHSI[Hq][Sq][Iq]++;

				}
			}
			saveHistogramHSI(histogramHSI, name);

		}
	}


	private void saveHistogramHSI(float[][][] histogram, String name)
	{

		String filename = name.substring(0, name.length() - 4) + "-HSI.txt";
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
