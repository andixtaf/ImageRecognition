package com.and1.algorithm;

import com.and1.algorithm.sort.SortL1Distance;
import com.and1.model.img.Image;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//Klasse zur Berechnung der com.and1.algorithm.Intersection
public class HSISeg_L1Distance extends SimilarityAlgorithm
{

	public List<Image> apply(Image query, List<Image> repository, int segStep)
	{
		float distanceseg1 = 0;
		float distance = 0;
		List<Float> distanceseg = new ArrayList<>();
		String name = query.toString();

		List<BufferedImage> segment;
		List hist1 = new ArrayList<>();
		segment = query.generateRasterInGivenSteps(segStep);
		for (int i = 0; i < segment.size(); i++)
		{
			Image seg = new Image(query.getFilePath(), segment.get(i));
			seg.generateHistogramHSI(segStep + "Seg" + i + name);
			float[][][] hist1seg = seg.getHistogramHSI(segStep + "Seg" + i + name);
			hist1.add(hist1seg);
		}
		float[][][] hist2seg;
		float[][][] h1seg;
		float[][][] h2seg;

		//Liste in die die das Img und die dazugeh�rige com.and1.algorithm.Intersection als Tupel gespeichert werden
		SortL1Distance[] list = new SortL1Distance[repository.size()];
		for (int i = 0; i < repository.size(); i++)
		{
			Image img = repository.get(i);
			String imgname = img.toString();
			List<BufferedImage> segmenthist2;
			List hist2 = new ArrayList<>();

			segmenthist2 = img.generateRasterInGivenSteps(segStep);

			int totalhist2 = img.getHeight() * img.getWidth();

			for (int j = 0; j < segmenthist2.size(); j++)
			{
				Image seghist2 = new Image(img.getFilePath(), segmenthist2.get(j));
				seghist2.generateHistogramHSI(segStep + "Seg" + j + imgname);
				hist2seg = seghist2.getHistogramHSI(segStep + "Seg" + j + imgname);
				hist2.add(hist2seg);
			}

			for (int k = 0; k < segmenthist2.size(); k++)
			{
				h1seg = (float[][][]) (hist1.get(k));
				h2seg = (float[][][]) (hist2.get(k));
				for (int l = 0; l < 18; l++)
				{
					for (int m = 0; m < 3; m++)
					{
						for (int n = 0; n < 3; n++)
						{
							distanceseg1 += Math.abs((h1seg[l][m][n] / totalhist2) - (h2seg[l][m][n] / totalhist2));
						}
					}
				}
				distanceseg.add(distanceseg1);
				distanceseg1 = 0;
			}

			for (Float aDistanceseg : distanceseg)
			{
				distance += aDistanceseg;
			}

			list[i] = new SortL1Distance(img, distance / segStep);
			distance = 0;
			distanceseg.clear();
			hist2.clear();
			segmenthist2.clear();
		}
		//Liste absteigend sortieren
		Arrays.sort(list);

		return getSortL1DistanceList(list);
	}

}
