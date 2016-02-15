import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Vector;

//Klasse zur Berechnung der Intersection
public class Seg_L1Distance implements SimilarityAlgorithm {


	public Vector<MRImage> apply(MRImage query, Vector<MRImage> repository, int segstep) {
		float distanceseg1 = 0;
		float distance = 0;
		//Falls Image ein Graustufenbild ist
		Vector<Float> distanceseg = new Vector<Float>();
		//Falls Image ein Graustufenbild ist
		if (query.getImage().getType() == query.getImage().TYPE_BYTE_GRAY) {
			String name = query.toString();
			Vector<BufferedImage> segment = new Vector<BufferedImage>();
			Vector hist1 = new Vector();
			//Image in 4 Teile zerlegen
			segment = query.generateRaster(segstep);
			//neues MRImage f�r jedes Teilbild erzeugen
			for (int i = 0; i < segment.size(); i++){
				MRImage seg = new MRImage (query.filePath,segment.get(i));
				seg.generateHistogramGray(segstep + "Seg" + i + "Gray" + name);
				float [] hist1seg = seg.getHistogramGray(segstep + "Seg" + i + "Gray" + name);
				hist1.add(hist1seg);
			}
			//Histogramme laden

			//System.out.println("Histogramm1 :" + query.filePath);
			float [] hist2seg = null;
			float [] h1seg = null;
			float [] h2seg = null;
			//Liste in die die das Img und die dazugeh�rige Intersection als Tupel gespeichert werden
			SortL1Distance [] list  = new SortL1Distance[repository.size()];
			for (int i = 0; i < repository.size(); i++) {
				MRImage img = repository.get(i);
				String imgname = img.toString();
				Vector<BufferedImage> segmenthist2 = new Vector<BufferedImage>();
				Vector hist2 = new Vector();

				segmenthist2 = img.generateRaster(segstep);

				for (int j = 0; j < segmenthist2.size(); j++){
					MRImage seghist2 = new MRImage (img.filePath,segmenthist2.get(j));
					seghist2.generateHistogramGray(segstep + "Seg" + j + "RGB" + imgname);
					hist2seg = seghist2.getHistogramGray(segstep + "Seg" + j + "RGB" + imgname);
					hist2.add(hist2seg);
				}

				for (int k = 0; k < segmenthist2.size(); k++) {
					h1seg = (float []) (hist1.get(k));
					h2seg = (float []) (hist2.get(k));
					for (int l = 0; l < 256; l++) {
						distanceseg1 += Math.abs(h1seg[l] - h2seg[l]);
					}
					//System.out.println("Teil-Distance");
					//System.out.println(distanceseg1);
					distanceseg.add(distanceseg1);
					distanceseg1 = 0;
				}

				for (int n = 0; n < distanceseg.size(); n++) {
					distance += (float)(distanceseg.get(n));

				}


				list[i] = new SortL1Distance(img,distance/segstep);

				//System.out.println("Distance");
				//System.out.println(distance);

				//distanceseg1 = 0;
				distance = 0;
				distanceseg.removeAllElements();
				hist2.removeAllElements();
				segmenthist2.removeAllElements();
			}
			//Liste absteigend sortieren
			Arrays.sort(list);
			Vector<MRImage> sortedlist = new Vector<MRImage>();
			for(int i = 0; i < list.length; i++){
				float dist = list[i].getDistance();
				MRImage image = list[i].getMRImage();
				//neues Repository erstellt welches sortierte Elemente enth�lt
				sortedlist.add(image);
				image.setSimilarity(dist, image);
			}

			return sortedlist;
		}
		//Falls Image RGB Bild ist
		else {
			String name = query.toString();
			int totalhist1 = query.getHeight() * query.getWidth();
			Vector<BufferedImage> segment = new Vector<BufferedImage>();
			Vector hist1 = new Vector();
			segment = query.generateRaster(segstep);
			for (int i = 0; i < segment.size(); i++){
				MRImage seg = new MRImage (query.filePath,segment.get(i));
				seg.generateHistogramRGB(segstep + "Seg" + i + name);
				float [][][] hist1seg = seg.getHistogramRGB(segstep + "Seg" + i + name);
				hist1.add(hist1seg);
			}
			//System.out.println("Histogramm1 :" + query.filePath);
			float [][][] hist2seg = null;
			float [][][] h1seg = null;
			float [][][] h2seg = null;
			//Liste in die die das Img und die dazugeh�rige Intersection als Tupel gespeichert werden
			SortL1Distance [] list  = new SortL1Distance[repository.size()];
			for (int i = 0; i < repository.size(); i++) {
				MRImage img = repository.get(i);
				String imgname = img.toString();
				Vector<BufferedImage> segmenthist2 = new Vector<BufferedImage>();
				Vector hist2 = new Vector();

				segmenthist2 = img.generateRaster(segstep);

				int totalhist2 = img.getHeight() * img.getWidth();

				for (int j = 0; j < segmenthist2.size(); j++){
					MRImage seghist2 = new MRImage (img.filePath,segmenthist2.get(j));
					seghist2.generateHistogramRGB(segstep + "Seg" + j + imgname);
					hist2seg = seghist2.getHistogramRGB(segstep + "Seg" + j + imgname);
					hist2.add(hist2seg);
				}

				for (int k = 0; k < segmenthist2.size(); k++) {
					h1seg = (float [][][]) (hist1.get(k));
					h2seg = (float [][][]) (hist2.get(k));
					for (int l = 0; l < 8; l++) {
						for (int m = 0; m < 8; m++) {
							for (int n = 0; n < 8; n++) {
								distanceseg1 += Math.abs((h1seg[l][m][n]/totalhist1) - (h2seg[l][m][n]/totalhist2));
							}
						}
					}
					distanceseg.add(distanceseg1);
					distanceseg1 = 0;
				}

				for (int n = 0; n < distanceseg.size(); n++) {
					distance += (float)(distanceseg.get(n));
				}

				list[i] = new SortL1Distance(img,distance/segstep);
				distance = 0;
				distanceseg.removeAllElements();
				hist2.removeAllElements();
				segmenthist2.removeAllElements();

			}
			//Liste absteigend sortieren
			Arrays.sort(list);
			Vector<MRImage> sortedlist = new Vector<MRImage>();
			for(int i = 0; i < list.length; i++){
				float dist = list[i].getDistance();
				MRImage image = list[i].getMRImage();
				//neues Repository erstellt welches sortierte Elemente enth�lt
				sortedlist.add(image);
				image.setSimilarity(dist, image);
			}

			return sortedlist;
		}
	}
}    