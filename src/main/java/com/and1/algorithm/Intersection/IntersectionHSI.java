package com.and1.algorithm.Intersection;

import com.and1.algorithm.SimilarityAlgorithm;
import com.and1.model.img.Image;

import java.util.Arrays;
import java.util.List;

public class IntersectionHSI extends SimilarityAlgorithm
{

	public List<Image> calculateSimilarity(Image basicImage, List<Image> repository, int segStep)
	{
		float intersection;
		float minsum = 0;
		float hist1sum = 0;

		String name = basicImage.toString();
		basicImage.generateHistogramHSI(name);
		float[][][] hist1 = basicImage.getHistogramHSI(name);
		float[][][] hist2;
		//Liste in die die das Img und die dazugehörige com.and1.algorithm.IntersectionRGB als Tupel gespeichert werden
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

		return getSortIntersectionList(list);
	}

}
