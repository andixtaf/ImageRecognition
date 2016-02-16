//Klasse die f�r das sortieren der Distance Werte verantwortlich ist
class SortL1Distance implements Comparable<SortL1Distance>
{
	private final float distance;
	private final MRImage img;

	//Tupel in dem Img und dazugeh�rige Distance gespeichert wird
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