package com.and1.view;

import com.and1.model.img.Image;
import com.and1.view.label.HistogramLabel;
import com.and1.view.label.HistogramLabelGray;
import com.and1.view.label.HistogramLabelHSI;
import com.and1.view.label.HistogramLabelRGB;

import javax.swing.*;
import java.awt.image.BufferedImage;

/**
 * package:  com.and1.view
 * Created by Andreas on 30.08.2016 for ImageRecognition.
 */
public class HistogramView
{
	public static void displayHistogramGRAY(MainFrame mainFrame, Image image)
	{
		JDialog d = new JDialog(mainFrame, "Histogram: " + image.toString());

		if(image.getImage().getType() == BufferedImage.TYPE_BYTE_GRAY)
		{
			HistogramLabel h =
					new HistogramLabelGray(image.getHistogramGray("/" + image.toString()));
			d.add(h);
			d.setSize(256, 480);
			d.setResizable(false);
			d.setVisible(true);

		} else
		{
			JOptionPane.showMessageDialog(mainFrame, "no RGB image ", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void displayHistogramRGB(MainFrame mainFrame, Image image)
	{
		JDialog d = new JDialog(mainFrame, "Histogram: " + image.toString());

		if(image.getImage().getType() == BufferedImage.TYPE_3BYTE_BGR)
		{
			HistogramLabel h =
					new HistogramLabelRGB(image.getHistogramRGB("/" + image.toString()));
			d.add(h);
			d.setSize(256, 480);
			d.setResizable(false);
			d.setVisible(true);

		} else
		{
			JOptionPane.showMessageDialog(mainFrame, "no GrayScale image ", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void displayHistogramHSI(MainFrame mainFrame, Image image)
	{
		JDialog d = new JDialog(mainFrame, "Histogram: " + image.toString());

		if(image.getImage().getType() == BufferedImage.TYPE_3BYTE_BGR)
		{
			HistogramLabel h = new HistogramLabelHSI(image.getHistogramRGB("/" + image.toString()));
			d.add(h);
			d.setSize(256, 480);
			d.setResizable(false);
			d.setVisible(true);

		} else
		{
			JOptionPane.showMessageDialog(mainFrame, "no GrayScale image ", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
