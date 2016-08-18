package com.and1.gui.label;

import java.awt.*;

public class HistogramLabelGray extends HistogramLabel
{
	private final float[] histogramGray;

	public HistogramLabelGray(float[] hist)
	{
		histogramGray = hist;
	}

	@Override
	public void paint(Graphics g)
	{
		//Graphics2D g2d = (Graphics2D)g;
		if (histogramGray.length == 256)
		{
			float max = getMaximumGrayOfHistogram();
			double yScale;

			//Für Skalierung Höhe des Bildes / Maximalwert des Histogramms
			yScale = getHeight() / max;
			//System.out.println(getHeight());
			int sx;
			int sy;
			int ex;
			double ey;

			//Farbgradient von Schwarz nach Weiß
			for (int i = 0; i < 256; i++)
			{
				g.setColor(new Color(i, i, i));
				g.drawLine(i, getHeight() - 20, i, getHeight());
			}
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, getWidth(), getHeight() - 20);

			g.setColor(Color.BLACK);

			//Histogramm zeichnen
			for (int i = 0; i < 256; i++)
			{
				sx = i;
				sy = getHeight() - 20;
				ex = sx;
				ey = getHeight() - (histogramGray[i] * yScale) - 20;
				g.drawLine(sx, sy, ex, (int) Math.round(ey));
			}
			g.drawRect(0, 0, 256, 480);

		}
	}

	private float getMaximumGrayOfHistogram()
	{
		float max = 0;
		for (int i = 0; i < 256; i++)
		{
			max = Math.max(histogramGray[i], max);
		}
		return max;
	}
}
