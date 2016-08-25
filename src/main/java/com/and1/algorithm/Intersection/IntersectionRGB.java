package com.and1.algorithm.Intersection;

import com.and1.algorithm.SimilarityAlgorithm;
import com.and1.model.img.Image;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;

public class IntersectionRGB extends SimilarityAlgorithm
{
	private static final Logger logger = LogManager.getLogger(IntersectionRGB.class);

	public List<Image> calculateSimilarity(Image basicImage, List<Image> repository, int segStep)
	{
		float intersection;
		float minSum;
		float histogramBasicSum;

		SortIntersection[] list = new SortIntersection[repository.size()];

		String name = basicImage.getFilePath().getAbsolutePath();

		float[][][] histogramBasic = basicImage.getHistogramRGB(name);

		float[][][] histogramToCompare;

		histogramBasicSum = getMinSumForHistogramRGB(histogramBasic, histogramBasic);

		for(int i = 0; i < repository.size(); i++)
		{
			Image img = repository.get(i);
			String imageName = img.getFilePath().getAbsolutePath();

			logger.info(imageName);

			histogramToCompare = img.getHistogramRGB(imageName);

			minSum = getMinSumForHistogramRGB(histogramBasic, histogramToCompare);

			intersection = minSum / histogramBasicSum;

			logger.info("minSum: " + minSum);
			logger.info("histogram1Sum: " + histogramBasicSum);
			logger.info("intersection: " + intersection);

			list[i] = new SortIntersection(img, intersection);
		}

		Arrays.sort(list);

		return getSortIntersectionList(list);
	}

	private float getMinSumForHistogramRGB(float[][][] histogramBasic, float[][][] histogramToCompare)
	{
		float minSum = 0;
		for(int j = 0; j < 8; j++)
		{
			for(int k = 0; k < 8; k++)
			{
				for(int l = 0; l < 8; l++)
				{
					minSum += Math.min(histogramBasic[j][k][l], histogramToCompare[j][k][l]);
				}
			}
		}
		return minSum;
	}

}