package com.and1.algorithm;

import com.and1.algorithm.sort.SortIntersection;
import com.and1.model.img.Image;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//Klasse zur Berechnung der com.and1.algorithm.Intersection
public class Seg_Intersection extends SimilarityAlgorithm
{

	public List<Image> apply(Image query, List<Image> repository, int segStep)
	{
		float intersectionSequence1;
		float intersection = 0;
		float minimumSumSequence1 = 0;
		float historySumSequence1 = 0;
		List<Float> intersectionSequence = new ArrayList<>();

		SortIntersection[] list;

		//Falls image ein Graustufenbild ist
		if (query.getImage().getType() == BufferedImage.TYPE_BYTE_GRAY)
		{
			String name = query.toString();
			List<BufferedImage> segment;
			List hist1 = new ArrayList<>();
			//image in 4 Teile zerlegen
			segment = query.generateRasterInGivenSteps(segStep);

			//neues com.and1.model.img.and1.Image f�r jedes Teilbild erzeugen
			for (int i = 0; i < segment.size(); i++)
			{
				Image seg = new Image(query.getFilePath(), segment.get(i));
				seg.generateHistogramGray(segStep + "Seg" + i + "Gray" + name);
				float[] hist1seg = seg.getHistogramGray(segStep + "Seg" + i + "Gray" + name);
				hist1.add(hist1seg);
			}

			//Histogramme laden

			//System.out.println("Histogramm1 :" + query.filePath);
			float[] hist2seg;
			float[] h1seg;
			float[] h2seg;
			//Liste in die die das Img und die dazugeh�rige com.and1.algorithm.Intersection als Tupel gespeichert werden
			list = new SortIntersection[repository.size()];
			for (int i = 0; i < repository.size(); i++)
			{
				Image img = repository.get(i);
				String imgname = img.toString();
				List<BufferedImage> segmenthist2;
				List hist2 = new ArrayList<>();

				segmenthist2 = img.generateRasterInGivenSteps(segStep);

				for (int j = 0; j < segmenthist2.size(); j++)
				{
					Image seghist2 = new Image(img.getFilePath(), segmenthist2.get(j));
					seghist2.generateHistogramGray(segStep + "Seg" + j + "RGB" + imgname);
					hist2seg = seghist2.getHistogramGray(segStep + "Seg" + j + "RGB" + imgname);
					hist2.add(hist2seg);
				}
				//Teilhistogramme miteinander vergleichen
				for (int k = 0; k < segmenthist2.size(); k++)
				{
					h1seg = (float[]) (hist1.get(k));
					h2seg = (float[]) (hist2.get(k));
					for (int l = 0; l < 256; l++)
					{
						minimumSumSequence1 += Math.min(h1seg[l], h2seg[l]);
						historySumSequence1 += h1seg[l];
					}

					intersectionSequence1 = minimumSumSequence1 / historySumSequence1;
					intersectionSequence.add(intersectionSequence1);
					minimumSumSequence1 = 0;
					historySumSequence1 = 0;
				}

				for (Float anIntersectionseg : intersectionSequence)
				{
					intersection += anIntersectionseg;
				}

				list[i] = new SortIntersection(img, intersection / segStep);

				intersection = 0;
				intersectionSequence.clear();
				hist2.clear();
				segmenthist2.clear();
			}
		}
		//Falls image ein RGB Bild ist
		else
		{
			String name = query.toString();
			List<BufferedImage> segment;
			List hist1 = new ArrayList<>();
			segment = query.generateRasterInGivenSteps(segStep);
			for (int i = 0; i < segment.size(); i++)
			{
				Image seg = new Image(query.getFilePath(), segment.get(i));
				seg.generateHistogramRGB(segStep + "Seg" + i + name);
				float[][][] hist1seg = seg.getHistogramRGB(segStep + "Seg" + i + name);
				hist1.add(hist1seg);
			}
			//System.out.println("Histogramm1 :" + query.filePath);
			float[][][] hist2seg;
			float[][][] h1seg;
			float[][][] h2seg;
			//Liste in die die das Img und die dazugeh�rige com.and1.algorithm.Intersection als Tupel gespeichert werden
			list = new SortIntersection[repository.size()];
			for (int i = 0; i < repository.size(); i++)
			{
				Image img = repository.get(i);
				String imgname = img.toString();
				List<BufferedImage> segmenthist2;
				List hist2 = new ArrayList<>();

				segmenthist2 = img.generateRasterInGivenSteps(segStep);

				for (int j = 0; j < segmenthist2.size(); j++)
				{
					Image seghist2 = new Image(img.getFilePath(), segmenthist2.get(j));
					seghist2.generateHistogramRGB(segStep + "Seg" + j + imgname);
					hist2seg = seghist2.getHistogramRGB(segStep + "Seg" + j + imgname);
					hist2.add(hist2seg);
				}

				for (int k = 0; k < segmenthist2.size(); k++)
				{
					h1seg = (float[][][]) (hist1.get(k));
					h2seg = (float[][][]) (hist2.get(k));
					for (int l = 0; l < 8; l++)
					{
						for (int m = 0; m < 8; m++)
						{
							for (int n = 0; n < 8; n++)
							{
								minimumSumSequence1 += Math.min(h1seg[l][m][n], h2seg[l][m][n]);
								historySumSequence1 += h1seg[l][m][n];
							}
						}
					}

					intersectionSequence1 = minimumSumSequence1 / historySumSequence1;
					intersectionSequence.add(intersectionSequence1);
					minimumSumSequence1 = 0;
					historySumSequence1 = 0;
				}

				for (Float anIntersectionseg : intersectionSequence)
				{
					intersection += anIntersectionseg;
				}
				list[i] = new SortIntersection(img, intersection / segStep);

				intersection = 0;
				intersectionSequence.clear();
				hist2.clear();
				segmenthist2.clear();
			}
			//Liste absteigend sortieren

		}

		Arrays.sort(list);

		return getSortIntersectionList(list);
	}
}    
