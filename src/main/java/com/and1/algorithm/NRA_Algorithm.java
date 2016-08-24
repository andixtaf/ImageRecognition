package com.and1.algorithm;

import com.and1.algorithm.sort.NRA_Algorithm_Sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NRA_Algorithm
{
	public NRA_Algorithm_Sort[] calculate(NRA_Algorithm_Sort[] euclidean, NRA_Algorithm_Sort[] chisquare, int k)
	{

		NRA_Algorithm_Sort[] nra = new NRA_Algorithm_Sort[euclidean.length];
		List<Float> ub_agg_list = new ArrayList<>();

		List<Search> read = new ArrayList<>();
		List<Integer> remindexl = new ArrayList<>();
		List<Integer> remindexr = new ArrayList<>();

		float tau;
		float ol;
		float or;
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

		for (int i = 0; i < euclidean.length; i++)
		{
			ol = euclidean[i].getDistance();
			or = chisquare[i].getDistance();

			if (read.size() > 1)
			{
				//prüfen ob momentaner Index der beiden Listen sich in der Liste read befindet
				checkl = search(read, euclidean[i].getIndex());
				checkr = search(read, chisquare[i].getIndex());
			}

			//Das passiert nur bei ersten lesen und einfügen der Indexwerte
			//von beiden Listen, da die read - Liste noch leer ist
			else
			{
				oleft = euclidean[i].getIndex();
				read.add(new Search(euclidean[i].getIndex(), ol / 2, (ol + or) / 2));
				remindexl.add(euclidean[i].getIndex());

				oright = chisquare[i].getIndex();
				read.add(new Search(chisquare[i].getIndex(), or / 2, (ol + or) / 2));
				remindexr.add(chisquare[i].getIndex());
			}

			//prüfen der linken Liste ob Index noch nicht vorhanden
			if (checkl == -1.0f)
			{
				oleft = euclidean[i].getIndex();

				read.add(new Search(euclidean[i].getIndex(), ol / 2, (ol + or) / 2));
				//System.out.println(oleft);
				//Index ebenfalls in seperate Liste einf�gen
				//dient zur sp�teren Aktualisierung der ub_agg - Werte
				remindexl.add(euclidean[i].getIndex());
			}

			//falls Index bereits vorhanden ist
			else if (checkl == 1.0f)
			{
				//System.out.println("Index schon vorhanden");
				oleft = euclidean[i].getIndex();
				oleft2 = 1;
			}

			//pr�fen der rechten Liste ob Index noch nicht vorhanden
			if (checkr == -1.0f)
			{
				oright = chisquare[i].getIndex();
				//System.out.println("Neu right");
				//System.out.println("Index chi");
				read.add(new Search(chisquare[i].getIndex(), or / 2, (ol + or) / 2));
				//System.out.println(oright);
				//Index ebenfalls in seperate Liste einf�gen
				//dient zur sp�teren Aktualisierung der ub_agg - Werte
				remindexr.add(chisquare[i].getIndex());
			}

			//falls Index bereits vorhanden ist
			else if (checkr == 1.0f)
			{
				//System.out.println("Index schon vorhanden");
				oright = chisquare[i].getIndex();
				oright2 = 1;
			}

			if (remindexl.size() > 1)
			{
				//wenn Index bereits in beiden Listen aufgetaucht ist
				if (oleft2 == 1)
				{
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
					//Index + lb_agg in nra eintragen (steht hier f�r die top-k Liste)
					nra[x] = new NRA_Algorithm_Sort((int) oleft, lb_agg);
					//System.out.println("Current nra-size");
					//System.out.println(nra.length);
					//x erh�hen um n�chste Position in der Liste zu setzen
					x++;
					check = true;
					oleft = 0;
					oleft2 = 0;
				}
				else
				{
					//System.out.println("Werte anpassen left");
					//Falls Index noch nicht in beiden Listen gefunden
					//den Aktualisierungspart der ub_agg Werte fand ich
					//recht knifflig
					for (Integer aRemindexl : remindexl)
					{
						int newindexl = getIndex(read, aRemindexl);
						float index = aRemindexl;
						float lb_agg = read.get(newindexl).getLb_agg();
						//ub_agg Wert des Index mit dem Wert des aktuellen Index addieren
						float ub_agg = read.get(newindexl).getLb_agg() + (or / 2);
						//und neue Werte setzen
						read.set(newindexl, new Search(index, lb_agg, ub_agg));
					}
				}
			}

			//analog zur linken Liste
			if (remindexr.size() > 1)
			{
				if (oright2 == 1)
				{
					//System.out.println("gleicher Index oright");
					//System.out.println("Indexsuchen");
					int newindex = getIndex(read, oright);
					//System.out.println(newindex);
					float lb_agg = read.get(newindex).getLb_agg() + (or / 2);
					float ub_agg = read.get(newindex).getUb_agg();
					read.set(newindex, new Search(oright, lb_agg, ub_agg));
					int removeindexl = removeIndex(remindexl, oright);
					remindexl.remove(removeindexl);
					nra[x] = new NRA_Algorithm_Sort((int) oright, lb_agg);
					//System.out.println("Current nra-size");
					//System.out.println(nra.length);
					check = true;
					x++;
					oright = 0;
					oright2 = 0;
				}
				else
				{
					//System.out.println("Werte anpassen right");
					for (Integer aRemindexr : remindexr)
					{
						int newindexr = getIndex(read, aRemindexr);
						float index = aRemindexr;
						float lb_agg = read.get(newindexr).getLb_agg();
						float ub_agg = read.get(newindexr).getLb_agg() + (ol / 2);
						read.set(newindexr, new Search(index, lb_agg, ub_agg));
					}
				}
			}

			tau = (ol + or) / 2;
			//System.out.println(nra.length);

			//Abfragen der ersten Bedingung ob lb_agg Werte von top-k >= tau sind
			//wenn condition1 = k dann ist 1. Abbruchbedingung erf�llt
			if (check)
			{
				for (int j = 0; j < x; j++)
				{
					if (condition1 < k)
					{
						//System.out.println("lb_agg");
						//System.out.println(nra[j].getDistance());
						if (nra[j].getDistance() >= tau)
						{
							condition1++;
						}
					}

				}

				//alle ub_agg der Indices die in read aber nicht in top-k sind ermitteln
				//und in neue Liste speichern
				for (Search aRead : read)
				{
					for (int l = 0; l < x; l++)
					{
						if (aRead.getIndex() != nra[l].getIndex())
						{
							ub_agg_list.add(aRead.getUb_agg());
						}
					}
				}

				//Abfragen der 2. Bedingung ob ub_agg Werte von top-k gr��er gleich
				//der ub_agg Werte der im vorigen Schritt gespeicherten Liste sind
				//wenn condition2 = k dann ist 2. Abbruchbedingung erf�llt
				for (Float anUb_agg_list : ub_agg_list)
				{
					for (int l = 0; l < x; l++)
					{
						if (condition2 < k)
						{
							if (nra[l].getDistance() >= anUb_agg_list)
							{
								condition2++;
							}
						}
					}
				}
				check = false;
			}

			//Falls beide Abbruchbedingungen erf�llt sind wird Schleife verlassen
			if (condition1 == k && condition2 == k)
			{
				//System.out.println("Abbruch");
				//System.out.println(x);
				break;
			}

			//andernfalls condition1 und condition2 auf 0 zur�cksetzen und die Liste
			//der gespeicherten ub_agg Werte l�schen
			else
			{
				condition2 = 0;
				condition1 = 0;
				ub_agg_list.clear();
			}
		}
		//aus irgendeinen Grund wollte der Compiler mit dem urpr�nglichen Array
		//nra nicht arbeiten also hab ich den Inahlt von nra in ein zweites
		//Array gespeichert
		NRA_Algorithm_Sort[] nrasort = new NRA_Algorithm_Sort[x];
		System.arraycopy(nra, 0, nrasort, 0, x);
		//Array absteigend sortieren
		Arrays.sort(nrasort);
		for (NRA_Algorithm_Sort aNrasort : nrasort)
		{
			System.out.println(aNrasort.getIndex());
			System.out.println(aNrasort.getDistance());
			//setValues((int)nrasort[i].getIndex(), nrasort[i].getDistance());
		}
		return nrasort;
	}

	private float search(List<Search> read, float value)
	{

		for (Search aRead : read)
		{
			if (aRead.getIndex() == value)
			{
				return 1.0f;
			}
		}
		return -1.0f;
	}

	private int getIndex(List<Search> read, float search)
	{

		for (int i = 0; i < read.size(); i++)
		{
			if (read.get(i).getIndex() == search)
			{
				return i;
			}
		}
		return 0;
	}

	private int removeIndex(List<Integer> list, float index)
	{
		for (int i = 0; i < list.size(); i++)
		{
			if (list.get(i) == (int) index)
			{
				return i;
			}
		}
		return 0;
	}

}
