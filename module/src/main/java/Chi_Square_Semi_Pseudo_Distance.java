import java.util.Arrays;
import java.util.Vector;

public class Chi_Square_Semi_Pseudo_Distance implements SimilarityAlgorithm {

	private float [] value;
	NRA_Algorithm_Sort [] nra_values;

	public Vector<MRImage> apply(MRImage query, Vector<MRImage> repository, int number) {

		float distance = 0;
		float scaleddistance = 0;
		float expectedvaluehist1 = 0;
		float expectedvaluehist2 = 0;
		float sum1 = 0;
		float sum2 = 0;
		float sum1sum2 = 0;
		float sumhist1 = 0;
		float sumhist2 = 0;
		int x = 0;

		String name = query.toString();
		//HSI Histogramm generieren
		query.generateHistogramHSI(name);
		//Histogramm laden
		float [][][] hist1 = query.getHistogramHSI(name);
		float [][][] hist2 = null;

		int totalhist1 = query.getHeight() * query.getWidth();

		SortIntersection [] list  = new SortIntersection[repository.size()];
		nra_values = new NRA_Algorithm_Sort[repository.size()];

		for (int i = 0; i < repository.size(); i++) {
			MRImage img = repository.get(i);
			//int totalhist2 = img.getHeight() * img.getWidth();
			String imgname = img.toString();
			img.generateHistogramHSI(imgname);
			hist2 = img.getHistogramHSI(imgname);

			for (int j = 0; j < 18; j++) {
				for (int k = 0; k < 3; k++) {
					for (int l = 0; l < 3; l++) {

						//Bestimmung der Gesamtsumme und der Einzelsummen beiden Histogramme für den erwarteten Wert
						if (x == 0) {
							for (int m = 0; m < 18; m++) {
								for (int n = 0; n < 3; n++) {
									for (int o = 0; o < 3; o++) {
										sum1sum2 += (hist1 [m][n][o] + hist2 [m][n][o]);
										sum1 += hist1 [m][n][o];
										sum2 += hist2 [m][n][o];
									}
								}
							}
							x = 1;
						}
                  
                        /*System.out.println("Sum1Sum2");
                        System.out.println(sum1sum2);
                        System.out.println("Sum1");
                        System.out.println(sum1);
                        System.out.println("Sum2");
                        System.out.println(sum2);
                        System.out.println("CurrentHistValue1");
                        System.out.println(hist1 [j][k][l]);
                        System.out.println("CurrentHistValue2");
                        System.out.println(hist2 [j][k][l]);*/

						//erwartete Werte der Histogramme bestimmen
						expectedvaluehist1 = ((hist1 [j][k][l] + hist2 [j][k][l]) * sum1) / sum1sum2;
						expectedvaluehist2 = ((hist1 [j][k][l] + hist2 [j][k][l]) * sum2) / sum1sum2;
                        
                        /*System.out.println("ExpectedValue1");
                        System.out.println(expectedvaluehist1);
                        System.out.println("ExpectedValue2");
                        System.out.println(expectedvaluehist2);*/

						if (expectedvaluehist1 == 0) {
							sumhist1 += 0;
						}
						else {
							//Bestimmung der beiden Teilsummen zur Berechnung der Distanz
							sumhist1 += ((hist1 [j][k][l] - expectedvaluehist1) * (hist1 [j][k][l] - expectedvaluehist1)) / expectedvaluehist1;
						}

						if (expectedvaluehist2 == 0) {
							sumhist2 += 0;
						}
						else {
							sumhist2 += ((hist2 [j][k][l] - expectedvaluehist2) * (hist2 [j][k][l] - expectedvaluehist2)) / expectedvaluehist2;
						}
                        
                        /*System.out.println("SumHist1");
                        System.out.println(sumhist1);
                        System.out.println("SumHist2");
                        System.out.println(sumhist2);*/

						expectedvaluehist1 = 0;
						expectedvaluehist2 = 0;
					}
				}

			}

			distance = sumhist1 + sumhist2;


			scaleddistance = scale(distance);
			//System.out.println("Similarityvalue");
			//System.out.println(similaritydistance);

			//System.out.println("Distance");
			//System.out.println(distance);

			list[i] = new SortIntersection(img,scaleddistance);
			nra_values[i] = new NRA_Algorithm_Sort(i,scaleddistance);

			distance = 0;
			sumhist1 = 0;
			sumhist2 = 0;
			sum1 = 0;
			sum2 = 0;
			sum1sum2 = 0;
			x = 0;
			scaleddistance = 0;
		}

		Arrays.sort(list);
		Arrays.sort(nra_values);
		Vector<MRImage> sortedlist = new Vector<MRImage>();
		value = new float[list.length];
		for(int i = 0; i < list.length; i++){
			float dist = list[i].getIntersection();
			value[i] = list[i].getIntersection();
			MRImage image = list[i].getMRImage();
			//neues Repository erstellt welches sortierte Elemente enthält
			sortedlist.add(image);
			image.setSimilarity(dist, image);
		}

		return sortedlist;
	}

	//Funktion zur Skalierung der übergroßen Werte der Distanzfunktion um Ähnlichkeitswerte zu erhalten
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
