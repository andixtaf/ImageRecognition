package com.and1.algorithm.Intersection;

import com.and1.algorithm.SimilarityAlgorithm;
import com.and1.model.img.Image;

import java.util.ArrayList;
import java.util.List;

public class IntersectionHSI extends SimilarityAlgorithm
{

	public List<Image> calculateSimilarity(Image basicImage, List<Image> repository, int segStep)
	{
		float intersection;
		float minsum = 0;
		float hist1sum = 0;

		float[][][] hist1 = basicImage.getHistogramHSI(basicImage);
		float[][][] hist2;

		List<SortIntersection> intersectionList = new ArrayList<>();
		for (int i = 0; i < repository.size(); i++)
		{
			Image img = repository.get(i);
			hist2 = img.getHistogramHSI(img);
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
			intersectionList.add(new SortIntersection(img, intersection));

			minsum = 0;
			hist1sum = 0;

		}

		return getSortIntersectionList(intersectionList);
	}

}
