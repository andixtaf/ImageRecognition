package com.and1.algorithm;

import com.and1.sort.SortL1Distance;
import com.and1.model.img.Image;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Vector;

//Klasse zur Berechnung der com.and1.algorithm.Intersection
public class Seg_L1Distance implements SimilarityAlgorithm
{

	public Vector<Image> apply(Image query, Vector<Image> repository, int segStep)
	{
		float distanceseg1 = 0;
		float distance = 0;
		//Falls image ein Graustufenbild ist
		Vector<Float> distanceseg = new Vector<>();
		//Falls image ein Graustufenbild ist
		if (query.getImage().getType() == BufferedImage.TYPE_BYTE_GRAY)
		{
			String name = query.toString();
			Vector<BufferedImage> segment;
			Vector hist1 = new Vector();
			//image in 4 Teile zerlegen
			segment = query.generateRasterInGivenSteps(segStep);
			//neues com.and1.model.img.and1.Image f�r jedes Teilbild erzeugen
			for (int i = 0; i < segment.size(); i++)
			{
				Image seg = new Image(query.getFilePath(), segment.get(i));
				seg.generateHistogramGray(segStep + "Seg" + i + "Gray" + name);
				float[] hist1seg = seg.getHistogramGray(segStep + "Seg" + i + "Gray" + name);
				hist1.add(hist1seg);
			}
			//Histogramme laden

			//System.out.println("Histogramm1 :" + query.filePath);
			float[] hist2seg;
			float[] h1seg;
			float[] h2seg;
			//Liste in die die das Img und die dazugeh�rige com.and1.algorithm.Intersection als Tupel gespeichert werden
			SortL1Distance[] list = new SortL1Distance[repository.size()];
			for (int i = 0; i < repository.size(); i++)
			{
				Image img = repository.get(i);
				String imageName = img.toString();
				Vector<BufferedImage> segmentHistory2;
				Vector hist2 = new Vector();

				segmentHistory2 = img.generateRasterInGivenSteps(segStep);

				for (int j = 0; j < segmentHistory2.size(); j++)
				{
					Image seghist2 = new Image(img.getFilePath(), segmentHistory2.get(j));
					seghist2.generateHistogramGray(segStep + "Seg" + j + "RGB" + imageName);
					hist2seg = seghist2.getHistogramGray(segStep + "Seg" + j + "RGB" + imageName);
					hist2.add(hist2seg);
				}

				for (int k = 0; k < segmentHistory2.size(); k++)
				{
					h1seg = (float[]) (hist1.get(k));
					h2seg = (float[]) (hist2.get(k));
					for (int l = 0; l < 256; l++)
					{
						distanceseg1 += Math.abs(h1seg[l] - h2seg[l]);
					}

					distanceseg.add(distanceseg1);
					distanceseg1 = 0;
				}

				for (Float aDistanceseg : distanceseg)
				{
					distance += aDistanceseg;
				}

				list[i] = new SortL1Distance(img, distance / segStep);

				//distanceseg1 = 0;
				distance = 0;
				distanceseg.removeAllElements();
				hist2.removeAllElements();
				segmentHistory2.removeAllElements();
			}
			//Liste absteigend sortieren
			Arrays.sort(list);
			Vector<Image> sortedlist = new Vector<>();
			for (SortL1Distance aList : list)
			{
				float dist = aList.getDistance();
				Image image = aList.getMRImage();
				//neues Repository erstellt welches sortierte Elemente enth�lt
				sortedlist.add(image);
				image.setSimilarity(dist, image);
			}

			return sortedlist;
		}
		//Falls image RGB Bild ist
		else
		{
			String name = query.toString();
			int totalhist1 = query.getHeight() * query.getWidth();
			Vector<BufferedImage> segment;
			Vector hist1 = new Vector();
			segment = query.generateRasterInGivenSteps(segStep);
			for (int i = 0; i < segment.size(); i++)
			{
				Image seg = new Image(query.getFilePath(), segment.get(i));
				seg.generateHistogramRGB(segStep + "Seg" + i + name);
				float[][][] hist1seg = seg.getHistogramRGB(segStep + "Seg" + i + name);
				hist1.add(hist1seg);
			}
			//System.out.println("Histogramm1 :" + query.filePath);
			float[][][] hist2seg;
			float[][][] h1seg;
			float[][][] h2seg;

			//Liste in die die das Img und die dazugeh�rige com.and1.algorithm.Intersection als Tupel gespeichert werden
			SortL1Distance[] list = new SortL1Distance[repository.size()];
			for (int i = 0; i < repository.size(); i++)
			{
				Image img = repository.get(i);
				String imgname = img.toString();
				Vector<BufferedImage> segmenthist2;
				Vector hist2 = new Vector();

				segmenthist2 = img.generateRasterInGivenSteps(segStep);

				int totalhist2 = img.getHeight() * img.getWidth();

				for (int j = 0; j < segmenthist2.size(); j++)
				{
					Image seghist2 = new Image(img.getFilePath(), segmenthist2.get(j));
					seghist2.generateHistogramRGB(segStep + "Seg" + j + imgname);
					hist2seg = seghist2.getHistogramRGB(segStep + "Seg" + j + imgname);
					hist2.add(hist2seg);
				}

				for (int k = 0; k < segmenthist2.size(); k++)
				{
					h1seg = (float[][][]) (hist1.get(k));
					h2seg = (float[][][]) (hist2.get(k));
					for (int l = 0; l < 8; l++)
					{
						for (int m = 0; m < 8; m++)
						{
							for (int n = 0; n < 8; n++)
							{
								distanceseg1 += Math.abs((h1seg[l][m][n] / totalhist1) - (h2seg[l][m][n] / totalhist2));
							}
						}
					}
					distanceseg.add(distanceseg1);
					distanceseg1 = 0;
				}

				for (Float aDistanceseg : distanceseg)
				{
					distance += aDistanceseg;
				}

				list[i] = new SortL1Distance(img, distance / segStep);
				distance = 0;
				distanceseg.removeAllElements();
				hist2.removeAllElements();
				segmenthist2.removeAllElements();

			}
			//Liste absteigend sortieren
			Arrays.sort(list);
			Vector<Image> sortedlist = new Vector<>();
			for (SortL1Distance aList : list)
			{
				float dist = aList.getDistance();
				Image image = aList.getMRImage();
				//neues Repository erstellt welches sortierte Elemente enth�lt
				sortedlist.add(image);
				image.setSimilarity(dist, image);
			}

			return sortedlist;
		}
	}
}    
