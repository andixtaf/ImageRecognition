package com.and1.view;

import com.and1.img.Image;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.Vector;

/**
 * Created by Andreas on 20.08.2016 for ImageRecognition.
 */
public class SegmentationView
{

	public void createSegmentationView(JFrame mainFrame, Integer segmentationStep, Image selectedImage)
	{
		if(selectedImage != null)
		{

			double imageSize;
			String bit = Integer.toBinaryString(segmentationStep);
			int len = bit.length();
			Vector<BufferedImage> segment;
			JDialog d = new JDialog(mainFrame, selectedImage.toString());
			JLabel help = new JLabel();

			if(len % 2 == 0)
			{
				imageSize = Math.sqrt(segmentationStep * 2);
			} else
			{
				imageSize = Math.sqrt(segmentationStep);
			}
			int isize = (int) (imageSize);
			segment = selectedImage.generateRasterInGivenSteps(segmentationStep);

			int x = 0;
			int y = 0;
			int z = 0;

			Vector<JLabel> jLabelDynamic = new Vector<>();

			for(int i = 0; i < segmentationStep; i++)
			{
				jLabelDynamic.add(new JLabel("label" + i));
			}

			for(int i = 0; i < segmentationStep; i++)
			{

				jLabelDynamic.get(i).setIcon(new ImageIcon(segment.get(i)));
				if(len % 2 == 0)
				{
					jLabelDynamic.get(i).setSize(selectedImage.getWidth() / isize, selectedImage.getHeight() / (isize / 2));
				} else
				{
					jLabelDynamic.get(i).setSize(selectedImage.getWidth() / isize, selectedImage.getHeight() / isize);
				}

				d.add(jLabelDynamic.get(i));
				jLabelDynamic.get(i).setLocation(x, y);

				if(len % 2 == 0)
				{
					if(z == isize - 1)
					{
						x = 0;
						y += selectedImage.getHeight() / (isize / 2) + 10;
						z = 0;
					} else
					{
						x += selectedImage.getWidth() / isize + 10;
						z++;
					}
				} else
				{
					if(z == isize - 1)
					{
						x = 0;
						y += selectedImage.getHeight() / isize + 10;
						z = 0;
					} else
					{
						x += selectedImage.getWidth() / isize + 10;
						z++;
					}
				}
			}
			d.add(help);
			d.setSize(selectedImage.getWidth() + (isize - 1) * 10, selectedImage.getHeight() + (isize + 1) * 10);
			d.setResizable(false);
			d.setVisible(true);
		}
	}

}
