package com.and1.algorithm.L1Distance;

import com.and1.algorithm.SimilarityAlgorithm;
import com.and1.model.img.Image;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

//Klasse zur Berechnung der Distance
public class L1DistanceRGB extends SimilarityAlgorithm
{
	private static final Logger logger = LogManager.getLogger(L1DistanceRGB.class);

	/**
	 * Generates random similarity values for all images relative to the query image
	 *
	 * @return The unprocessed repository
	 */
	public List<Image> calculateSimilarity(Image basicImage, List<Image> repository, int segStep)
	{
		SortL1Distance[] list;

		float distance = 0;
		int totalhist1 = basicImage.getHeight() * basicImage.getWidth();
		if(basicImage.getImage().getType() == BufferedImage.TYPE_BYTE_GRAY)
		{
			String name = basicImage.getFilePath().getAbsolutePath();
			float[] hist1 = basicImage.getHistogramGray(name);
			//System.out.println("Histogramm1 :" + query.filePath);
			float[] hist2;
			//Liste in die die das Img und die dazugehörige com.and1.algorithm.IntersectionRGB als Tupel gespeichert werden
			list = new SortL1Distance[repository.size()];
			for (int i = 0; i < repository.size(); i++)
			{
				Image img = repository.get(i);
				String imgname = img.getFilePath().getAbsolutePath();
				hist2 = img.getHistogramGray(imgname);

				for (int j = 0; j < hist1.length; j++)
				{
					distance += Math.abs(hist1[j] - hist2[j]);
				}

				list[i] = new SortL1Distance(img, distance);

				distance = 0;
			}
		}
		else
		{
			String name = basicImage.getFilePath().getAbsolutePath();
			float[][][] hist1 = basicImage.getHistogramRGB(name);
			//System.out.println("Histogramm1 :" + query.filePath);
			float[][][] hist2;
			//Liste in die die das Img und die dazugeh�rige com.and1.algorithm.IntersectionRGB als Tupel gespeichert werden
			list = new SortL1Distance[repository.size()];
			for (int i = 0; i < repository.size(); i++)
			{
				Image img = repository.get(i);
				int totalhist2 = img.getHeight() * img.getWidth();
				String imgname = img.getFilePath().getAbsolutePath();
				//System.out.println(i);
				hist2 = img.getHistogramRGB(imgname);
				//System.out.println("Histogramm1 :" + query.filePath);
				//System.out.println("Histogramm2 :" + com.and1.model.img.filePath);
				for (int j = 0; j < 8; j++)
				{
					for (int k = 0; k < 8; k++)
					{
						for (int l = 0; l < 8; l++)
						{
							distance += Math.abs(hist1[j][k][l] / totalhist1 - hist2[j][k][l] / totalhist2);
						}
					}
				}

				list[i] = new SortL1Distance(img, distance);
				//System.out.println("Distance");
				//System.out.println(distance);
				distance = 0;
			}
		}

		//Liste aufsteigend sortieren
		Arrays.sort(list);

		return getSortL1DistanceList(list);
	}
}