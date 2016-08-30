package com.and1.algorithm;

import com.and1.algorithm.Intersection.SortIntersection;
import com.and1.algorithm.L1Distance.SortL1Distance;
import com.and1.model.img.Image;

import java.util.ArrayList;
import java.util.List;

// TODO make this class abstract and put all equal functions in here
//TODO check calculation in threads
public abstract class SimilarityAlgorithm
{
	public abstract List<Image> calculateSimilarity(Image basicImage, List<Image> repository, int segStep);

	protected List<Image> getSortIntersectionList(SortIntersection[] list)
	{
		List<Image> sortedList = new ArrayList<>();

		for(SortIntersection aList : list)
		{
			float intersect = aList.getIntersection();
			Image image = aList.getMRImage();

			sortedList.add(image);
			image.setSimilarity(intersect, image);
		}

		return sortedList;
	}

	protected List<Image> getSortL1DistanceList(SortL1Distance[] list)
	{
		List<Image> sortedList = new ArrayList<>();
		for(SortL1Distance aList : list)
		{
			float dist = aList.getDistance();
			Image image = aList.getMRImage();

			sortedList.add(image);
			image.setSimilarity(dist, image);
		}

		return sortedList;
	}
}
