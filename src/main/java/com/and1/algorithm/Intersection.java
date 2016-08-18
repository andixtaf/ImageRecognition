package com.and1.algorithm;

import com.and1.SortIntersection;
import com.and1.img.MRImage;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Vector;

public class Intersection implements SimilarityAlgorithm
{

	public Vector<MRImage> apply(MRImage query, Vector<MRImage> repository, int segstep)
	{
		float intersection;
		float minsum = 0;
		float hist1sum = 0;
		if (query.getImage().getType() == BufferedImage.TYPE_BYTE_GRAY)
		{
			String name = query.getFilePath().getAbsolutePath();
			float[] hist1 = query.getHistogramGray(name);

			float[] hist2;

			//Liste in die die das Img und die dazugeh�rige com.and1.algorithm.Intersection als Tupel gespeichert werden
			SortIntersection[] list = new SortIntersection[repository.size()];
			for (int i = 0; i < repository.size(); i++)
			{
				MRImage img = repository.get(i);
				String imgname = img.getFilePath().getAbsolutePath();

				hist2 = img.getHistogramGray(imgname);

				for (int j = 0; j < hist1.length; j++)
				{
					minsum += Math.min(hist1[j], hist2[j]);
					hist1sum += hist1[j];
				}

				intersection = minsum / hist1sum;
				list[i] = new SortIntersection(img, intersection);

				minsum = 0;
				hist1sum = 0;

			}
			//Liste absteigend sortieren
			Arrays.sort(list);
			Vector<MRImage> sortedlist = new Vector<>();
			for (SortIntersection aList : list)
			{
				float intersect = aList.getIntersection();
				MRImage image = aList.getMRImage();

				sortedlist.add(image);
				image.setSimilarity(intersect, image);
			}

			return sortedlist;
		}
		else
		{
			String name = query.getFilePath().getAbsolutePath();
			float[][][] hist1 = query.getHistogramRGB(name);

			float[][][] hist2;

			//Liste in die die das Img und die dazugeh�rige com.and1.algorithm.Intersection als Tupel gespeichert werden
			SortIntersection[] list = new SortIntersection[repository.size()];
			for (int i = 0; i < repository.size(); i++)
			{
				MRImage img = repository.get(i);
				String imgname = img.getFilePath().getAbsolutePath();
				System.out.println(imgname);

				hist2 = img.getHistogramRGB(imgname);

				for (int j = 0; j < 8; j++)
				{
					for (int k = 0; k < 8; k++)
					{
						for (int l = 0; l < 8; l++)
						{
							minsum += Math.min(hist1[j][k][l], hist2[j][k][l]);
							hist1sum += hist1[j][k][l];
						}
					}
				}

				intersection = minsum / hist1sum;

				list[i] = new SortIntersection(img, intersection);
				minsum = 0;
				hist1sum = 0;

			}
			//Liste absteigend sortieren
			Arrays.sort(list);
			Vector<MRImage> sortedlist = new Vector<>();
			for (SortIntersection aList : list)
			{
				float intersect = aList.getIntersection();
				MRImage image = aList.getMRImage();
				//neues Repository erstellt welches sortierte Elemente enth�lt
				sortedlist.add(image);
				image.setSimilarity(intersect, image);
			}

			return sortedlist;
		}

	}
}