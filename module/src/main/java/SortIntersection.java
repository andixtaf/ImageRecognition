//Klasse die für das sortieren der Intersection Werte verantwortlich ist
class SortIntersection implements Comparable<SortIntersection>
{
	private final float intersection;
	private final MRImage img;

	//Tupel in dem Img und dazugehörige Intersection gespeichert wird
	public SortIntersection(MRImage img, float intersection)
	{
		this.img = img;
		this.intersection = intersection;
	}

	public int compareTo(SortIntersection value)
	{
		if(intersection > value.intersection) {
			return -1;
		}
		if(intersection < value.intersection) {
			return 1;
		}

		return 0;
	}

	@Override
	public String toString()
	{
		return img + "   Intersection:  " + intersection;
	}

	public float getIntersection()
	{
		return intersection;
	}

	public MRImage getMRImage()
	{
		return img;
	}
}
