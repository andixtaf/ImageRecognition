package com.and1.algorithm.Intersection;

import com.and1.algorithm.SimilarityAlgorithm;
import com.and1.model.img.Image;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//Klasse zur Berechnung der com.and1.algorithm.IntersectionRGB
public class IntersectionSegRGB extends SimilarityAlgorithm
{

	public List<Image> calculateSimilarity(Image basicImage, List<Image> repository, int segStep)
	{
		float intersectionSequence1;
		float intersection = 0;
		float minimumSumSequence1 = 0;
		float historySumSequence1 = 0;
		List<Float> intersectionSequence = new ArrayList<>();

		SortIntersection[] list;

		String name = basicImage.toString();
		List<BufferedImage> segment;
		List hist1 = new ArrayList<>();
		segment = basicImage.generateRasterInGivenSteps(segStep);
		for(int i = 0; i < segment.size(); i++)
		{
			Image seg = new Image(basicImage.getFilePath(), segment.get(i));
			seg.generateHistogramRGB(segStep + "Seg" + i + name);
			float[][][] hist1seg = seg.getHistogramRGB(segStep + "Seg" + i + name);
			hist1.add(hist1seg);
		}
		//System.out.println("Histogramm1 :" + query.filePath);
		float[][][] hist2seg;
		float[][][] h1seg;
		float[][][] h2seg;
		//Liste in die die das Img und die dazugeh�rige com.and1.algorithm.IntersectionRGB als Tupel gespeichert werden
		list = new SortIntersection[repository.size()];
		for(int i = 0; i < repository.size(); i++)
		{
			Image img = repository.get(i);
			String imgname = img.toString();
			List<BufferedImage> segmenthist2;
			List hist2 = new ArrayList<>();

			segmenthist2 = img.generateRasterInGivenSteps(segStep);

			for(int j = 0; j < segmenthist2.size(); j++)
			{
				Image seghist2 = new Image(img.getFilePath(), segmenthist2.get(j));
				seghist2.generateHistogramRGB(segStep + "Seg" + j + imgname);
				hist2seg = seghist2.getHistogramRGB(segStep + "Seg" + j + imgname);
				hist2.add(hist2seg);
			}

			for(int k = 0; k < segmenthist2.size(); k++)
			{
				h1seg = (float[][][]) (hist1.get(k));
				h2seg = (float[][][]) (hist2.get(k));
				for(int l = 0; l < 8; l++)
				{
					for(int m = 0; m < 8; m++)
					{
						for(int n = 0; n < 8; n++)
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

			for(Float anIntersectionseg : intersectionSequence)
			{
				intersection += anIntersectionseg;
			}
			list[i] = new SortIntersection(img, intersection / segStep);

		}

		//Liste absteigend sortieren
		Arrays.sort(list);

		return getSortIntersectionList(list);
	}
}    
