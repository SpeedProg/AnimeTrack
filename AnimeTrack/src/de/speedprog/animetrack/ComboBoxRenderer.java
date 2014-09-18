package de.speedprog.animetrack;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * Custom Render for my combobox that should display anime states.
 * 
 * @author SpeedProg
 * 
 * @param <E>
 */
public class ComboBoxRenderer<E> extends JLabel implements ListCellRenderer<E> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5496537612708547452L;
	/**
	 * Colors for special series states.
	 */
	private static final Color COLOR_NOTFINISHED = Color.ORANGE,
			COLOR_NOTSTARTED = new Color(200, 200, 200);

	/**
	 * Create a custom renderer.
	 */
	public ComboBoxRenderer() {
		setOpaque(true);
		setHorizontalAlignment(LEFT);
		setVerticalAlignment(CENTER);
	}

	@Override
	public final Component getListCellRendererComponent(
			final JList<? extends E> list, final E value, final int index,
			final boolean isSelected, final boolean cellHasFocus) {
		boolean isFinished = true;
		if (value instanceof Anime
				&& ((Anime) value).getLastOnline() > ((Anime) value)
						.getLastWatched()) {
			isFinished = false;
		}
		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}
		if (!isFinished) {
			if (((Anime) value).getLastWatched() > 0) {
				setBackground(COLOR_NOTFINISHED);
			} else {
				setBackground(COLOR_NOTSTARTED);
			}
		}
		if (value != null) {
			setText(value.toString());
		}
		return this;
	}

}
