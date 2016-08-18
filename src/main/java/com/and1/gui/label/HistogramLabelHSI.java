package com.and1.gui.label;

import java.awt.*;
import java.util.ArrayList;

public class HistogramLabelHSI extends HistogramLabel
{
	private final float[][][] histogram;

	public HistogramLabelHSI(float[][][] hist)
	{
		histogram = hist;
	}

	@Override
	public void paint(Graphics g)
	{
		double yScale;

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
		for (int i = 0; i < 18; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				for (int k = 0; k < 3; k++)
				{
					g.setColor(new Color(i, j, k));
					g.drawLine(x, getHeight() - 20, x, getHeight());
					x++;
				}
			}
		}
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight() - 20);

		g.setColor(Color.BLACK);
		//Histogramm zeichnen
		sx = 0;
		for (int i = 0; i < 18; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				for (int k = 0; k < 3; k++)
				{
					g.setColor(new Color(i * 14, j * 85, k * 85));
					sy = getHeight();
					ex = sx;
					ey = getHeight() - (histogram[i][j][k] * yScale) - 20;
					g.drawLine(sx, sy, ex, (int) Math.round(ey));
					sx++;
				}
			}
		}
		g.drawRect(0, 0, 162, 480);
	}

	private float getMaxHSI()
	{
		java.util.List<Float> maximum = new ArrayList<>();
		float max = 0;

		for (int i = 0; i < 18; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				for (int k = 0; k < 3; k++)
				{
					maximum.add(histogram[i][j][k]);
				}
			}
		}

		for (Float aMaximum : maximum)
		{
			max = Math.max(aMaximum, max);
		}

		return max;
	}
}
