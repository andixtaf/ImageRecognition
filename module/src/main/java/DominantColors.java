public class DominantColors implements Comparable<DominantColors>{

	private float [] hsicolor;
	private float count;

	public DominantColors(float [] hsicolor, float count) {
		this.hsicolor = hsicolor;
		this.count = count;
	}

	public int compareTo(DominantColors value ) {
		if( count > value.count )
			return -1;
		if( count < value.count )
			return 1;

		return 0;
	}

	@Override
	public String toString() {
		return hsicolor + "   Count:  " + count;
	}

	public float getCount(){
		return count;
	}

	public float [] getHSIColor(){
		return hsicolor;
	}

}
