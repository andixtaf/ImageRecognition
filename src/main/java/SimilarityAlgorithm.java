import image.MRImage;

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
	Vector<MRImage> apply(MRImage query, Vector<MRImage> repository, int segstep);

}
