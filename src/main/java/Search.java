class Search
{

	private final float index;
	private float lb_agg;
	private float ub_agg;

	public Search(float index, float lb_agg, float ub_agg)
	{
		this.index = index;
		this.lb_agg = lb_agg;
		this.ub_agg = ub_agg;
	}

	public float getIndex()
	{
		return index;
	}

	public float getLb_agg()
	{
		return lb_agg;
	}

	public float getUb_agg()
	{
		return ub_agg;
	}
}
