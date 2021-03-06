package com.and1.algorithm.Intersection;

import com.and1.model.img.Image;

public class SortIntersection implements Comparable<SortIntersection>
{
	private final float intersection;

	private final Image img;

	public SortIntersection(Image img, float intersection)
	{
		this.img = img;
		this.intersection = intersection;
	}

	public int compareTo(SortIntersection value)
	{
		int similar = 0;
		if (intersection > value.intersection)
		{
			similar = -1;
		}
		if (intersection < value.intersection)
		{
			similar = 1;
		}

		return similar;
	}

	@Override
	public String toString()
	{
		return img + "  IntersectionRGB:  " + intersection;
	}

	public float getIntersection()
	{
		return intersection;
	}

	public Image getMRImage()
	{
		return img;
	}
}
