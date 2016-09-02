package com.and1.model.img;

import com.and1.model.img.histogram.HistogramGray;
import com.and1.model.img.histogram.HistogramHSI;
import com.and1.model.img.histogram.HistogramRGB;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Image
{
	private static final Logger logger = LogManager.getLogger(Image.class);

	private static final float MAX_COLOUR_VALUE = 255f;

	private List<float[]> colors;

	private List<Float> hsiColorCount;

	private Float similarity;

	private Image queryImage;

	private final File filePath;

	private final BufferedImage image;

	private final java.awt.Image thumbnail;

	private final HistogramGray histogramGray = new HistogramGray();
	private final HistogramRGB histogramRGB = new HistogramRGB();
	private final HistogramHSI histogramHSI = new HistogramHSI();

	//TODO refactor logic in new class
	public Image(File file, BufferedImage img)
	{
		filePath = file;
		image = img;
		similarity = 1.0f;
		queryImage = this;
		thumbnail = image.getScaledInstance(-1, 60, java.awt.Image.SCALE_FAST);
	}

	// TODO should work with a single given number and not power of 2
	public List<BufferedImage> generateRasterInGivenSteps(int segmentationStep)
	{
		List<BufferedImage> segment = new ArrayList<>();

		String bit = Integer.toBinaryString(segmentationStep);
		Integer bitLength = bit.length();

		int sqrtOfOriginalSize = getSqrtOfOriginalSize(segmentationStep, bitLength);

		int x = 0;
		int y = 0;
		int z = 0;

		Integer wide;

		Integer height;
		if (bitLength % 2 == 0)
		{
			height = image.getHeight() / (sqrtOfOriginalSize / 2);
			wide = image.getWidth() / (sqrtOfOriginalSize / 2);
		}
		else
		{
			height = image.getHeight() / (sqrtOfOriginalSize);
			wide = image.getWidth() / sqrtOfOriginalSize;
		}

		for (int i = 0; i < segmentationStep; i++)
		{
			segment.add(image.getSubimage(x, y, wide, height));

			if (z >= sqrtOfOriginalSize - 1)
			{
				x = 0;
				y += height;
				z = 0;
			}
			else
			{
				x += height;
				z++;
			}
		}

		return segment;
	}

	private int getSqrtOfOriginalSize(int segmentationStep, Integer bitLength)
	{
		Double imageSize;
		if (bitLength % 2 == 0)
		{
			imageSize = Math.sqrt(segmentationStep * 2);
		}
		else
		{
			imageSize = Math.sqrt(segmentationStep);
		}
		return imageSize.intValue();
	}

	public void generateHSIColors()
	{

		float[][][] histHSI = new float[18][3][3];

		colors = new ArrayList<>();

		hsiColorCount = new ArrayList<>();

		for (int i = 0; i < image.getWidth(); i++)
		{
			for (int j = 0; j < image.getHeight(); j++)
			{
				int Sq;
				int Iq;
				Color RGB = new Color(image.getRGB(i, j));

				float HSIColors[] = new float[3];

				float redValue = RGB.getRed() / MAX_COLOUR_VALUE;
				float greenValue = RGB.getBlue() / MAX_COLOUR_VALUE;
				float blueValue = RGB.getGreen() / MAX_COLOUR_VALUE;

				//Berechnung Hue
				float x = ((redValue - greenValue) + (redValue - blueValue)) / 2;
				float y = (float) (Math.sqrt(((redValue - greenValue) * (redValue - greenValue)) +
													 ((redValue - blueValue) * (greenValue - blueValue))));

				float Hue = 0;
				float Saturation;

				if (y == 0)
				{
					Hue = 0;
				}
				else
				{
					if (blueValue <= greenValue)
					{
						Hue = (float) ((Math.acos(x / y)));
					}
					else if (blueValue > greenValue)
					{
						Hue = 360 - ((float) ((Math.acos(x / y))));
					}
				}

				//Berechnung Saturation
				float minRGB = Math.min(Math.min(redValue, greenValue), blueValue);

				if ((redValue + greenValue + blueValue) == 0)
				{
					Saturation = 1;
				}
				else
				{
					Saturation = 1 - ((3 * minRGB) / (redValue + greenValue + blueValue));
				}

				//Berechnung Intensity
				float I = ((redValue + greenValue + blueValue) / 3);

				int Hq = (int) ((Hue) / 20f);

				if (Saturation * 3 < (1 / 3f))
				{
					Sq = 0;
				}
				else if (Saturation * 3 < (2 / 3f))
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

				float Sat = Saturation * 100;

				float Int = I * 255;

				histHSI[Hq][Sq][Iq]++;

				if (histHSI[Hq][Sq][Iq] == 1.0)
				{
					HSIColors[0] = Hue;
					HSIColors[1] = Sat;
					HSIColors[2] = Int;
				}
				colors.add(HSIColors);
			}
		}

		for (int i = 0; i < 18; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				for (int k = 0; k < 3; k++)
				{
					hsiColorCount.add(histHSI[i][j][k]);
				}
			}
		}
	}

	public BufferedImage getImage()
	{
		return image;
	}

	public java.awt.Image getThumbnail()
	{
		return this.thumbnail;
	}


	public float[] getHistogramGray(Image givenImage)
	{
		return histogramGray.getHistogram(givenImage);
	}

	public float[][][] getHistogramRGB(Image givenImage)
	{
		return histogramRGB.getHistogram(givenImage);
	}

	public float[][][] getHistogramHSI(Image givenImage)
	{
		return histogramHSI.getHistogram(givenImage);
	}

	public List getColors(int count)
	{

		//System.out.println(hsiColorCount.size());

		float[] color;
		float[] addcontent = new float[3];
		List<Object> dominantcolor = new ArrayList<>();
		//List <Float> hsicolor = hsiColorCount;
		float a = 0;

		for (int i = 0; i < hsiColorCount.size(); i++)
		{
			if (hsiColorCount.size() > count)
			{
				if (hsiColorCount.get(i).equals(a))
				{
					hsiColorCount.remove(i);
					i = 0;
				}
			}
		}
		//System.out.println("Size:" + hsicolor.size());

        /*for (int i = 0; i < hsicolor.size(); i++) {
			System.out.println(hsicolor.get(i));
        }*/

		if (hsiColorCount.size() < count)
		{
			for (int i = 0; i < count - hsiColorCount.size(); i++)
			{
				addcontent[0] = 0;
				addcontent[1] = 0;
				addcontent[2] = 0;
			}
			colors.add(addcontent);
		}

		DominantColors[] lidominantColorList = new DominantColors[hsiColorCount.size()];

		for (int i = 0; i < hsiColorCount.size(); i++)
		{
			color = colors.get(i);
			lidominantColorList[i] = new DominantColors(color, hsiColorCount.get(i));
		}

		Arrays.sort(lidominantColorList);

		for (int i = 0; i < count; i++)
		{
			dominantcolor.add(lidominantColorList[(count - 1) - i].getHSIColor());
		}

		hsiColorCount.clear();
		return dominantcolor;
	}

	public String toString()
	{
		return filePath.toString();
	}

	public int getWidth()
	{
		if (image != null)
		{
			return image.getWidth();
		}
		else
		{
			return 0;
		}
	}

	public int getHeight()
	{
		if (image != null)
		{
			return image.getHeight();
		}
		else
		{
			return 0;
		}
	}

	/**
	 * Returns the similarity value measured from the query image
	 *
	 * @return The similarity value in a range from [0,1] or -1 if the query image is not set or the value has not been recalculated
	 */
	public float getSimilarity()
	{
		if (queryImage != null)
		{
			return similarity;
		}
		else
		{
			return -1.0f;
		}
	}

	/**
	 * Sets the similarity value to a given query image
	 *
	 * @param similarity the similarity value to the query image
	 * @param q          the query image
	 */
	public void setSimilarity(float similarity, Image q)
	{
		this.similarity = similarity;
		queryImage = q;
	}

	public String getImageName()
	{
		return filePath.getName();
	}

	public File getFilePath()
	{
		return filePath;
	}
}
