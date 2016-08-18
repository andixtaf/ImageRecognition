package com.and1.gui.Renderer;

import com.and1.img.MRImage;

import javax.swing.*;
import java.awt.*;

public class MRImageCellRendererSorted extends JLabel implements ListCellRenderer
{

	public MRImageCellRendererSorted()
	{
		setOpaque(true);
	}

	public Component getListCellRendererComponent(JList list, Object value,
												  int index, boolean isSelected, boolean cellHasFocus)
	{

		float similarity = 1.0f;

		if (value instanceof MRImage)
		{
			MRImage img = (MRImage) value;
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
			background = new Color(similarity, similarity, similarity);
			foreground = new Color(33, 83, 134);
		}

		setBackground(background);
		setForeground(foreground);

		return this;
	}
}
