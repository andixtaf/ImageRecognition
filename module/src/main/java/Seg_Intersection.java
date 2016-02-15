import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Vector;

//Klasse zur Berechnung der Intersection
public class Seg_Intersection implements SimilarityAlgorithm
{

	public Vector<MRImage> apply(MRImage query, Vector<MRImage> repository, int segstep)
	{
		float intersectionseg1;
		float intersection = 0;
		float minsumseg1 = 0;
		float hist1sumseg1 = 0;
		Vector<Float> intersectionseg = new Vector<>();
		//Falls Image ein Graustufenbild ist
		if(query.getImage().getType() == BufferedImage.TYPE_BYTE_GRAY) {
			String name = query.toString();
			Vector<BufferedImage> segment;
			Vector hist1 = new Vector();
			//Image in 4 Teile zerlegen
			segment = query.generateRasterInGivenSteps(segstep);
			//neues MRImage für jedes Teilbild erzeugen
			for(int i = 0; i < segment.size(); i++) {
				MRImage seg = new MRImage(query.filePath, segment.get(i));
				seg.generateHistogramGray(segstep + "Seg" + i + "Gray" + name);
				float[] hist1seg = seg.getHistogramGray(segstep + "Seg" + i + "Gray" + name);
				hist1.add(hist1seg);
			}
			//Histogramme laden

			//System.out.println("Histogramm1 :" + query.filePath);
			float[] hist2seg;
			float[] h1seg;
			float[] h2seg;
			//Liste in die die das Img und die dazugehörige Intersection als Tupel gespeichert werden
			SortIntersection[] list = new SortIntersection[repository.size()];
			for(int i = 0; i < repository.size(); i++) {
				MRImage img = repository.get(i);
				String imgname = img.toString();
				Vector<BufferedImage> segmenthist2;
				Vector hist2 = new Vector();

				segmenthist2 = img.generateRasterInGivenSteps(segstep);

				for(int j = 0; j < segmenthist2.size(); j++) {
					MRImage seghist2 = new MRImage(img.filePath, segmenthist2.get(j));
					seghist2.generateHistogramGray(segstep + "Seg" + j + "RGB" + imgname);
					hist2seg = seghist2.getHistogramGray(segstep + "Seg" + j + "RGB" + imgname);
					hist2.add(hist2seg);
				}
				//Teilhistogramme miteinander vergleichen
				for(int k = 0; k < segmenthist2.size(); k++) {
					h1seg = (float[]) (hist1.get(k));
					h2seg = (float[]) (hist2.get(k));
					for(int l = 0; l < 256; l++) {
						minsumseg1 += Math.min(h1seg[l], h2seg[l]);
						hist1sumseg1 += h1seg[l];
					}

					intersectionseg1 = minsumseg1 / hist1sumseg1;
					intersectionseg.add(intersectionseg1);
					intersectionseg1 = 0;
					minsumseg1 = 0;
					hist1sumseg1 = 0;
				}

				for(Float anIntersectionseg : intersectionseg) {
					intersection += anIntersectionseg;
				}

				list[i] = new SortIntersection(img, intersection / segstep);

				intersection = 0;
				intersectionseg.removeAllElements();
				hist2.removeAllElements();
				segmenthist2.removeAllElements();
			}
			//Liste absteigend sortieren
			Arrays.sort(list);
			Vector<MRImage> sortedlist = new Vector<>();
			for(SortIntersection aList : list) {
				float intersect = aList.getIntersection();
				MRImage image = aList.getMRImage();
				//neues Repository erstellt welches sortierte Elemente enthält
				sortedlist.add(image);
				image.setSimilarity(intersect, image);
			}

			return sortedlist;
		}
		//Falls Image ein RGB Bild ist
		else {
			String name = query.toString();
			Vector<BufferedImage> segment;
			Vector hist1 = new Vector();
			segment = query.generateRasterInGivenSteps(segstep);
			for(int i = 0; i < segment.size(); i++) {
				MRImage seg = new MRImage(query.filePath, segment.get(i));
				seg.generateHistogramRGB(segstep + "Seg" + i + name);
				float[][][] hist1seg = seg.getHistogramRGB(segstep + "Seg" + i + name);
				hist1.add(hist1seg);
			}
			//System.out.println("Histogramm1 :" + query.filePath);
			float[][][] hist2seg;
			float[][][] h1seg;
			float[][][] h2seg;
			//Liste in die die das Img und die dazugehörige Intersection als Tupel gespeichert werden
			SortIntersection[] list = new SortIntersection[repository.size()];
			for(int i = 0; i < repository.size(); i++) {
				MRImage img = repository.get(i);
				String imgname = img.toString();
				Vector<BufferedImage> segmenthist2;
				Vector hist2 = new Vector();

				segmenthist2 = img.generateRasterInGivenSteps(segstep);

				for(int j = 0; j < segmenthist2.size(); j++) {
					MRImage seghist2 = new MRImage(img.filePath, segmenthist2.get(j));
					seghist2.generateHistogramRGB(segstep + "Seg" + j + imgname);
					hist2seg = seghist2.getHistogramRGB(segstep + "Seg" + j + imgname);
					hist2.add(hist2seg);
				}

				for(int k = 0; k < segmenthist2.size(); k++) {
					h1seg = (float[][][]) (hist1.get(k));
					h2seg = (float[][][]) (hist2.get(k));
					for(int l = 0; l < 8; l++) {
						for(int m = 0; m < 8; m++) {
							for(int n = 0; n < 8; n++) {
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

				for(Float anIntersectionseg : intersectionseg) {
					intersection += anIntersectionseg;
				}
				list[i] = new SortIntersection(img, intersection / segstep);

				intersection = 0;
				intersectionseg.removeAllElements();
				hist2.removeAllElements();
				segmenthist2.removeAllElements();
			}
			//Liste absteigend sortieren
			Arrays.sort(list);
			Vector<MRImage> sortedlist = new Vector<>();
			for(SortIntersection aList : list) {
				float intersect = aList.getIntersection();
				MRImage image = aList.getMRImage();
				//neues Repository erstellt welches sortierte Elemente enthält
				sortedlist.add(image);
				image.setSimilarity(intersect, image);
			}

			return sortedlist;
		}
	}
}    
