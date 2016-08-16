
import image.MRImage;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Vector;

public class Intersection implements SimilarityAlgorithm
{

	public Vector<MRImage> apply(image.MRImage query, Vector<image.MRImage> repository, int segstep)
	{
		float intersection;
		float minsum = 0;
		float hist1sum = 0;
		if(query.getImage().getType() == BufferedImage.TYPE_BYTE_GRAY) {
			String name = query.filePath.getAbsolutePath();
			float[] hist1 = query.getHistogramGray(name);

			float[] hist2;

			//Liste in die die das Img und die dazugeh�rige Intersection als Tupel gespeichert werden
			SortIntersection[] list = new SortIntersection[repository.size()];
			for(int i = 0; i < repository.size(); i++) {
				image.MRImage img = repository.get(i);
				String imgname = img.filePath.getAbsolutePath();

				hist2 = img.getHistogramGray(imgname);

				for(int j = 0; j < hist1.length; j++) {
					minsum += Math.min(hist1[j], hist2[j]);
					hist1sum += hist1[j];
				}

				intersection = minsum / hist1sum;
				list[i] = new SortIntersection(img, intersection);

				minsum = 0;
				hist1sum = 0;

			}
			//Liste absteigend sortieren
			Arrays.sort(list);
			Vector<image.MRImage> sortedlist = new Vector<>();
			for(SortIntersection aList : list) {
				float intersect = aList.getIntersection();
				image.MRImage image = aList.getMRImage();

				sortedlist.add(image);
				image.setSimilarity(intersect, image);
			}

			return sortedlist;
		} else {
			String name = query.filePath.getAbsolutePath();
			float[][][] hist1 = query.getHistogramRGB(name);

			float[][][] hist2;

			//Liste in die die das Img und die dazugeh�rige Intersection als Tupel gespeichert werden
			SortIntersection[] list = new SortIntersection[repository.size()];
			for(int i = 0; i < repository.size(); i++) {
				image.MRImage img = repository.get(i);
				String imgname = img.filePath.getAbsolutePath();
				System.out.println(imgname);

				hist2 = img.getHistogramRGB(imgname);

				for(int j = 0; j < 8; j++) {
					for(int k = 0; k < 8; k++) {
						for(int l = 0; l < 8; l++) {
							minsum += Math.min(hist1[j][k][l], hist2[j][k][l]);
							hist1sum += hist1[j][k][l];
						}
					}
				}

				intersection = minsum / hist1sum;

				list[i] = new SortIntersection(img, intersection);
				minsum = 0;
				hist1sum = 0;

			}
			//Liste absteigend sortieren
			Arrays.sort(list);
			Vector<image.MRImage> sortedlist = new Vector<>();
			for(SortIntersection aList : list) {
				float intersect = aList.getIntersection();
				image.MRImage image = aList.getMRImage();
				//neues Repository erstellt welches sortierte Elemente enth�lt
				sortedlist.add(image);
				image.setSimilarity(intersect, image);
			}

			return sortedlist;
		}

	}
}