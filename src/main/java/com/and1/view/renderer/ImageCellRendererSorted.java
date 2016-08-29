package com.and1.view.renderer;

import com.and1.model.img.Image;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;

public class ImageCellRendererSorted extends JLabel implements ListCellRenderer
{
	private static final Logger logger = LogManager.getLogger(ImageCellRendererSorted.class);

	public ImageCellRendererSorted()
	{
		setOpaque(true);
	}

	public Component getListCellRendererComponent(JList list, Object value,
												  int index, boolean isSelected, boolean cellHasFocus)
	{

		float similarity = 1.0f;

		if (value instanceof Image)
		{
			Image img = (Image) value;
			this.setIcon(new ImageIcon(img.getThumbnail()));
			similarity = img.getSimilarity();
		}

		setText(value.toString() + " Similarity: " + similarity);

		Color background;
		Color foreground;

		// check if this cell represents the current DnD drop location
		JList.DropLocation dropLocation = list.getDropLocation();
		if (dropLocation != null && !dropLocation.isInsert() && dropLocation.getIndex() == index)
		{

			background = Color.BLUE;
			foreground = Color.WHITE;

			// check if this cell is selected
		}
		else if (isSelected)
		{
			background = new Color(33, 83, 134);
			foreground = Color.WHITE;

			// unselected, and not the DnD drop location
		}
		else
		{
			Integer colorNumber = Math.round(similarity) % 255;

			background = new Color(colorNumber, colorNumber, colorNumber);
			foreground = new Color(33, 83, 134);
		}

		setBackground(background);
		setForeground(foreground);

		return this;
	}
}
