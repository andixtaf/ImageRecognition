package com.and1.view;

import com.and1.model.img.Image;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;

public class ActionListenerFunction
{
	private static final Logger logger = LogManager.getLogger(ActionListenerFunction.class);

	public void actionPreview(Image selectedImage, JFrame frame)
	{
		if(selectedImage != null)
		{
			JDialog d = new JDialog(frame, selectedImage.toString());
			JLabel p = new JLabel();
			p.setIcon(new ImageIcon(selectedImage.getImage()));
			d.add(p);
			d.setSize(selectedImage.getWidth(), selectedImage.getHeight());
			d.setResizable(false);
			d.setVisible(true);

		} else
		{
			JOptionPane.showMessageDialog(frame, "no selected image");
			logger.info("no selected image!");
		}
	}

	public void actionSegmentation(JFrame mainFrame, Image selectedImage)
	{
		Integer segmentationStep = chooseSegmentationStepWindow();

		SegmentationView segmentationView = new SegmentationView();

		if(Integer.bitCount(segmentationStep) == 1)
		{
			segmentationView.createSegmentationView(mainFrame, segmentationStep, selectedImage);
		} else
		{
			JOptionPane.showMessageDialog(null,
			                              "Segmentationstep must be a value of power of 2 ",
			                              "Error",
			                              JOptionPane.ERROR_MESSAGE);
		}
	}

	private int chooseSegmentationStepWindow()
	{
		JFrame frame = new JFrame("Input Dialog");

		String segmentation = JOptionPane.showInputDialog(frame, "Geben Sie die Segmentation ein (vielfaches von 2!");

		return Integer.parseInt(segmentation);
	}

	public void actionExit()
	{
		logger.info("system exit...");
		System.exit(0);
	}
}
