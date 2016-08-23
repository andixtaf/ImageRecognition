package com.and1.algorithm;

import com.and1.model.img.Image;

import java.util.Vector;

/**
 * Basic interface for similarity algorithms
 *
 * @author David Zellhoefer
 */
// TODO make this class abstract and put all equal functions in here
abstract class SimilarityAlgorithm
{

	/**
	 * Applies the similarity algorithm
	 *
	 * @param query      The query image
	 * @param repository The repository of all available images
	 * @return A list excluding the query image ordered descending by their similarity
	 */
	abstract Vector<Image> apply(Image query, Vector<Image> repository, int segStep);

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
