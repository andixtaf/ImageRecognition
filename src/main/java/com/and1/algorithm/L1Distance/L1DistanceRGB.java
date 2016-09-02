package com.and1.algorithm.L1Distance;

import com.and1.algorithm.SimilarityAlgorithm;
import com.and1.model.img.Image;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;

//Klasse zur Berechnung der Distance
public class L1DistanceRGB extends SimilarityAlgorithm
{
	private static final Logger logger = LogManager.getLogger(L1DistanceRGB.class);

	/**
	 * Generates random similarity values for all images relative to the query image
	 *
	 * @return The unprocessed repository
	 */
	public List<Image> calculateSimilarity(Image basicImage, List<Image> repository, int segStep)
	{
		SortL1Distance[] list;

		float distance = 0;
		int totalhist1 = basicImage.getHeight() * basicImage.getWidth();

		float[][][] hist1 = basicImage.getHistogramRGB(basicImage);

		float[][][] hist2;

		list = new SortL1Distance[repository.size()];
		for (int i = 0; i < repository.size(); i++)
		{
			Image img = repository.get(i);
			int totalhist2 = img.getHeight() * img.getWidth();

			hist2 = img.getHistogramRGB(img);

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

			distance = 0;
		}

		//Liste aufsteigend sortieren
		Arrays.sort(list);

		return getSortL1DistanceList(list);
	}
}