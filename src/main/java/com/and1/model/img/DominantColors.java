package com.and1.model.img;

import java.util.Arrays;

public class DominantColors implements Comparable<DominantColors>
{

	private final float[] colorHSI;

	private final float count;

	public DominantColors(float[] colorHSI, float count)
	{
		this.colorHSI = colorHSI;
		this.count = count;
	}

	public int compareTo(DominantColors value)
	{
		if (count > value.count)
		{
			return -1;
		}
		if (count < value.count)
		{
			return 1;
		}

		return 0;
	}

	@Override
	public String toString()
	{
		return Arrays.toString(colorHSI) + "   Count:  " + count;
	}

	public float[] getHSIColor()
	{
		return colorHSI;
	}

}
