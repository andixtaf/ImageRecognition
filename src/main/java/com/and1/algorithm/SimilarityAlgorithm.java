package com.and1.algorithm;

import com.and1.algorithm.sort.SortIntersection;
import com.and1.algorithm.sort.SortL1Distance;
import com.and1.model.img.Image;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Basic interface for similarity algorithms
 *
 * @author David Zellhoefer
 */
// TODO make this class abstract and put all equal functions in here
public abstract class SimilarityAlgorithm
{

	/**
	 * Applies the similarity algorithm
	 *
	 * @param query      The query image
	 * @param repository The repository of all available images
	 * @return A list excluding the query image ordered descending by their similarity
	 */
	public abstract List<Image> apply(Image query, List<Image> repository, int segStep);

	@NotNull
	List<Image> getSortIntersectionList(SortIntersection[] list)
	{
		List<Image> sortedlist = new ArrayList<>();

		for(SortIntersection aList : list)
		{
			float intersect = aList.getIntersection();
			Image image = aList.getMRImage();

			sortedlist.add(image);
			image.setSimilarity(intersect, image);
		}

		return sortedlist;
	}

	@NotNull
	List<Image> getSortL1DistanceList(SortL1Distance[] list)
	{
		List<Image> sortedlist = new ArrayList<>();
		for(SortL1Distance aList : list)
		{
			float dist = aList.getDistance();
			Image image = aList.getMRImage();
			//neues Repository erstellt welches sortierte Elemente enth�lt
			sortedlist.add(image);
			image.setSimilarity(dist, image);
		}

		return sortedlist;
	}

	public class Pocket<T>
	{
		private T value;

		public Pocket() {}

		public Pocket( T value ) { this.value = value; }

		public void set( T value ) { this.value = value; }

		public T get() { return value; }

		public boolean isEmpty() { return value != null; }

		public void empty() { value = null; }
	}

}
