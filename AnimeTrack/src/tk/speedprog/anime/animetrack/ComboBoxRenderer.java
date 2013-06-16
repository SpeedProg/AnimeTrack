package tk.speedprog.anime.animetrack;

import java.awt.Color;
import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class ComboBoxRenderer<E> extends JLabel implements ListCellRenderer<E> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5496537612708547452L;
	private Color colorNotFinished = Color.ORANGE, colorNotStarted = new Color(200, 200, 200);
	public ComboBoxRenderer() {
		setOpaque(true);
		setHorizontalAlignment(LEFT);
		setVerticalAlignment(CENTER);
	}

	public ComboBoxRenderer(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public ComboBoxRenderer(Icon arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public ComboBoxRenderer(String arg0, int arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public ComboBoxRenderer(Icon arg0, int arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public ComboBoxRenderer(String arg0, Icon arg1, int arg2) {
		super(arg0, arg1, arg2);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	public Component getListCellRendererComponent(JList<? extends E> list,
			E value, int index, boolean isSelected, boolean cellHasFocus) {
		//int selectedIndex = ((Integer) value).intValue();
		boolean isFinished = true;
		if (value instanceof Anime && ((Anime) value).getLastOnline() > ((Anime) value).getLastWatched()) {
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
			if (((Anime)value).getLastWatched() > 0) {
			setBackground(colorNotFinished);
			} else {
				setBackground(colorNotStarted);
			}
		}
		if (value != null)
			setText(value.toString());
		return this;
	}

}
