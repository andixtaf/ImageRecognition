import java.util.Arrays;
import java.util.Vector;


public class HSI_Euclidean_Distance {

	private float [] value;
	NRA_Algorithm_Sort [] nra_values;

	public Vector<MRImage> applySimilarity(MRImage query, Vector<MRImage> repository, int number, int similarity) {

		float distance = 0;
		float scaleddistance = 0;
		Vector hsicol1 = new Vector();

		float [] h1 = null;
		float [] h2 = null;
		//HSI Histogramm generieren
		query.generateHSIColors();
		hsicol1 = query.getColors(number);
		Vector hsicol2 = new Vector();

		int totalhist1 = query.getHeight() * query.getWidth();


		SortL1Distance [] list  = new SortL1Distance[repository.size()];
		nra_values = new NRA_Algorithm_Sort[repository.size()];

		for (int i = 0; i < repository.size(); i++) {
			//System.out.println(i);
			MRImage img = repository.get(i);
			int totalhist2 = img.getHeight() * img.getWidth();
			img.generateHSIColors();
			hsicol2 = img.getColors(number);

			for (int j = 0; j < number; j++) {
				h1 = (float []) hsicol1.get(j);
				for (int k = 0; k < number; k++) {
					h2 = (float []) hsicol2.get(k);
                        /*if (i == 21) {
                        System.out.println("H1Point1");
                        System.out.println(h1[0]);
                        System.out.println("H1Point2");
                        System.out.println(h1[1]);
                        System.out.println("H1Point3");
                        System.out.println(h1[2]);
                        System.out.println("H2Point1");
                        System.out.println(h2[0]);
                        System.out.println("H2Point2");
                        System.out.println(h2[1]);
                        System.out.println("H2Point3");
                        System.out.println(h2[2]);
                        }*/
					distance += Math.sqrt((h1[0]  - h2[0]) * (h1[0] - h2[0]) +
							                      (h1[1]  - h2[1]) * (h1[1] - h2[1]) +
							                      (h1[2]  - h2[2]) * (h1[2] - h2[2]));
                           /* if (i == 21) {
                            System.out.println("Currentdistance");
                            System.out.println(distance);
                            }*/

				}
			}
			scaleddistance = scale(distance);

			list[i] = new SortL1Distance(img,scaleddistance);
			nra_values[i] = new NRA_Algorithm_Sort(i,scaleddistance);

			distance = 0;
			scaleddistance = 0;
		}
		Arrays.sort(list);
		Arrays.sort(nra_values);
		Vector<MRImage> sortedlist = new Vector<MRImage>();
		value = new float[list.length];
		for(int i = 0; i < list.length; i++){
			float dist = list[i].getDistance();
			value[i] = list[i].getDistance();
			MRImage image = list[i].getMRImage();
			//neues Repository erstellt welches sortierte Elemente enthält
			sortedlist.add(image);
			image.setSimilarity(dist, image);
		}

		return sortedlist;
	}

	private float scale(float dist) {
		float scaleddistance = 0;
		float s = 2000;

		scaleddistance = 1 / (1 + ( dist / s));

		return scaleddistance;
	}

	public float [] getValue() {
		return value;
	}

	public NRA_Algorithm_Sort [] getNRA_Values() {
		return nra_values;
	}

}

    