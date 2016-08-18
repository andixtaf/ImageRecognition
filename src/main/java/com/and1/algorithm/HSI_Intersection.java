package com.and1.algorithm;

import com.and1.SortIntersection;
import com.and1.img.Image;

import java.util.Arrays;
import java.util.Vector;

public class HSI_Intersection implements SimilarityAlgorithm
{

	public Vector<Image> apply(Image query, Vector<Image> repository, int segstep)
	{
		float intersection;
		float minsum = 0;
		float hist1sum = 0;

		String name = query.toString();
		query.generateHistogramHSI(name);
		float[][][] hist1 = query.getHistogramHSI(name);
		float[][][] hist2;
		//Liste in die die das Img und die dazugeh�rige com.and1.algorithm.Intersection als Tupel gespeichert werden
		SortIntersection[] list = new SortIntersection[repository.size()];
		for (int i = 0; i < repository.size(); i++)
		{
			Image img = repository.get(i);
			String imgname = img.toString();
			img.generateHistogramHSI(imgname);
			hist2 = img.getHistogramHSI(imgname);
			for (int j = 0; j < 18; j++)
			{
				for (int k = 0; k < 3; k++)
				{
					for (int l = 0; l < 3; l++)
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
		Vector<Image> sortedlist = new Vector<>();
		for (SortIntersection aList : list)
		{
			float intersect = aList.getIntersection();
			Image image = aList.getMRImage();
			//neues Repository erstellt welches sortierte Elemente enth�lt
			sortedlist.add(image);
			image.setSimilarity(intersect, image);
		}

		return sortedlist;
	}

}