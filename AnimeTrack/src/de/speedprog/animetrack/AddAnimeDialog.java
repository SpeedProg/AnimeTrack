package de.speedprog.animetrack;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import javax.swing.JTextField;
import javax.swing.JLabel;

/**
 * Dialog for inputting information about the anime to add.
 * 
 * @author SpeedProg
 * 
 */
public class AddAnimeDialog extends JDialog implements ActionListener {

	/**
	 * Default width of the dialog.
	 */
	private static final int DEFWIDTH = 300;
	/**
	 * Default height of the dialog.
	 */
	private static final int DEFHEIGHT = 200;
	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = -4466909675723812801L;
	/**
	 * JTextField that will contain the Animes name.
	 */
	private JTextField textFieldName;
	/**
	 * JTextField that will contain the Animes url.
	 */
	private JTextField textFieldUrl;
	/**
	 * The MainWindow that will get the Anime.
	 */
	private MainWindowInterface mwa;
	/**
	 * JTextField for the REX to use for detecting Anime numbers.
	 */
	private JTextField textFieldRegEx;

	/**
	 * Create the dialog.
	 * @param mainWindow The MainWindowInterface implementation to use
	 */
	public AddAnimeDialog(final MainWindowInterface mainWindow) {
		JPanel contentPanel = new JPanel();
		setMinimumSize(new Dimension(DEFWIDTH, DEFHEIGHT));
		this.mwa = mainWindow;
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[][grow]", "[][][]"));
		JLabel lblName = new JLabel("Name:");
		contentPanel.add(lblName, "cell 0 0,alignx trailing");
		textFieldName = new JTextField();
		contentPanel.add(textFieldName, "cell 1 0,growx");

		JLabel lblUrl = new JLabel("URL:");
		contentPanel.add(lblUrl, "cell 0 1,alignx trailing");

		textFieldUrl = new JTextField();
		contentPanel.add(textFieldUrl, "cell 1 1,growx");
		JLabel lblRegex = new JLabel("RegEx:");
		contentPanel.add(lblRegex, "cell 0 2,alignx trailing");

		textFieldRegEx = new JTextField();
		contentPanel.add(textFieldRegEx, "cell 1 2,growx");

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton okButton = new JButton("OK");
		okButton.setActionCommand("OK");
		okButton.addActionListener(this);
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand("Cancel");
		cancelButton.addActionListener(this);
		buttonPane.add(cancelButton);
	}

	@Override
	public final void actionPerformed(final ActionEvent arg0) {
		if (arg0.getActionCommand().equals("OK")) {
			Anime a = new Anime(textFieldName.getText(),
					textFieldUrl.getText(), textFieldRegEx.getText());
			mwa.addAnime(a);
			this.dispose();
		} else {
			this.dispose();
		}
	}

}
