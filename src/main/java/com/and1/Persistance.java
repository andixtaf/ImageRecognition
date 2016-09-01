package com.and1;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * package:  com.and1
 * Created by Andreas on 01.09.2016 for ImageRecognition.
 */
public class Persistance
{
	private static final Logger logger = LogManager.getLogger(Persistance.class);

	private String savePath;

	private String loadPath;

	public Persistance(String savePath, String loadPath)
	{
		this.savePath = savePath;
		this.loadPath = loadPath;
	}

	public void saveHistogramHSI(float[][][] histogram, String name)
	{
		String pathToSave = savePath;

		File file = new File(pathToSave + name.substring(0, name.length() - 4) + "-Gray.txt");

		persistHistogram(histogram, pathToSave + file.getName());
	}

	public void saveHistogramGray(float[] histogram, String name)
	{
		String pathToSave = savePath;

		File file = new File(pathToSave + name.substring(0, name.length() - 4) + "-Gray.txt");

		persistHistogram(histogram, pathToSave + file.getName());
	}

	private void persistHistogram(Object histogram, String pathToSave)
	{
		File fileToSave = new File(pathToSave);

		if(!fileToSave.exists() && fileToSave.isFile() && fileToSave.canWrite())
		{
			try
			{
				ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(fileToSave));
				output.writeObject(histogram);
				output.close();
			} catch(IOException e)
			{
				logger.error("save Histogram HSI : " + e);
			}
		}
	}
}
