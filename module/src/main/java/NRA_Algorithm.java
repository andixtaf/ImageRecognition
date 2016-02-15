import java.util.Arrays;
import java.util.Vector;

public class NRA_Algorithm {

	private float distance = 1.0f;
	private int index = 0;


	public NRA_Algorithm_Sort [] calculate(NRA_Algorithm_Sort [] euclidean, NRA_Algorithm_Sort [] chisquare, int k) {

		NRA_Algorithm_Sort [] eucl = euclidean;
		NRA_Algorithm_Sort [] chi = chisquare;
		NRA_Algorithm_Sort [] nra = new NRA_Algorithm_Sort [euclidean.length];
		Vector <Float> ub_agg_list = new Vector <Float>();
		//keine Ahnung warum wir die Klasse Search genannt haben
		//im Nachhinein betrachtet, wäre Write eigentlich wesentlich
		//passender gewesen
		Vector <Search> read = new Vector <Search>();
		Vector <Integer> remindexl = new Vector <Integer>();
		Vector <Integer> remindexr = new Vector <Integer>();

		float tau = 0;
		float ol = 0;
		float or = 0;
		float oleft = 0;
		float oright = 0;
		float checkl = 0;
		float checkr = 0;
		int x = 0;
		float oleft2 = 0;
		float oright2 = 0;
		int condition1 = 0;
		int condition2 = 0;
		boolean check = false;

		for (int i = 0; i < eucl.length; i++) {
			//System.out.println(i);
			//System.out.println("remindexl");
			//System.out.println(remindexl.size());
			//System.out.println("remindexr");
			//System.out.println(remindexr.size());
			//System.out.println("Distance eucl");
			ol = eucl[i].getDistance();
			//System.out.println(ol);
			//System.out.println("Distance chi");
			or = chi[i].getDistance();
			//System.out.println(or);


			if (read.size() > 1) {
				//prüfen ob momentaner Index der beiden Listen sich in der Liste read befindet
				checkl = search(read, eucl[i].getIndex());
				checkr = search(read, chi[i].getIndex());
			}

			//Das passiert nur bei ersten lesen und einfügen der Indexwerte
			//von beiden Listen, da die read - Liste noch leer ist
			else {
				//System.out.println("read > 2");
				//System.out.println("Index eucl");
				oleft = eucl[i].getIndex();
				read.add(new Search(eucl[i].getIndex(), ol / 2, (ol + or)/2));
				remindexl.add((int)eucl[i].getIndex());
				//System.out.println(oleft);
				//System.out.println("Index chi");
				oright = chi[i].getIndex();
				read.add(new Search(chi[i].getIndex(), or / 2, (ol + or)/2));
				remindexr.add((int)chi[i].getIndex());
				//System.out.println(oright);
			}

			//prüfen der linken Liste ob Index noch nicht vorhanden
			if (checkl == -1.0f) {
				oleft = eucl[i].getIndex();
				//System.out.println("Neu left");
				//System.out.println("Index eucl");
				//neues Element in Liste einfügen
				read.add(new Search(eucl[i].getIndex(), ol / 2, (ol + or)/2));
				//System.out.println(oleft);
				//Index ebenfalls in seperate Liste einfügen
				//dient zur späteren Aktualisierung der ub_agg - Werte
				remindexl.add((int)eucl[i].getIndex());
			}

			//falls Index bereits vorhanden ist
			else if (checkl == 1.0f) {
				//System.out.println("Index schon vorhanden");
				oleft = eucl[i].getIndex();
				oleft2 = 1;
			}

			//prüfen der rechten Liste ob Index noch nicht vorhanden
			if (checkr == -1.0f) {
				oright = chi[i].getIndex();
				//System.out.println("Neu right");
				//System.out.println("Index chi");
				read.add(new Search(chi[i].getIndex(), or / 2, (ol + or)/2));
				//System.out.println(oright);
				//Index ebenfalls in seperate Liste einfügen
				//dient zur späteren Aktualisierung der ub_agg - Werte
				remindexr.add((int)chi[i].getIndex());
			}

			//falls Index bereits vorhanden ist
			else if (checkr == 1.0f) {
				//System.out.println("Index schon vorhanden");
				oright = chi[i].getIndex();
				oright2 = 1;
			}


			if (remindexl.size() > 1) {
				//wenn Index bereits in beiden Listen aufgetaucht ist
				if (oleft2 == 1) {
					//System.out.println("gleicher Index oleft");
					//System.out.println("Indexsuchen");
					//bestimmen an welcher Position sich der Index Wert in der
					//read Liste befindet
					int newindex = getIndex(read, oleft);
					//System.out.println(newindex);
					//lb_agg und ub_agg bestimmen
					float lb_agg = read.get(newindex).getLb_agg() + (ol / 2);
					float ub_agg = read.get(newindex).getUb_agg();
					//neue Werte an Postion des Index Wertes in der read Liste eintragen
					read.set(newindex, new Search(oleft, lb_agg, ub_agg));
					//Index Werte aus Liste entfernen da er nicht mehr gebraucht wird
					int removeindexr = removeIndex(remindexr, oleft);
					remindexr.remove(removeindexr);
					//Index + lb_agg in nra eintragen (steht hier für die top-k Liste)
					nra [x] = new NRA_Algorithm_Sort((int)oleft, lb_agg);
					//System.out.println("Current nra-size");
					//System.out.println(nra.length);
					//x erhöhen um nächste Position in der Liste zu setzen
					x++;
					check = true;
					oleft = 0;
					oleft2 = 0;
				}

				else {
					//System.out.println("Werte anpassen left");
					//Falls Index noch nicht in beiden Listen gefunden
					//den Aktualisierungspart der ub_agg Werte fand ich
					//recht knifflig
					for (int j = 0; j < remindexl.size(); j++) {
						int newindexl = getIndex(read, remindexl.get(j));
						float index = remindexl.get(j);
						float lb_agg = read.get(newindexl).getLb_agg();
						//ub_agg Wert des Index mit dem Wert des aktuellen Index addieren
						float ub_agg = read.get(newindexl).getLb_agg() + (or / 2);
						//und neue Werte setzen
						read.set(newindexl, new Search(index, lb_agg, ub_agg));
					}
				}
			}

			//analog zur linken Liste
			if (remindexr.size() > 1) {
				if (oright2 == 1) {
					//System.out.println("gleicher Index oright");
					//System.out.println("Indexsuchen");
					int newindex = getIndex(read, oright);
					//System.out.println(newindex);
					float lb_agg = read.get(newindex).getLb_agg() + (or / 2);
					float ub_agg = read.get(newindex).getUb_agg();
					read.set(newindex, new Search(oright, lb_agg, ub_agg));
					int removeindexl = removeIndex(remindexl, oright);
					remindexl.remove(removeindexl);
					nra [x] = new NRA_Algorithm_Sort((int)oright, lb_agg);
					//System.out.println("Current nra-size");
					//System.out.println(nra.length);
					check = true;
					x++;
					oright = 0;
					oright2 = 0;
				}

				else {
					//System.out.println("Werte anpassen right");
					for (int l = 0; l < remindexr.size(); l++) {
						int newindexr = getIndex(read, remindexr.get(l));
						float index = remindexr.get(l);
						float lb_agg = read.get(newindexr).getLb_agg();
						float ub_agg = read.get(newindexr).getLb_agg() + (ol / 2);
						read.set(newindexr, new Search(index, lb_agg, ub_agg));
					}
				}
			}

			tau = (ol + or) / 2;
			//System.out.println(nra.length);

			//Abfragen der ersten Bedingung ob lb_agg Werte von top-k >= tau sind
			//wenn condition1 = k dann ist 1. Abbruchbedingung erfüllt
			if (check == true) {
				for (int j = 0; j < x; j++) {
					if (condition1 < k) {
						//System.out.println("lb_agg");
						//System.out.println(nra[j].getDistance());
						if (nra[j].getDistance() >= tau ) {
							condition1++;
						}
					}

				}

				//alle ub_agg der Indices die in read aber nicht in top-k sind ermitteln
				//und in neue Liste speichern
				for (int j = 0; j < read.size(); j++) {
					for (int l = 0; l < x; l++) {
						if (read.get(j).getIndex() != nra[l].getIndex() ) {
							ub_agg_list.add(read.get(j).getUb_agg());
						}
					}
				}

				//Abfragen der 2. Bedingung ob ub_agg Werte von top-k größer gleich
				//der ub_agg Werte der im vorigen Schritt gespeicherten Liste sind
				//wenn condition2 = k dann ist 2. Abbruchbedingung erfüllt
				for (int j = 0; j < ub_agg_list.size(); j++) {
					for (int l = 0; l < x; l++) {
						if (condition2 < k) {
							if (nra[l].getDistance() >= ub_agg_list.get(j)) {
								condition2++;
							}
						}
					}
				}
				check = false;
			}

			//Falls beide Abbruchbedingungen erfüllt sind wird Schleife verlassen
			if (condition1 == k && condition2 == k) {
				//System.out.println("Abbruch");
				//System.out.println(x);
				break;
			}

			//andernfalls condition1 und condition2 auf 0 zurücksetzen und die Liste
			//der gespeicherten ub_agg Werte löschen
			else {
				condition2 = 0;
				condition1 = 0;
				ub_agg_list.removeAllElements();
			}
		}
		//aus irgendeinen Grund wollte der Compiler mit dem urprünglichen Array
		//nra nicht arbeiten also hab ich den Inahlt von nra in ein zweites
		//Array gespeichert
		NRA_Algorithm_Sort [] nrasort = new NRA_Algorithm_Sort [x];
		for(int j = 0; j < x; j++) {
			nrasort[j] = nra[j];
		}
		//Array absteigend sortieren
		Arrays.sort(nrasort);
		for (int i= 0; i < nrasort.length; i++) {
			System.out.println((int)nrasort[i].getIndex());
			System.out.println(nrasort[i].getDistance());
			//setValues((int)nrasort[i].getIndex(), nrasort[i].getDistance());
		}
		return nrasort;
	}

	public float search(Vector <Search> read, float value) {

		for (int i = 0; i < read.size(); i++) {
			if (read.get(i).getIndex() == value) {
				return 1.0f;
			}
		}
		return -1.0f;
	}

	public int getIndex(Vector <Search> read, float search) {

		for (int i = 0; i < read.size(); i++) {
			if (read.get(i).getIndex() == search) {
				return (int)i;
			}
		}
		return 0;
	}

	public int removeIndex(Vector <Integer> list, float index) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) == (int)index) {
				return (int)i;
			}
		}
		return 0;
	}

	//die nächsten Funktionen sind eigentlich unnötig
	//das Einfügen der einzelnen Werte sollte wie bei MRImage auch
	//über einen CellRenderer funktionen, aber das hat einfach nicht
	//funktioniert
	//vielleicht hast du ja einen Tipp wie man das doch noch funktionsfähig
	//machen kann
	//die zugehörige CellRenderer Klasse heißt NRACellRenderer
	public float getDistance() {
		return distance;
	}

	public int getIndex() {
		return index;
	}

	public void displayNRAValues(NRA_Algorithm_Sort [] nra) {
		for (int i = 0; i < nra.length; i++) {
			setValues((int)nra[i].getIndex(), nra[i].getDistance());
		}

	}
	public void setValues(int index, float distance) {
		this.distance = distance;
		this.index = index;
	}
}
