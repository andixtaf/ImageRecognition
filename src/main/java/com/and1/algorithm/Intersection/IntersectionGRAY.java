package com.and1.algorithm.Intersection;

import com.and1.algorithm.SimilarityAlgorithm;
import com.and1.model.img.Image;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
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

		List<SortIntersection> intersectionList = new ArrayList<>();

		float[] histogramBasic = basicImage.getHistogramGray(basicImage);
		float[] histogramToCompare;

		histogramBasicSum = getMinSumForGrayHistogram(histogramBasic, histogramBasic);


		int i = 0;
		for(Image img : repository)
		{
			histogramToCompare = img.getHistogramGray(img);

			minSum = getMinSumForGrayHistogram(histogramBasic, histogramToCompare);

			intersection = minSum / histogramBasicSum;

			intersectionList.add(new SortIntersection(img, intersection));
		}

		return getSortIntersectionList(intersectionList);
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
