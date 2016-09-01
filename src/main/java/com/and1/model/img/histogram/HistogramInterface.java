package com.and1.model.img.histogram;

import java.awt.image.BufferedImage;

/**
 * package:  com.and1.model.img.histogram
 * Created by Andreas on 01.09.2016 for ImageRecognition.
 */
public interface HistogramInterface
{
	void generateHistogram(BufferedImage image, String name);

	void saveHistogram(float[] histogram, String name);

	void normalize(float[] histogram, int countOfTotalPixel);
}
