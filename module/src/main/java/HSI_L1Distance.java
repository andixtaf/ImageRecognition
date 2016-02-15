import java.util.Arrays;
import java.util.Vector;

//Klasse zur Berechnung der Distance
public class HSI_L1Distance implements SimilarityAlgorithm
{

	/**
	 * Generates random similarity values for all images relative to the query image
	 *
	 * @return The unprocessed repository
	 */
	public Vector<MRImage> apply(MRImage query, Vector<MRImage> repository, int segstep)
	{

		float distance = 0;
		String name = query.toString();
		//HSI Histogramm generieren
		query.generateHistogramHSI(name);
		//Histogramm laden
		float[][][] hist1 = query.getHistogramHSI(name);
		float[][][] hist2;

		int totalhist1 = query.getHeight() * query.getWidth();
		//Liste in die die das Img und die dazugehörige Intersection als Tupel gespeichert werden
		SortL1Distance[] list = new SortL1Distance[repository.size()];
		for(int i = 0; i < repository.size(); i++) {
			MRImage img = repository.get(i);
			int totalhist2 = img.getHeight() * img.getWidth();
			String imgname = img.toString();
			img.generateHistogramHSI(imgname);
			hist2 = img.getHistogramHSI(imgname);
			for(int j = 0; j < 18; j++) {
				for(int k = 0; k < 3; k++) {
					for(int l = 0; l < 3; l++) {
						distance += Math.abs(hist1[j][k][l] / totalhist1 - hist2[j][k][l] / totalhist2);
					}
				}
			}

			list[i] = new SortL1Distance(img, distance);
			//System.out.println("Distance");
			//System.out.println(distance);
			distance = 0;
		}
		//Liste aufsteigend sortieren
		Arrays.sort(list);
		Vector<MRImage> sortedlist = new Vector<>();
		for(SortL1Distance aList : list) {
			float dist = aList.getDistance();
			MRImage image = aList.getMRImage();
			//neues Repository erstellt welches sortierte Elemente enthält
			sortedlist.add(image);
			image.setSimilarity(dist, image);
		}

		return sortedlist;
	}
}