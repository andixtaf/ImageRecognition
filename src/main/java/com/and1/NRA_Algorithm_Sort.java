package com.and1;

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
		if(distance > value.distance) {
			return -1;
		}
		if(distance < value.distance) {
			return 1;
		}

		return 0;
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