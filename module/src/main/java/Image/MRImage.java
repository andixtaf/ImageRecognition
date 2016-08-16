package image;

import image.Histogram.Histogram;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public class MRImage
{

	private static final float MAX_COLOUR_VALUE = 255f;

	protected File filePath;
	private BufferedImage image;
	private Image thumbnail;
	private java.util.List<float[]> colors;
	private java.util.List<Float> hsiColorCount;
	private float similarity;
	private MRImage queryImage;
	private Histogram histogram = new Histogram();

	public MRImage(File file, BufferedImage img)
	{
		filePath = file;
		image = img;
		similarity = 1.0f;
		queryImage = this;
		thumbnail = image.getScaledInstance(-1, 60, Image.SCALE_FAST);
	}

	public Vector<BufferedImage> generateRasterInGivenSteps(int segmentationStep)
	{
		Vector<BufferedImage> segment = new Vector<>();

		String bit = Integer.toBinaryString(segmentationStep);
		Integer bitLength = bit.length();

		int sqrtOfOriginalSize = getSqrtOfOriginalSize(segmentationStep, bitLength);

		int x = 0;
		int y = 0;
		int z = 0;

		Integer wide;

		Integer height;
		if(bitLength % 2 == 0)
		{
			height = image.getHeight() / (sqrtOfOriginalSize / 2);
			wide = image.getWidth() / (sqrtOfOriginalSize / 2);
		} else
		{
			height = image.getHeight() / (sqrtOfOriginalSize);
			wide = image.getWidth() / sqrtOfOriginalSize;
		}

		for(int i = 0; i < segmentationStep; i++)
		{
			segment.add(image.getSubimage(x, y, wide, height));

			if(z >= sqrtOfOriginalSize - 1)
			{
				x = 0;
				y += height;
				z = 0;
			} else
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
		if(bitLength % 2 == 0)
		{
			imageSize = Math.sqrt(segmentationStep * 2);
		} else
		{
			imageSize = Math.sqrt(segmentationStep);
		}
		return imageSize.intValue();
	}

	public void generateHSIColors()
	{

		float[][][] histHSI = new float[18][3][3];

		colors = new ArrayList<>();

		hsiColorCount = new Vector<>();

		for(int i = 0; i < image.getWidth(); i++)
		{
			for(int j = 0; j < image.getHeight(); j++)
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

				if(y == 0)
				{
					Hue = 0;
				} else
				{
					if(blueValue <= greenValue)
					{
						Hue = (float) ((Math.acos(x / y)));
					} else if(blueValue > greenValue)
					{
						Hue = 360 - ((float) ((Math.acos(x / y))));
					}
				}

				//Berechnung Saturation
				float minRGB = Math.min(Math.min(redValue, greenValue), blueValue);

				if((redValue + greenValue + blueValue) == 0)
				{
					Saturation = 1;
				} else
				{
					Saturation = 1 - ((3 * minRGB) / (redValue + greenValue + blueValue));
				}

				//Berechnung Intensity
				float I = ((redValue + greenValue + blueValue) / 3);

				int Hq = (int) ((Hue) / 20f);

				if(Saturation * 3 < (1 / 3f))
				{
					Sq = 0;
				} else if(Saturation * 3 < (2 / 3f))
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

				float Sat = Saturation * 100;

				float Int = I * 255;

				histHSI[Hq][Sq][Iq]++;

				if(histHSI[Hq][Sq][Iq] == 1.0)
				{
					HSIColors[0] = Hue;
					HSIColors[1] = Sat;
					HSIColors[2] = Int;
				}
				colors.add(HSIColors);
			}
		}

		for(int i = 0; i < 18; i++)
		{
			for(int j = 0; j < 3; j++)
			{
				for(int k = 0; k < 3; k++)
				{
					hsiColorCount.add(histHSI[i][j][k]);
				}
			}
		}

	}

	public void generateHistogramGray(String name)
	{
		histogram.generateHistogramGray(image, name);
	}

	//RGB Histogramm erzeugen
	public void generateHistogramRGB(String name)
	{
		float[][][] histogramRGB = new float[8][8][8];

		for(int x = 0; x < image.getWidth(); x++)
		{
			for(int y = 0; y < image.getHeight(); y++)
			{

				Color rgb = new Color(image.getRGB(x, y));

				//jeweils Rot, Gr�n und Blau Farbwerte aus rgb erzeugen
				int r = (rgb.getRed()) / 32;
				int g = (rgb.getGreen()) / 32;
				int b = (rgb.getBlue()) / 32;

				//Werte in Histogramm eintragen und gleiche Werte hochz�hlen
				histogramRGB[r][g][b]++;
			}
		}

		//Histogramm in Text-Datei speichern
		saveHistogramRGB(histogramRGB, name);

	}

	//HSI Histogramm generieren
	public void generateHistogramHSI(String name)
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

	//RGB Histogramm in Text-Datei speichern
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

	//HSI Histogramm in Text-Datei speichern
	private void saveHistogramHSI(float[][][] histogram, String name)
	{

		String filename = name.substring(0, name.length() - 4) + "-HSI.txt";
		File file = new File(filename);
		if(!file.exists())
		{
			try
			{
				//von ImageName.jpg den .jpg abschneiden und mit -HSI.txt ersetzen
				ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(filename));
				output.writeObject(histogram);
				output.close();
			} catch(IOException ex)
			{
				ex.printStackTrace();
			}
		}
	}

	public BufferedImage getImage()
	{
		return image;
	}

	public Image getThumbnail()
	{
		return this.thumbnail;
	}

	//Graustufen Histogramm aus Text-Datei laden und in ein float[] Array speichern
	public float[] getHistogramGray(String name)
	{
		String filename = (name.substring(0, name.length() - 4) + "-Gray.txt");
		float[] histGray;
		histGray = new float[256];
		try
		{
			ObjectInputStream input = new ObjectInputStream(new FileInputStream(filename));
			histGray = (float[]) (input.readObject());
			input.close();
		} catch(ClassNotFoundException | IOException ex)
		{
			ex.printStackTrace();
		}
		return histGray;
	}

	//RGB Histogramm aus Text-Datei laden und in ein float[][][] Array speichern
	public float[][][] getHistogramRGB(String name)
	{
		String filename = (name.substring(0, name.length() - 4) + "-RGB.txt");
		float[][][] histRGB;
		histRGB = new float[8][8][8];
		try
		{
			ObjectInputStream input = new ObjectInputStream(new FileInputStream(filename));
			histRGB = (float[][][]) (input.readObject());
			input.close();
		} catch(ClassNotFoundException | IOException ex)
		{
			ex.printStackTrace();
		}
		return histRGB;
	}

	//HSI Histogramm aus Text-Datei laden und in ein float[][][] Array speichern
	public float[][][] getHistogramHSI(String name)
	{
		String filename = (name.substring(0, name.length() - 4) + "-HSI.txt");
		float[][][] histHSI;
		histHSI = new float[18][3][3];
		try
		{
			ObjectInputStream input = new ObjectInputStream(new FileInputStream(filename));
			histHSI = (float[][][]) (input.readObject());
			input.close();
		} catch(ClassNotFoundException | IOException ex)
		{
			ex.printStackTrace();
		}
		return histHSI;
	}

	public Vector getColors(int count)
	{

		//System.out.println(hsiColorCount.size());

		float[] color;
		float[] addcontent = new float[3];
		Vector dominantcolor = new Vector();
		//Vector <Float> hsicolor = hsiColorCount;
		float a = 0;

		for(int i = 0; i < hsiColorCount.size(); i++)
		{
			if(hsiColorCount.size() > count)
			{
				if(hsiColorCount.get(i).equals(a))
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

		if(hsiColorCount.size() < count)
		{
			for(int i = 0; i < count - hsiColorCount.size(); i++)
			{
				addcontent[0] = 0;
				addcontent[1] = 0;
				addcontent[2] = 0;
			}
			colors.add(addcontent);
		}

		DominantColors[] lidominantColorList = new DominantColors[hsiColorCount.size()];

		for(int i = 0; i < hsiColorCount.size(); i++)
		{
			color = colors.get(i);
			lidominantColorList[i] = new DominantColors(color, hsiColorCount.get(i));
		}

		Arrays.sort(lidominantColorList);

		for(int i = 0; i < count; i++)
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
		if(image != null)
		{
			return image.getWidth();
		} else
		{
			return 0;
		}
	}

	public int getHeight()
	{
		if(image != null)
		{
			return image.getHeight();
		} else
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
		if(this.queryImage != null)
		{
			return similarity;
		} else
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
	public void setSimilarity(float similarity, MRImage q)
	{
		this.similarity = similarity;
		this.queryImage = q;
	}
}

