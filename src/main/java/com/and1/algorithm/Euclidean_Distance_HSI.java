package com.and1.algorithm;

import com.and1.algorithm.L1Distance.SortL1Distance;
import com.and1.algorithm.sort.NRA_Algorithm_Sort;
import com.and1.model.img.Image;

import java.util.Arrays;
import java.util.List;

public class Euclidean_Distance_HSI extends SimilarityAlgorithm
{

	private NRA_Algorithm_Sort[] nra_values;

	@Override
	public List<Image> calculateSimilarity(Image basicImage, List<Image> repository, int segStep)
	{
		return null;
	}

	public List<Image> applySimilarity(Image query, List<Image> repository, int number, int similarity)
	{

		Float distance = 0F;
		Float scaledDistance;
		List colorHSI;

		float[] h1;
		float[] h2;
		query.generateHSIColors();
		colorHSI = query.getColors(number);
		List colorHSI2;

		SortL1Distance[] list = new SortL1Distance[repository.size()];
		nra_values = new NRA_Algorithm_Sort[repository.size()];

		for (int i = 0; i < repository.size(); i++)
		{
			//System.out.println(i);
			Image img = repository.get(i);
			img.generateHSIColors();
			colorHSI2 = img.getColors(number);

			for (int j = 0; j < number; j++)
			{
				h1 = (float[]) colorHSI.get(j);
				for (int k = 0; k < number; k++)
				{
					h2 = (float[]) colorHSI2.get(k);
					distance += (float) Math.sqrt((h1[0] - h2[0]) * (h1[0] - h2[0]) +
														  (h1[1] - h2[1]) * (h1[1] - h2[1]) +
														  (h1[2] - h2[2]) * (h1[2] - h2[2]));
				}
			}
			scaledDistance = scale(distance);

			list[i] = new SortL1Distance(img, scaledDistance);
			nra_values[i] = new NRA_Algorithm_Sort(i, scaledDistance);

			distance = 0F;
		}
		Arrays.sort(list);
		Arrays.sort(nra_values);

		return getSortL1DistanceList(list);
	}

	private Float scale(Float dist)
	{
		Float s = 2_000F;

		return 1 / (1 + (dist / s));
	}

	public NRA_Algorithm_Sort[] getNRA_Values()
	{
		return nra_values;
	}

}

    