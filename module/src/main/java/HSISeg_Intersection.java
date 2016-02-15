import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Vector;


//Klasse zur Berechnung der Intersection
public class HSISeg_Intersection implements SimilarityAlgorithm {


	public Vector<MRImage> apply(MRImage query, Vector<MRImage> repository, int segstep) {
		float intersectionseg1 = 0;
		float intersection = 0;
		float minsumseg1 = 0;
		float hist1sumseg1 = 0;
		Vector<Float> intersectionseg = new Vector<Float>();

		String name = query.toString();
		Vector<BufferedImage> segment = new Vector<BufferedImage>();
		Vector hist1 = new Vector();
		segment = query.generateRaster(segstep);
		for (int i = 0; i < segment.size(); i++){
			MRImage seg = new MRImage (query.filePath,segment.get(i));
			seg.generateHistogramHSI(segstep + "Seg" + i + "HSI" + name);
			float [][][] hist1seg = seg.getHistogramHSI(segstep + "Seg" + i + "HSI" + name);
			hist1.add(hist1seg);
		}
		float [][][] hist2seg = null;
		float [][][] h1seg = null;
		float [][][] h2seg = null;
		//Liste in die die das Img und die dazugehörige Intersection als Tupel gespeichert werden
		SortIntersection [] list  = new SortIntersection[repository.size()];

		for (int i = 0; i < repository.size(); i++) {
			MRImage img = repository.get(i);
			String imgname = img.toString();
			Vector<BufferedImage> segmenthist2 = new Vector<BufferedImage>();
			Vector hist2 = new Vector();


			segmenthist2 = img.generateRaster(segstep);

			for (int j = 0; j < segmenthist2.size(); j++){
				MRImage seghist2 = new MRImage (img.filePath,segmenthist2.get(j));
				seghist2.generateHistogramHSI(segstep + "Seg" + j + "HSI" + imgname);
				hist2seg = seghist2.getHistogramHSI(segstep + "Seg" + j + "HSI" + imgname);
				hist2.add(hist2seg);
			}

			for (int k = 0; k < segmenthist2.size(); k++) {
				h1seg = (float [][][]) (hist1.get(k));
				h2seg = (float [][][]) (hist2.get(k));
				for (int l = 0; l < 18; l++) {
					for (int m = 0; m < 3; m++) {
						for (int n = 0; n < 3; n++) {
							minsumseg1 += Math.min(h1seg[l][m][n], h2seg[l][m][n]);
							hist1sumseg1 += h1seg[l][m][n];
						}
					}
				}

				intersectionseg1 = minsumseg1 / hist1sumseg1;
				intersectionseg.add(intersectionseg1);
				intersectionseg1 = 0;
				minsumseg1 = 0;
				hist1sumseg1 = 0;
			}

			for (int n = 0; n < intersectionseg.size(); n++) {
				intersection += (float)(intersectionseg.get(n));
			}
			list[i] = new SortIntersection(img,intersection / segstep);

			intersection = 0;
			intersectionseg.removeAllElements();
			hist2.removeAllElements();
			segmenthist2.removeAllElements();
		}
		//Liste absteigend sortieren
		Arrays.sort(list);
		Vector<MRImage> sortedlist = new Vector<MRImage>();
		for(int i = 0; i < list.length; i++){
			float intersect = list[i].getIntersection();
			MRImage image = list[i].getMRImage();
			//neues Repository erstellt welches sortierte Elemente enthält
			sortedlist.add(image);
			image.setSimilarity(intersect, image);
		}

		return sortedlist;
	}
}
