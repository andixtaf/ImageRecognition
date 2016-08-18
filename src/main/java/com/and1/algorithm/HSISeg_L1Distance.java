package com.and1.algorithm;

import com.and1.SortL1Distance;
import com.and1.img.Image;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Vector;

//Klasse zur Berechnung der com.and1.algorithm.Intersection
public class HSISeg_L1Distance implements SimilarityAlgorithm
{

	public Vector<Image> apply(Image query, Vector<Image> repository, int segstep)
	{
		float distanceseg1 = 0;
		float distance = 0;
		Vector<Float> distanceseg = new Vector<>();
		String name = query.toString();

		Vector<BufferedImage> segment;
		Vector hist1 = new Vector();
		segment = query.generateRasterInGivenSteps(segstep);
		for (int i = 0; i < segment.size(); i++)
		{
			Image seg = new Image(query.getFilePath(), segment.get(i));
			seg.generateHistogramHSI(segstep + "Seg" + i + name);
			float[][][] hist1seg = seg.getHistogramHSI(segstep + "Seg" + i + name);
			hist1.add(hist1seg);
		}
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

			segmenthist2 = img.generateRasterInGivenSteps(segstep);

			int totalhist2 = img.getHeight() * img.getWidth();

			for (int j = 0; j < segmenthist2.size(); j++)
			{
				Image seghist2 = new Image(img.getFilePath(), segmenthist2.get(j));
				seghist2.generateHistogramHSI(segstep + "Seg" + j + imgname);
				hist2seg = seghist2.getHistogramHSI(segstep + "Seg" + j + imgname);
				hist2.add(hist2seg);
			}

			for (int k = 0; k < segmenthist2.size(); k++)
			{
				h1seg = (float[][][]) (hist1.get(k));
				h2seg = (float[][][]) (hist2.get(k));
				for (int l = 0; l < 18; l++)
				{
					for (int m = 0; m < 3; m++)
					{
						for (int n = 0; n < 3; n++)
						{
							distanceseg1 += Math.abs((h1seg[l][m][n] / totalhist2) - (h2seg[l][m][n] / totalhist2));
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

			list[i] = new SortL1Distance(img, distance / segstep);
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
