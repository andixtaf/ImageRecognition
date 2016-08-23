package com.and1.sort;

import com.and1.model.img.Image;

public class SortL1Distance implements Comparable<SortL1Distance>
{
	private final float distance;

	private final Image img;

	public SortL1Distance(Image img, float distance)
	{
		this.img = img;
		this.distance = distance;
	}

	public int compareTo(SortL1Distance value)
	{
		int result = 0;

		if (distance < value.distance)
		{
			result = -1;
		}
		else if (distance > value.distance)
		{
			result = 1;
		}

		return result;
	}

	@Override
	public String toString()
	{
		return img + "   Distance:  " + distance;
	}

	public float getDistance()
	{
		return distance;
	}

	public Image getMRImage()
	{
		return img;
	}
}