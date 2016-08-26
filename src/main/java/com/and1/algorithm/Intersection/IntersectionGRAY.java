package com.and1.algorithm.Intersection;

import com.and1.algorithm.SimilarityAlgorithm;
import com.and1.model.img.Image;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Andreas on 25.08.2016 for ImageRecognition.
 */
public class IntersectionGRAY extends SimilarityAlgorithm
{
	private static final Logger logger = LogManager.getLogger(IntersectionGRAY.class);

	public List<Image> calculateSimilarity(Image basicImage, List<Image> repository, int segStep)
	{
		float intersection;
		float minSum;
		float histogramBasicSum;

		SortIntersection[] list = new SortIntersection[repository.size()];

		String name = basicImage.getFilePath().getAbsolutePath();

		float[] histogramBasic = basicImage.getHistogramGray(name);
		float[] histogramToCompare;

		histogramBasicSum = getMinSumForGrayHistogram(histogramBasic, histogramBasic);

		for(int i = 0; i < repository.size(); i++)
		{
			Image img = repository.get(i);
			String imageName = img.getFilePath().getAbsolutePath();

			histogramToCompare = img.getHistogramGray(imageName);

			minSum = getMinSumForGrayHistogram(histogramBasic, histogramToCompare);

			intersection = minSum / histogramBasicSum;

			list[i] = new SortIntersection(img, intersection);
		}

		Arrays.sort(list);

		return getSortIntersectionList(list);
	}

	private float getMinSumForGrayHistogram(float[] histogramBasic, float[] histogramToCompare)
	{
		float minSum = 0;

		for(int j = 0; j < histogramBasic.length; j++)
		{
			minSum += Math.min(histogramBasic[j], histogramToCompare[j]);
		}

		logger.info("minSum: " + minSum);
		return minSum;
	}

}
