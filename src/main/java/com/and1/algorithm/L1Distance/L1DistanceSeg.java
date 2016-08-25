package com.and1.algorithm.L1Distance;

import com.and1.algorithm.SimilarityAlgorithm;
import com.and1.model.img.Image;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//Klasse zur Berechnung der com.and1.algorithm.IntersectionRGB
public class L1DistanceSeg extends SimilarityAlgorithm
{

	//TODO avoid code duplication
	public List<Image> calculateSimilarity(Image basicImage, List<Image> repository, int segStep)
	{
		float distanceSegment1 = 0;
		float distance = 0;
		//Falls image ein Graustufenbild ist
		List<Float> distanceseg = new ArrayList<>();
		SortL1Distance[] list;

		//Falls image ein Graustufenbild ist
		if(basicImage.getImage().getType() == BufferedImage.TYPE_BYTE_GRAY)
		{
			String name = basicImage.toString();
			List<BufferedImage> segment;
			List hist1 = new ArrayList<>();

			//image in 4 Teile zerlegen
			segment = basicImage.generateRasterInGivenSteps(segStep);

			//neues Image für jedes Teilbild erzeugen
			for (int i = 0; i < segment.size(); i++)
			{
				Image seg = new Image(basicImage.getFilePath(), segment.get(i));
				seg.generateHistogramGray(segStep + "Seg" + i + "Gray" + name);
				float[] hist1seg = seg.getHistogramGray(segStep + "Seg" + i + "Gray" + name);
				hist1.add(hist1seg);
			}
			//Histogramme laden

			//System.out.println("Histogramm1 :" + query.filePath);
			float[] hist2seg;
			float[] h1seg;
			float[] h2seg;
			//Liste in die die das Img und die dazugeh�rige com.and1.algorithm.IntersectionRGB als Tupel gespeichert werden
			list = new SortL1Distance[repository.size()];
			for (int i = 0; i < repository.size(); i++)
			{
				Image img = repository.get(i);
				String imageName = img.toString();
				List<BufferedImage> segmentHistory2;
				List hist2 = new ArrayList<>();

				segmentHistory2 = img.generateRasterInGivenSteps(segStep);

				for (int j = 0; j < segmentHistory2.size(); j++)
				{
					Image seghist2 = new Image(img.getFilePath(), segmentHistory2.get(j));
					seghist2.generateHistogramGray(segStep + "Seg" + j + "RGB" + imageName);
					hist2seg = seghist2.getHistogramGray(segStep + "Seg" + j + "RGB" + imageName);
					hist2.add(hist2seg);
				}

				for (int k = 0; k < segmentHistory2.size(); k++)
				{
					h1seg = (float[]) (hist1.get(k));
					h2seg = (float[]) (hist2.get(k));
					for (int l = 0; l < 256; l++)
					{
						distanceSegment1 += Math.abs(h1seg[l] - h2seg[l]);
					}

					distanceseg.add(distanceSegment1);
					distanceSegment1 = 0;
				}

				for (Float aDistanceseg : distanceseg)
				{
					distance += aDistanceseg;
				}

				list[i] = new SortL1Distance(img, distance / segStep);

				//distanceseg1 = 0;
				distance = 0;
				distanceseg.clear();
				hist2.clear();
				segmentHistory2.clear();
			}

		}
		//Falls image RGB Bild ist
		else
		{
			String name = basicImage.toString();
			int totalhist1 = basicImage.getHeight() * basicImage.getWidth();
			List<BufferedImage> segment;
			List hist1 = new ArrayList<>();
			segment = basicImage.generateRasterInGivenSteps(segStep);
			for (int i = 0; i < segment.size(); i++)
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
			list = new SortL1Distance[repository.size()];
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
								distanceSegment1 +=
										Math.abs((h1seg[l][m][n] / totalhist1) - (h2seg[l][m][n] / totalhist2));
							}
						}
					}
					distanceseg.add(distanceSegment1);
					distanceSegment1 = 0;
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

		}
		//Liste absteigend sortieren
		Arrays.sort(list);

		return getSortL1DistanceList(list);
	}
}    
