package com.and1.algorithm;

import com.and1.algorithm.sort.NRA_Algorithm_Sort;
import com.and1.algorithm.sort.SortIntersection;
import com.and1.model.img.Image;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Chi_Square_Semi_Pseudo_Distance extends SimilarityAlgorithm
{

	private NRA_Algorithm_Sort[] nra_values;

	@Override
	public List<Image> apply(Image query, List<Image> repository, int segStep)
	{

		Float distance;
		float scaledDistance;
		float expectedValueHistory1;
		float expectedValueHistory2;
		float sum1 = 0;
		float sum2 = 0;
		float totalSum = 0;
		float sumHistory1 = 0;
		float sumHistory2 = 0;
		int x = 0;

		String name = query.toString();
		query.generateHistogramHSI(name);

		float[][][] hist1 = query.getHistogramHSI(name);
		float[][][] hist2;

		SortIntersection[] list = new SortIntersection[repository.size()];
		nra_values = new NRA_Algorithm_Sort[repository.size()];

		for (int i = 0; i < repository.size(); i++)
		{
			Image img = repository.get(i);
			//int totalhist2 = com.and1.model.img.getHeight() * com.and1.model.img.getWidth();
			String imgName = img.toString();
			img.generateHistogramHSI(imgName);
			hist2 = img.getHistogramHSI(imgName);

			for (int j = 0; j < 18; j++)
			{
				for (int k = 0; k < 3; k++)
				{
					for (int l = 0; l < 3; l++)
					{

						//Bestimmung der Gesamtsumme und der Einzelsummen beiden Histogramme für den erwarteten Wert
						if (x == 0)
						{
							for (int m = 0; m < 18; m++)
							{
								for (int n = 0; n < 3; n++)
								{
									for (int o = 0; o < 3; o++)
									{
										totalSum += (hist1[m][n][o] + hist2[m][n][o]);
										sum1 += hist1[m][n][o];
										sum2 += hist2[m][n][o];
									}
								}
							}
							x = 1;
						}

						//erwartete Werte der Histogramme bestimmen
						expectedValueHistory1 = ((hist1[j][k][l] + hist2[j][k][l]) * sum1) / totalSum;
						expectedValueHistory2 = ((hist1[j][k][l] + hist2[j][k][l]) * sum2) / totalSum;

						if (expectedValueHistory1 == 0)
						{
							sumHistory1 += 0;
						}
						else
						{
							//Bestimmung der beiden Teilsummen zur Berechnung der Distanz
							sumHistory1 +=
									((hist1[j][k][l] - expectedValueHistory1) *
											(hist1[j][k][l] - expectedValueHistory1)) /
											expectedValueHistory1;
						}

						if (expectedValueHistory2 == 0)
						{
							sumHistory2 += 0;
						}
						else
						{
							sumHistory2 +=
									((hist2[j][k][l] - expectedValueHistory2) *
											(hist2[j][k][l] - expectedValueHistory2)) /
											expectedValueHistory2;
						}
					}
				}

			}

			distance = sumHistory1 + sumHistory2;

			scaledDistance = scale(distance);

			list[i] = new SortIntersection(img, scaledDistance);

			nra_values[i] = new NRA_Algorithm_Sort(i, scaledDistance);

			sumHistory1 = 0;
			sumHistory2 = 0;
			sum1 = 0;
			sum2 = 0;
			totalSum = 0;
			x = 0;
		}

		Arrays.sort(list);
		Arrays.sort(nra_values);

		List<Image> sortedList = new ArrayList<>();

		Arrays.sort(list);

		return getSortIntersectionList(list);
	}

	//Funktion zur Skalierung der übergroßen Werte der Distanzfunktion um Ähnlichkeitswerte zu erhalten
	private float scale(float dist)
	{
		float s = 2000f;

		return 1 / (1 + (dist / s));
	}

	public NRA_Algorithm_Sort[] getNRA_Values()
	{
		return nra_values;
	}

}
