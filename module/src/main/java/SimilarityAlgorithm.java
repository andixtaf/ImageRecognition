import java.util.Vector;

/**
 * Basic interface for similarity algorithms
 * @author David Zellhoefer
 *
 */
public interface SimilarityAlgorithm {

	/**
	 * Applies the similarity algorithm
	 * @param query The query image
	 * @param repository The repository of all available images
	 * @return A list excluding the query image ordered descending by their similarity
	 */
	public Vector<MRImage> apply(MRImage query, Vector<MRImage> repository, int segstep);

}
