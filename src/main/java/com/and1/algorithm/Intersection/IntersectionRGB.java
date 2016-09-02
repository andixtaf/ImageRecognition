package com.and1.algorithm.Intersection;

import com.and1.algorithm.SimilarityAlgorithm;
import com.and1.model.img.Image;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class IntersectionRGB extends SimilarityAlgorithm
{
	private static final Logger logger = LogManager.getLogger(IntersectionRGB.class);

	public List<Image> calculateSimilarity(Image basicImage, List<Image> repository, int segStep)
	{
		float intersection;
		float minSum;
		float histogramBasicSum;

		List<SortIntersection> intersectionList = new ArrayList<>();

		float[][][] histogramBasic = basicImage.getHistogramRGB(basicImage);

		float[][][] histogramToCompare;

		histogramBasicSum = getMinSumForHistogramRGB(histogramBasic, histogramBasic);

		for(int i = 0; i < repository.size(); i++)
		{
			Image img = repository.get(i);

			histogramToCompare = img.getHistogramRGB(img);

			minSum = getMinSumForHistogramRGB(histogramBasic, histogramToCompare);

			intersection = minSum / histogramBasicSum;

			logger.info("minSum: " + minSum);
			logger.info("histogram1Sum: " + histogramBasicSum);
			logger.info("intersection: " + intersection);

			intersectionList.add(new SortIntersection(img, intersection));
		}

		return getSortIntersectionList(intersectionList);
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