package com.and1.algorithm;

import com.and1.model.img.Image;
import com.and1.sort.NRA_Algorithm_Sort;
import com.and1.sort.SortL1Distance;

import java.util.Arrays;
import java.util.Vector;

public class HSI_Euclidean_Distance
{

	private NRA_Algorithm_Sort[] nra_values;

	public Vector<Image> applySimilarity(Image query, Vector<Image> repository, int number, int similarity)
	{

		Float distance = 0F;
		Float scaledDistance;
		Vector colorHSI;

		float[] h1;
		float[] h2;
		query.generateHSIColors();
		colorHSI = query.getColors(number);
		Vector colorHSI2;

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
		Vector<Image> sortedlist = new Vector<>();

		for (SortL1Distance aList : list)
		{
			Float dist = aList.getDistance();
			Image image = aList.getMRImage();
			sortedlist.add(image);
			image.setSimilarity(dist, image);
		}

		return sortedlist;
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

    