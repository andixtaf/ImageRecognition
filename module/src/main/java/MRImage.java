import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.*;
import java.util.Arrays;
import java.util.Vector;

public class MRImage {

	private boolean hasHistogram = false;
	private boolean isGrayscale = false;
	private float [] histogramGray;
	private float [] [] [] histogramRGB;
	private float [] [] [] histogramHSI;
	private  Vector colors;
	private Vector <Float> hsicolorcount;
	BufferedImage image = null;
	Image thumbnail = null;
	String filePath = null;
	private int total;
	private float similarity;
	private MRImage queryImage;
	private WritableRaster raster = null;


	public MRImage(String file, BufferedImage img) {
		int w, h;
		filePath = file;
		image = img;
		similarity = 1.0f;
		queryImage = this;
		thumbnail = image.getScaledInstance(-1, 60, Image.SCALE_FAST);
		raster = image.getRaster();
	}

	public MRImage(String file) {
		filePath = file;
	}

	public MRImage(String[] csvValues) {
	}

	//Image in 4 Teile zerlegen
	public Vector<BufferedImage> generateRaster(int segstep) {

		Vector<BufferedImage> segment = new Vector<BufferedImage>();
		//System.out.println(image.getHeight());
		//       System.out.println(image.getWidth());

		double imgsize = 0;
		String bit = Integer.toBinaryString(segstep);
		int len = bit.length();

		if (len % 2 == 0) {
			imgsize = Math.sqrt(segstep * 2);
		}
		else {
			imgsize = Math.sqrt(segstep);
		}
		int isize = (int)(imgsize);
		//System.out.println("Faktor");
		//System.out.println(isize);
		int x = 0;
		int y = 0;
		int z = 0;
		BufferedImage img = null;


		for (int i = 0; i < segstep; i++) {

           /*System.out.println(i);
            System.out.println("x");
            System.out.println(x);
            System.out.println("y");
            System.out.println(y);*/


			if (len % 2 == 0) {
				img = image.getSubimage(x, y, image.getWidth()/isize, image.getHeight()/(isize/2));
			}
			else {
				img = image.getSubimage(x, y, image.getWidth()/isize, image.getHeight()/isize);
			}
			segment.add(img);

			//System.out.println("Seg" + i);
			//System.out.println(segment.get(i).getWidth());
			//System.out.println(segment.get(i).getHeight());

			if (z == isize - 1) {
				// System.out.println("Breite erreicht");
				x = 0;
				if (len % 2 == 0) {
					y += image.getHeight()/ (isize/2);
				}
				else {
					y += image.getHeight()/isize;
				}
				z = 0;
			}
			else  {
				x += image.getWidth()/isize;
				z++;

			}
		}
		//System.out.println(segment.size());

		return segment;
	}

	public void generateHSIColors() {

		float [] [] [] histHSI = new float [18] [3] [3];

		colors = new Vector();

		int count = 0;

		hsicolorcount = new Vector<Float>();

		if (image != null) {

			for (int i = 0; i < image.getWidth(); i++)
			{
				for (int j = 0; j < image.getHeight(); j++)
				{
					int Sq = 0;
					int Iq = 0;
					Color RGB = new Color (image.getRGB(i, j));

					float HSIcolors [] = new float [3];

					float R = RGB.getRed() / 255f;
					float G = RGB.getBlue() / 255f;
					float B = RGB.getGreen() / 255f;

					//Berechnung Hue
					float x = ((R - G) + (R - B)) / 2;
					float y = (float)(Math.sqrt(((R - G) * (R - G)) + ((R - B) * (G - B))));

                             /* System.out.println("x");
                              System.out.println(x);
                              System.out.println("y");
                              System.out.println(y);*/

					float H = 0;
					float S = 0;

					if (y == 0) {
						H = 0;
					}
					else {
						if (B <= G) {
							//System.out.println("B < G");
							//System.out.println((float)((Math.acos(x / y))));
							H = (float)((Math.acos(x / y)));
						}
						else if (B > G) {
							//System.out.println("B > G");
							//System.out.println(360 - (float)((Math.acos(x / y))));
							H = 360  - ((float)((Math.acos(x / y))));
						}
					}
					//System.out.println("H");
					//System.out.println(H);

					//Berechnung Saturation
					float minRGB = Math.min(Math.min(R,G),B);


					if ((R+G+B) == 0 ) {
						S = 1;
					}
					else {
						S = 1 - ((3 * minRGB) / (R + G + B));
					}
					//System.out.println("S");
					//System.out.println(S);

					//Berechnung Intensity
					float I = ((R + G + B) / 3);
					//System.out.println("I");
					//System.out.println(I);

					int Hq = (int)((H) / 20f);

					if (S * 3 < (1/3f)) {
						Sq = 0;
					}
					else if (S * 3 < (2/3f)) {
						Sq = 1;
					}
					else {
						Sq = 2;
					}

					if (I * 3 < 85) {
						Iq = 0;
					}
					else if (I * 3 < 170) {
						Iq = 1;
					}
					else {
						Iq = 2;
					}

					float Sat = S * 100;

					float Int = I * 255;

					histHSI [Hq][Sq][Iq]++;

					if (histHSI [Hq][Sq][Iq] == 1.0) {
                                  /*count++;
                                  System.out.println(count);
                                  System.out.println("H");
                              System.out.println(H);

                              System.out.println("S");
                              System.out.println(Sat);

                              System.out.println("Int");
                              System.out.println(Int);*/
						HSIcolors[0] = H;
						HSIcolors[1] = Sat;
						HSIcolors[2] = Int;
					}
					colors.add(HSIcolors);
				}
			}
			//System.out.println(count);

			for (int i = 0; i < 18; i++) {
				for (int j = 0; j < 3; j++) {
					for (int k = 0; k < 3; k++) {
						hsicolorcount.add(histHSI [i][j][k]);
					}
				}
			}

			//for (int i = 0; i < hsicolorcount.size(); i++){
			//System.out.println(hsicolorcount.get(i));
			// }
		}
	}

	//Graustufen Histogramm erzeugen
	public void generateHistogramGray(String name) {
		if (image != null)
		{
			//TODO: normalisiertes Histogramm erzeugen
			histogramGray = new float[256];


			for (int x = 0; x < image.getWidth(); x++)
			{
				for (int y = 0; y < image.getHeight(); y++)
				{
					int [] pixel = null;
					pixel = raster.getPixel(x, y, pixel);
					//Anzahl der vorkommenden Pixel an der Stelle i
					histogramGray[pixel[0]]++;
					//Anzahl der Pixel des gesamten Bildes
					total++;
				}
			}

			hasHistogram = true;
			normalize(histogramGray);
			//Histogramm in Text-Datei speichern
			saveHistogramGray(histogramGray, name);


		}
	}

	//RGB Histogramm erzeugen
	public void generateHistogramRGB(String name) {
		if (image != null)
		{

			histogramRGB = new float [8] [8] [8];

			for (int x = 0; x < image.getWidth(); x++)
			{
				for (int y = 0; y < image.getHeight(); y++)
				{

					Color rgb = new Color (image.getRGB(x, y));

					//jeweils Rot, Grün und Blau Farbwerte aus rgb erzeugen
					int r = (rgb.getRed())/32;
					int g = (rgb.getGreen()) / 32;
					int b = (rgb.getBlue()) / 32;

					//Werte in Histogramm eintragen und gleiche Werte hochzählen
					histogramRGB [r][g][b]++;
				}
			}

			hasHistogram = true;
			//Histogramm in Text-Datei speichern
			saveHistogramRGB(histogramRGB, name);
		}

	}

	//HSI Histogramm generieren
	public void generateHistogramHSI(String name) {

		histogramHSI = new float [18] [3] [3];

		if (image != null) {

			for (int i = 0; i < image.getWidth(); i++)
			{
				for (int j = 0; j < image.getHeight(); j++)
				{
					int Sq = 0;
					int Iq = 0;
					Color RGB = new Color (image.getRGB(i, j));

					float R = RGB.getRed() / 255f;
					float G = RGB.getBlue() / 255f;
					float B = RGB.getGreen() / 255f;

					//Berechnung Hue
					float x = ((R - G) + (R - B)) / 2;
					float y = (float)(Math.sqrt(((R - G) * (R - G)) + ((R - B) * (G - B))));

					float H = 0;

					if (B <= G) {
						H = (float)((Math.acos(x / y)));
					}
					else if (B > G) {
						H = 360f  - ((float)((Math.acos(x / y))));
					}
					//System.out.println("H");
					//System.out.println(H);

					//Berechnung Saturation
					float minRGB = Math.min(Math.min(R,G),B);

					float S = 1 - ((3 * minRGB) / (R + G + B));
					//System.out.println("S");
					//System.out.println(S);

					//Berechnung Intensity
					float I = ((R + G + B) / 3);
					//System.out.println("I");
					//System.out.println(I);

					int Hq = (int)((H) / 20f);

					if (S * 3 < (1/3f)) {
						Sq = 0;
					}
					else if (S * 3 < (2/3f)) {
						Sq = 1;
					}
					else {
						Sq = 2;
					}

					if (I * 3 < 85) {
						Iq = 0;
					}
					else if (I * 3 < 170) {
						Iq = 1;
					}
					else {
						Iq = 2;
					}

					//System.out.println("Hq");
					//System.out.println(Hq);

					//System.out.println("Sq");
					//System.out.println(Sq);

					//System.out.println("Iq");
					//System.out.println(Iq);


					histogramHSI [Hq][Sq][Iq]++;




					//Histogramm in Text-Datei speichern
				}
			}
			saveHistogramHSI(histogramHSI, name);

        /*float [] test = getDominantHSIColors(4);

        for (int i = 0; i < test.length; i++) {
            System.out.println(test[i]);
        }*/
        /*for (int i = 0; i < 18; i++) {
                                    for (int j = 0; j < 3; j++) {
                                        for (int k = 0; k < 3; k++) {
                                            System.out.println(histogramHSI[i][j][k]);
                                        }
                                    }
                                }*/
		}
	}

	//Graustufen Histogramm normalisieren
	private void normalize(float [] histogram) {

		float sum = 0;
		for (int i = 0; i < 256; i++)
		{

			histogram[i] = histogram[i]/total;
			//sum += histogram[i];
		}

		//System.out.println(sum);
	}

	//Graustufen Histogramm in Text-Datei speichern
	public void saveHistogramGray(float [] histogram, String name) {

		File file = new File(name.substring(0, name.length()-4) + "-Gray.txt");
		if(!file.exists()){
			try {
				//von ImageName.jpg den .jpg abschneiden und mit -Gray.txt ersetzen
				ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(name.substring(0, name.length()-4) + "-Gray.txt"));
				output.writeObject(histogram);
				output.close();
			} catch (FileNotFoundException ex) {
				ex.printStackTrace();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	//RGB Histogramm in Text-Datei speichern
	public void saveHistogramRGB(float [][][] histogram, String name) {
		//von ImageName.jpg den .jpg abschneiden und mit -RGB.txt ersetzen
		String filename = name.substring(0,name.length()-4) + "-RGB.txt";
		File file = new File(filename);
		if(!file.exists()){
			try {
				ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(filename));
				output.writeObject(histogram);
				output.close();
			} catch (FileNotFoundException ex) {
				ex.printStackTrace();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	//HSI Histogramm in Text-Datei speichern
	public void saveHistogramHSI(float [][][] histogram, String name) {

		String filename = name.substring(0,name.length()-4) + "-HSI.txt";
		File file = new File(filename);
		if (!file.exists()){
			try {
				//von ImageName.jpg den .jpg abschneiden und mit -HSI.txt ersetzen
				ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(filename));
				output.writeObject(histogram);
				output.close();
			} catch (FileNotFoundException ex) {
				ex.printStackTrace();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public BufferedImage getImage() {
		return image;
	}

	public Image getThumbnail() {
		return this.thumbnail;
	}

	//Graustufen Histogramm aus Text-Datei laden und in ein float[] Array speichern
	public float [] getHistogramGray(String name) {
		String filename = (name.substring(0, name.length()-4) + "-Gray.txt");
		float [] histGray = null;
		histGray = new float [256];
		try {
			ObjectInputStream input = new ObjectInputStream(new FileInputStream(filename));
			histGray = (float[]) (input.readObject());
			input.close();
		}  catch (EOFException ex) {
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return histGray;
	}

	//RGB Histogramm aus Text-Datei laden und in ein float[][][] Array speichern
	public float[] [] [] getHistogramRGB(String name) {
		String filename = (name.substring(0, name.length()-4) + "-RGB.txt");
		float [][][] histRGB = null;
		histRGB = new float [8] [8] [8];
		try {
			ObjectInputStream input = new ObjectInputStream(new FileInputStream(filename));
			histRGB = (float[][][]) (input.readObject());
			input.close();
		}  catch (EOFException ex) {
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return histRGB;
	}

	//HSI Histogramm aus Text-Datei laden und in ein float[][][] Array speichern
	public float [] [] [] getHistogramHSI(String name) {
		String filename = (name.substring(0, name.length()-4) + "-HSI.txt");
		float [][][] histHSI = null;
		histHSI = new float [18] [3] [3];
		try {
			ObjectInputStream input = new ObjectInputStream(new FileInputStream(filename));
			histHSI = (float[][][]) (input.readObject());
			input.close();
		}  catch (EOFException ex) {
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return histHSI;
	}

	public Vector getColors(int count) {

		//System.out.println(hsicolorcount.size());

		float [] color = new float [3];
		float [] addcontent = new float [3];
		Vector dominantcolor = new Vector();
		//Vector <Float> hsicolor = hsicolorcount;
		float a = 0;

		for (int i = 0; i < hsicolorcount.size(); i++) {
			if (hsicolorcount.size() > count) {
				if (hsicolorcount.get(i).equals(a)) {
					hsicolorcount.remove(i);
					i = 0;
				}
			}
		}
		//System.out.println("Size:" + hsicolor.size());

        /*for (int i = 0; i < hsicolor.size(); i++) {
            System.out.println(hsicolor.get(i));
        }*/

		if (hsicolorcount.size() < count) {
			for (int i = 0; i < count - hsicolorcount.size(); i++) {
				addcontent [0] = 0;
				addcontent [1] = 0;
				addcontent [2] = 0;
			}
			colors.add(addcontent);
		}

		DominantColors [] list  = new DominantColors[hsicolorcount.size()];

		for (int i = 0; i < hsicolorcount.size(); i++) {
			color = (float []) colors.get(i);
			list[i] = new DominantColors(color,hsicolorcount.get(i));
		}


		Arrays.sort(list);

		for (int i = 0; i < count; i++) {
			dominantcolor.add(list[(count - 1) - i].getHSIColor());
		}
		colors.removeAllElements();
		hsicolorcount.removeAllElements();
		return dominantcolor;
	}

	public String toString() {
		return new File(filePath).getName();
	}

	public int getWidth() {
		if (image != null) {
			return image.getWidth();
		} else {
			return 0;
		}
	}

	public int getHeight() {
		if (image != null) {
			return image.getHeight();
		} else {
			return 0;
		}
	}

	/**
	 * Returns the similarity value measured from the query image
	 * @return The similarity value in a range from [0,1] or -1 if the query image is not set or the value has not been recalculated
	 * @see getQueryImage
	 */
	public float getSimilarity() {
		if (this.queryImage != null) {
			return similarity;
		} else {
			return -1.0f;
		}
	}


	/**
	 * Sets the similarity value to a given query image
	 * @param similarity the similarity value to the query image
	 * @param q the query image
	 */
	public void setSimilarity(float similarity, MRImage q) {
		this.similarity = similarity;
		this.queryImage = q;
	}

	public MRImage getQueryImage() {
		return queryImage;
	}

	public boolean isGray() {
		return this.isGrayscale;
	}
}

