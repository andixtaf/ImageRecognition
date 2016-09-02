package com.and1.algorithm.sort;

public class NRA_Algorithm_Sort implements Comparable<NRA_Algorithm_Sort>
{
	private final int index;

	private final float distance;

	public NRA_Algorithm_Sort(int index, float distance)
	{
		this.index = index;
		this.distance = distance;
	}

	public int compareTo(NRA_Algorithm_Sort value)
	{
		int similar = 0;
		if (distance > value.distance)
		{
			similar = -1;
		}
		if (distance < value.distance)
		{
			similar = 1;
		}

		return similar;
	}

	@Override
	public String toString()
	{
		return "Index:  " + index + "   Lb_agg:  " + distance;
	}

	public float getDistance()
	{
		return distance;
	}

	public int getIndex()
	{
		return index;
	}
}
