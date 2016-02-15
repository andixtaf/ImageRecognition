import javax.swing.*;
import java.awt.*;

public class NRACellRenderer extends JLabel implements ListCellRenderer {

	public NRACellRenderer() {
		setOpaque(true);
	}

	public Component getListCellRendererComponent(JList list, Object value,
	                                              int index, boolean isSelected, boolean cellHasFocus) {

		int nraindex = 0;
		float distance = 1.0f;

		if (value instanceof NRA_Algorithm) {
			NRA_Algorithm nra = (NRA_Algorithm) value;
			nraindex = nra.getIndex();
			//System.out.println(nraindex);
			distance = nra.getDistance();
			//System.out.println(distance);

		}

		else {
			System.out.println(value.getClass());

		}

		setText("Index:   " + nraindex + "    " + "lb_agg:   " + distance);

		Color background;
		Color foreground;

		// check if this cell represents the current DnD drop location
		JList.DropLocation dropLocation = list.getDropLocation();
		if (dropLocation != null && !dropLocation.isInsert() && dropLocation.getIndex() == index) {

			background = Color.BLUE;
			foreground = Color.WHITE;
			// check if this cell is selected
		} else if (isSelected) {
			background = new Color(33, 83, 134);
			foreground = Color.WHITE;

			// unselected, and not the DnD drop location
		} else {
			if (distance > 1) {
				distance = 1;
			}
			background = new Color(distance, distance, distance);
			foreground = new Color(33, 83, 134);
		}
		;

		setBackground(background);
		setForeground(foreground);

		return this;
	}

}