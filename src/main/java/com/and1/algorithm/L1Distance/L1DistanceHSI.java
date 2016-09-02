package com.and1.algorithm.L1Distance;

import com.and1.algorithm.SimilarityAlgorithm;
import com.and1.model.img.Image;

import java.util.Arrays;
import java.util.List;

public class L1DistanceHSI extends SimilarityAlgorithm
{

	public List<Image> calculateSimilarity(Image basicImage, List<Image> repository, int segStep)
	{

		float distance = 0;

		float[][][] hist1 = basicImage.getHistogramHSI(basicImage);
		float[][][] hist2;

		int totalhist1 = basicImage.getHeight() * basicImage.getWidth();

		SortL1Distance[] list = new SortL1Distance[repository.size()];
		for (int i = 0; i < repository.size(); i++)
		{
			Image img = repository.get(i);
			int totalhist2 = img.getHeight() * img.getWidth();
			hist2 = img.getHistogramHSI(img);
			for (int j = 0; j < 18; j++)
			{
				for (int k = 0; k < 3; k++)
				{
					for (int l = 0; l < 3; l++)
					{
						distance += Math.abs(hist1[j][k][l] / totalhist1 - hist2[j][k][l] / totalhist2);
					}
				}
			}

			list[i] = new SortL1Distance(img, distance);


			distance = 0;
		}
		Arrays.sort(list);

		return getSortL1DistanceList(list);
	}
}