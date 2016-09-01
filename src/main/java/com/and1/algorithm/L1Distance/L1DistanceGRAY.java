package com.and1.algorithm.L1Distance;

import com.and1.algorithm.SimilarityAlgorithm;
import com.and1.model.img.Image;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;

public class L1DistanceGRAY extends SimilarityAlgorithm
{
	private static final Logger logger = LogManager.getLogger(L1DistanceGRAY.class);

	/**
	 * Generates random similarity values for all images relative to the query image
	 *
	 * @return The unprocessed repository
	 */
	public List<Image> calculateSimilarity(Image basicImage, List<Image> repository, int segStep)
	{
		SortL1Distance[] list;

		float distance = 0;
		int totalHistogramBase = basicImage.getHeight() * basicImage.getWidth();

		String name = basicImage.getFilePath().getAbsolutePath();
		float[] hist1 = basicImage.getHistogramGray(name);
		//System.out.println("Histogramm1 :" + query.filePath);
		float[] hist2;
		//Liste in die die das Img und die dazugehörige com.and1.algorithm.IntersectionRGB als Tupel gespeichert werden
		list = new SortL1Distance[repository.size()];
		for (int i = 0; i < repository.size(); i++)
		{
			Image img = repository.get(i);
			String imgname = img.getFilePath().getAbsolutePath();
			hist2 = img.getHistogramGray(imgname);

			for (int j = 0; j < hist1.length; j++)
			{
				distance += Math.abs(hist1[j] - hist2[j]);
			}

			list[i] = new SortL1Distance(img, distance);

			distance = 0;
		}

		//Liste aufsteigend sortieren
		Arrays.sort(list);

		return getSortL1DistanceList(list);
	}
}