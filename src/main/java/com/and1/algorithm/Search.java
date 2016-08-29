package com.and1.algorithm;

class Search
{

	private float lb_agg;

	private float ub_agg;

	private final float index;

	Search(float index, float lb_agg, float ub_agg)
	{
		this.index = index;
		this.lb_agg = lb_agg;
		this.ub_agg = ub_agg;
	}

	float getIndex()
	{
		return index;
	}

	float getLb_agg()
	{
		return lb_agg;
	}

	float getUb_agg()
	{
		return ub_agg;
	}
}
