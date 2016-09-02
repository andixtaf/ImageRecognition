package com.and1;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

/**
 * package:  com.and1
 * Created by Andreas on 01.09.2016 for ImageRecognition.
 */
public class Persistance
{
	private static final Logger logger = LogManager.getLogger(Persistance.class);

	private String pathToPersistedHistograms;

	public Persistance(String pathToPersistedHistograms)
	{
		this.pathToPersistedHistograms = pathToPersistedHistograms;
	}

	public void saveHistogramRGB(float[][][] histogram, String name)
	{
		String pathToSave = pathToPersistedHistograms;

		File file = new File(pathToSave + "/" + name.substring(0, name.length() - 4) + "-RGB.txt");

		persistHistogram(histogram, file);
	}

	private void persistHistogram(Object histogram, File fileToSave)
	{
		try
		{
			ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(fileToSave));
			output.writeObject(histogram);
			output.close();
		}
		catch (IOException e)
		{
			logger.error("unable to save Histogram: " + e);
		}
	}

	public void saveHistogramHSI(float[][][] histogram, String name)
	{
		String pathToSave = pathToPersistedHistograms;

		File file = new File(pathToSave + "/" + name.substring(0, name.length() - 4) + "-HSI.txt");

		persistHistogram(histogram, file);
	}

	public void saveHistogramGray(float[] histogram, String name)
	{
		String pathToSave = pathToPersistedHistograms;

		File file = new File(pathToSave + "/" + name.substring(0, name.length() - 4) + "-Gray.txt");

		persistHistogram(histogram, file);
	}

	public float[] getHistogramGray(String name)
	{
		String pathToSave = pathToPersistedHistograms;

		File file = new File(pathToSave + "/" + name.substring(0, name.length() - 4) + "-Gray.txt");

		return (float[]) readPersistedHistogram(file);
	}

	private Object readPersistedHistogram(File file)
	{
		Object histogramGray = null;

		try
		{
			ObjectInputStream input = new ObjectInputStream(new FileInputStream(file));
			histogramGray = (input.readObject());
			input.close();
		}
		catch (ClassNotFoundException | IOException ex)
		{
			logger.error("get histogram gray: ", ex);
		}
		return histogramGray;
	}

	public float[][][] getHistogramRGB(String name)
	{
		String pathToSave = pathToPersistedHistograms;

		File file = new File(pathToSave + "/" + name.substring(0, name.length() - 4) + "-RGB.txt");

		return (float[][][]) readPersistedHistogram(file);
	}

	public float[][][] getHistogramHSI(String name)
	{
		String pathToSave = pathToPersistedHistograms;

		File file = new File(pathToSave + "/" + name.substring(0, name.length() - 4) + "-HSI.txt");

		return (float[][][]) readPersistedHistogram(file);
	}

	public Boolean existsPersistedFile(String name, String suffix)
	{
		String pathToSave = pathToPersistedHistograms;

		File file = new File(pathToSave + "/" + name.substring(0, name.length() - 4) + suffix);

		return file.exists();
	}
}
