package com.and1.gui.label;

import java.awt.*;

public class HistogramLabelRGB extends HistogramLabel
{
	private final float[][][] histogram;

	//Konstruktor RGB / HSI Histogramm
	public HistogramLabelRGB(float[][][] hist)
	{
		histogram = hist;
	}

	@Override
	public void paint(Graphics g)
	{
		if (histogram.length == 8)
		{
			double[] maximum = getMaximum();
			double yScale;

			//Für Skalierung Höhe des Bildes / Maximalwert des Histogramms
			yScale = getHeight() / Math.max(maximum[0],
											Math.max(maximum[1], Math.max(maximum[2], Math.max(maximum[3],
																							   Math.max(maximum[4], Math.max(maximum[5], Math.max(maximum[6], maximum[7])))))));

			int xAxeOfHistogram;
			int xAxeOfColourGradient = 0;
			int sy;
			int ex;
			double ey;

			//Farbgradient zeichnen
			for (int i = 0; i < 8; i++)
			{
				for (int j = 0; j < 8; j++)
				{
					for (int k = 0; k < 8; k++)
					{
						g.setColor(new Color(i, j, k));
						g.drawLine(xAxeOfColourGradient, getHeight() - 20, xAxeOfColourGradient, getHeight());
						xAxeOfColourGradient++;
					}
				}
			}
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, getWidth(), getHeight() - 20);

			g.setColor(Color.BLACK);
			//Histogramm zeichnen
			xAxeOfHistogram = 0;
			for (int i = 0; i < 8; i++)
			{
				for (int j = 0; j < 8; j++)
				{
					for (int k = 0; k < 8; k++)
					{
						g.setColor(new Color(i * 32, j * 32, k * 32));
						sy = getHeight();
						ex = xAxeOfHistogram;
						ey = getHeight() - (histogram[i][j][k] * yScale) - 20;
						g.drawLine(xAxeOfHistogram, sy, ex, (int) Math.round(ey));
						xAxeOfHistogram++;
					}
				}
			}
			g.drawRect(0, 0, 512, 480);

		}
	}

	private double[] getMaximum()
	{
		double[] max = new double[histogram.length];

		if (histogram.length == 8)
		{
			for (int i = 0; i < 8; i++)
			{
				for (int j = 0; j < 8; j++)
				{
					for (int k = 0; k < 8; k++)
					{
						if (histogram[i][j][k] > max[i])
						{
							max[i] = histogram[i][j][k];
						}
					}
				}
			}
		}

		return max;
	}
}

