//Klasse die für das sortieren der Distance Werte verantwortlich ist
public class SortL1Distance implements Comparable<SortL1Distance>{
	private float distance;
	private MRImage img;

	//Tupel in dem Img und dazugehörige Distance gespeichert wird
	public SortL1Distance(MRImage img, float distance) {
		this.img = img;
		this.distance = distance;
	}

	public int compareTo(SortL1Distance value ) {
		if( distance < value.distance )
			return -1;
		if( distance > value.distance )
			return 1;

		return 0;
	}

	@Override
	public String toString() {
		return img + "   Distance:  " + distance;
	}

	public float getDistance(){
		return distance;
	}

	public MRImage getMRImage(){
		return img;
	}
}