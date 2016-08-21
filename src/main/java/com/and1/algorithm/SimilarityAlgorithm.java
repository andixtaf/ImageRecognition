package com.and1.algorithm;

import com.and1.img.Image;

import java.util.Vector;

/**
 * Basic interface for similarity algorithms
 *
 * @author David Zellhoefer
 */
interface SimilarityAlgorithm
{

	/**
	 * Applies the similarity algorithm
	 *
	 * @param query      The query image
	 * @param repository The repository of all available images
	 * @return A list excluding the query image ordered descending by their similarity
	 */
	Vector<Image> apply(Image query, Vector<Image> repository, int segStep);

}
