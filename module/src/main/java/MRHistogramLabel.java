import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class MRHistogramLabel extends JLabel {

	private float[] [] [] histogram;
	private float [] histogramGray;

	//Konstruktor RGB / HSI Histogramm
	public MRHistogramLabel(float[][][] hist) {
		histogram = hist;
	}

	//Konstruktor Graystufen Histogramm
	public MRHistogramLabel(float [] hist) {
		histogramGray = hist;
	}

	//Maximum des Graustufen Histogramms bestimmen
	public float getMaximumGray() {
		float max = 0;
		for (int i = 0; i < 256; i++) {
			max = Math.max(histogramGray[i], max);
		}
		return max;
	}

	//Maximum des RGB / HSI Histogramms bestimmen
	public double[] getMaximum() {
		double[] max = new double[histogram.length];

		//RGB
		if(histogram.length == 8) {
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					for (int k = 0; k < 8; k++) {
						if (histogram[i][j][k] > max[i]) {
							max[i] = histogram[i][j][k];
						}
					}
				}
			}
		}
		//HSI
    /*else {

       for (int i = 0; i < 18; i++) {
          for (int j = 0; j < 3; j++) {
            for (int k = 0; k < 3; k++) {
              if (histogram[i][j][k] > max[i]) {
                max[i] = histogram[i][j][k];
              }
            }
          }
        }
    } */

		return max;
	}

	public float getMaxHSI() {
		//System.out.println(histogram.length);
		Vector<Float> maximum = new Vector<Float>();
		float max = 0;

		for (int i = 0; i < 18; i++) {
			for (int j = 0; j < 3; j++) {
				for (int k = 0; k < 3; k++) {
					//System.out.println(histogram[i][j][k]);
					maximum.add(histogram[i][j][k]);
				}
			}
		}

		for (int i = 0; i < maximum.size(); i++) {
			max = Math.max(maximum.get(i), max);
		}

		return max;
	}

	@Override
	public void paint(Graphics g) {
		//Graustufen Histogramm
		if (histogramGray != null) {
			//Graphics2D g2d = (Graphics2D)g;
			if (histogramGray.length == 256) {
				float max = getMaximumGray();
				double yScale = 0;

				//Für Skalierung Höhe des Bildes / Maximalwert des Histogramms
				yScale = getHeight() / max;
				//System.out.println(getHeight());
				int sx;
				int sy;
				int ex;
				double ey;

				//Farbgradient von Schwarz nach Weiß
				for (int i = 0; i < 256; i++) {
					g.setColor(new Color(i,i,i));
					g.drawLine(i,getHeight()-20,i,getHeight());
				}
				g.setColor(Color.WHITE);
				g.fillRect(0, 0, getWidth(), getHeight()-20);

				g.setColor(Color.BLACK);

				//Histogramm zeichnen
				for (int i = 0; i < 256; i++) {
					sx = i;
					sy = getHeight()-20;
					ex = sx;
					ey = getHeight() - (histogramGray[i] * yScale)-20;
					g.drawLine(sx, sy,ex,(int) Math.round(ey));
				}
				g.drawRect(0,0 ,256, 480);

			}
		}
		//RGB Histogramm
		else if ( histogram != null) {
			if (histogram.length == 8) {
				double[] maximum = getMaximum();
				double yScale = 0;

				//Für Skalierung Höhe des Bildes / Maximalwert des Histogramms
				yScale = getHeight() / (double) Math.max(maximum[0],
				                                         Math.max(maximum[1], Math.max(maximum[2], Math.max(maximum[3], Math.max(maximum[4], Math.max(maximum[5], Math.max(maximum[6], maximum[7])))))));

				//x - Achse des Histogramms
				int sx = 0;
				//x - Achse des Farbgradients
				int x = 0;
				int sy = 0;
				int ex;
				double ey;

				//Farbgradient zeichnen
				for (int i = 0; i < 8; i++) {
					for(int j = 0; j <8; j++) {
						for(int k = 0; k <8; k++){
							g.setColor(new Color(i,j,k));
							g.drawLine(x,getHeight()-20,x,getHeight());
							x++;
						}
					}
				}
				g.setColor(Color.WHITE);
				g.fillRect(0, 0, getWidth(), getHeight()-20);

				g.setColor(Color.BLACK);
				//Histogramm zeichnen
				sx = 0;
				for (int i = 0; i < 8; i++) {
					for (int j = 0; j < 8; j++) {
						for (int k = 0; k < 8; k++) {
							g.setColor(new Color (i * 32, j * 32, k * 32));
							sy = getHeight();
							ex = sx;
							ey = getHeight() - (histogram[i][j][k] * yScale)-20;
							g.drawLine(sx, sy,ex,(int) Math.round(ey));
							sx++;
						}
					}
				}
				g.drawRect(0,0 ,512, 480);
			}
			//HSI
			else {

				double yScale = 0;

				float max = getMaxHSI();

				yScale = getHeight() / max;
				//System.out.println(yScale);
				//x - Achse des Farbgradients
				int x = 0;
				//x - Achse des Histogramms
				int sx;
				int sy;
				int ex;
				double ey;

				//Farbgradient zeichnen
				for (int i = 0; i < 18; i++) {
					for(int j = 0; j <3; j++) {
						for(int k = 0; k <3; k++){
							g.setColor(new Color(i,j,k));
							g.drawLine(x,getHeight()-20,x,getHeight());
							x++;
						}
					}
				}
				g.setColor(Color.WHITE);
				g.fillRect(0, 0, getWidth(), getHeight()-20);

				g.setColor(Color.BLACK);
				//Histogramm zeichnen
				sx = 0;
				for (int i = 0; i < 18; i++) {
					for (int j = 0; j < 3; j++) {
						for (int k = 0; k < 3; k++) {
							g.setColor(new Color (i * 14, j * 85, k * 85));
							sy = getHeight();
							ex = sx;
							ey = getHeight() - (histogram[i][j][k] * yScale)-20;
							g.drawLine(sx, sy,ex,(int) Math.round(ey));
							sx++;
						}
					}
				}
				g.drawRect(0,0 ,162, 480);
			}
		}
	}
}     
