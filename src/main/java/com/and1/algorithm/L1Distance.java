package com.and1.algorithm;

import com.and1.sort.SortL1Distance;
import com.and1.model.img.Image;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Vector;

//Klasse zur Berechnung der Distance
public class L1Distance implements SimilarityAlgorithm
{

	/**
	 * Generates random similarity values for all images relative to the query image
	 *
	 * @return The unprocessed repository
	 */
	public Vector<Image> apply(Image query, Vector<Image> repository, int segStep)
	{

		float distance = 0;
		int totalhist1 = query.getHeight() * query.getWidth();
		if (query.getImage().getType() == BufferedImage.TYPE_BYTE_GRAY)
		{
			String name = query.getFilePath().getAbsolutePath();
			float[] hist1 = query.getHistogramGray(name);
			//System.out.println("Histogramm1 :" + query.filePath);
			float[] hist2;
			//Liste in die die das Img und die dazugeh�rige com.and1.algorithm.Intersection als Tupel gespeichert werden
			SortL1Distance[] list = new SortL1Distance[repository.size()];
			for (int i = 0; i < repository.size(); i++)
			{
				Image img = repository.get(i);
				String imgname = img.getFilePath().getAbsolutePath();
				//System.out.println(i);
				hist2 = img.getHistogramGray(imgname);
				//System.out.println("Histogramm1 :" + query.filePath);
				//System.out.println("Histogramm2 :" + com.and1.model.img.filePath);
				for (int j = 0; j < hist1.length; j++)
				{
					distance += Math.abs(hist1[j] - hist2[j]);
				}

				list[i] = new SortL1Distance(img, distance);
				//System.out.println("Distance");
				//System.out.println(distance);
				distance = 0;
			}
			//Liste aufsteigend sortieren
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
		else
		{
			String name = query.getFilePath().getAbsolutePath();
			float[][][] hist1 = query.getHistogramRGB(name);
			//System.out.println("Histogramm1 :" + query.filePath);
			float[][][] hist2;
			//Liste in die die das Img und die dazugeh�rige com.and1.algorithm.Intersection als Tupel gespeichert werden
			SortL1Distance[] list = new SortL1Distance[repository.size()];
			for (int i = 0; i < repository.size(); i++)
			{
				Image img = repository.get(i);
				int totalhist2 = img.getHeight() * img.getWidth();
				String imgname = img.getFilePath().getAbsolutePath();
				//System.out.println(i);
				hist2 = img.getHistogramRGB(imgname);
				//System.out.println("Histogramm1 :" + query.filePath);
				//System.out.println("Histogramm2 :" + com.and1.model.img.filePath);
				for (int j = 0; j < 8; j++)
				{
					for (int k = 0; k < 8; k++)
					{
						for (int l = 0; l < 8; l++)
						{
							distance += Math.abs(hist1[j][k][l] / totalhist1 - hist2[j][k][l] / totalhist2);
						}
					}
				}

				list[i] = new SortL1Distance(img, distance);
				//System.out.println("Distance");
				//System.out.println(distance);
				distance = 0;
			}
			//Liste aufsteigend sortieren
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