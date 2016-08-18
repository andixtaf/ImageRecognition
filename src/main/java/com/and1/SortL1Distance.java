package com.and1;

import com.and1.img.MRImage;

public class SortL1Distance implements Comparable<SortL1Distance>
{
	private final float distance;
	private final MRImage img;

	public SortL1Distance(MRImage img, float distance)
	{
		this.img = img;
		this.distance = distance;
	}

	public int compareTo(SortL1Distance value)
	{
		int result = 0;

		if(distance < value.distance) {
			result = -1;
		} else if(distance > value.distance) {
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

	public MRImage getMRImage()
	{
		return img;
	}
}