import javax.swing.*;
import java.awt.*;

class MRImageCellRenderer extends JLabel implements ListCellRenderer
{

	public MRImageCellRenderer()
	{
		setOpaque(true);
	}

	public Component getListCellRendererComponent(JList list, Object value,
	                                              int index, boolean isSelected, boolean cellHasFocus)
	{

		if(value instanceof MRImage) {
			MRImage img = (MRImage) value;
			this.setIcon(new ImageIcon(img.getThumbnail()));
		}

		setText(value.toString());

		Color background;
		Color foreground;

		// check if this cell represents the current DnD drop location
		JList.DropLocation dropLocation = list.getDropLocation();
		if(dropLocation != null && !dropLocation.isInsert() && dropLocation.getIndex() == index) {

			background = Color.BLUE;
			foreground = Color.WHITE;

			// check if this cell is selected
		} else if(isSelected) {
			background = new Color(33, 83, 134);
			foreground = Color.WHITE;

			// unselected, and not the DnD drop location
		} else {
			background = Color.WHITE;
			foreground = Color.BLACK;
		}

		setBackground(background);
		setForeground(foreground);

		return this;
	}
}
