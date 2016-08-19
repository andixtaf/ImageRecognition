package com.and1.gui.label;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;

public class HistogramLabelGray extends HistogramLabel
{
	private static final Logger logger = LogManager.getLogger(HistogramLabelGray.class);

	private static final int GrayScaleCount = 256;

	private final float[] histogramGray;

	public HistogramLabelGray(float[] hist)
	{
		histogramGray = hist;
	}

	@Override
	public void paint(Graphics g)
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
		for (int i = 0; i < GrayScaleCount; i++)
		{
			g.setColor(new Color(i, i, i));
			g.drawLine(i, getHeight() - 20, i, getHeight());
		}
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight() - 20);

		g.setColor(Color.BLACK);

		//Histogramm zeichnen
		for (int i = 0; i < GrayScaleCount; i++)
		{
			sx = i;
			sy = getHeight() - 20;
			ex = sx;
			ey = getHeight() - (histogramGray[i] * yScale) - 20;
			g.drawLine(sx, sy, ex, (int) Math.round(ey));
		}

//		g.drawRect(0, 0, GrayScaleCount, 480);

	}

	private float getMaximumGrayOfHistogram()
	{
		float max = 0;
		for (int i = 0; i < GrayScaleCount; i++)
		{
			max = Math.max(histogramGray[i], max);
		}
		return max;
	}
}
