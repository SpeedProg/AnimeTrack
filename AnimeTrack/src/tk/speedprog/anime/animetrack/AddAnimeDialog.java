package tk.speedprog.anime.animetrack;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import javax.swing.JTextField;
import javax.swing.JLabel;

public class AddAnimeDialog extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4466909675723812801L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldName;
	private JTextField textFieldUrl;
	private Anime a;
	private MainWindowInterface mwa;
	private JTextField textFieldRegEx;

	/**
	 * Create the dialog.
	 */
	public AddAnimeDialog(MainWindowInterface mwa) {
		this.mwa = mwa;
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[][grow]", "[][][]"));
		{
			JLabel lblName = new JLabel("Name:");
			contentPanel.add(lblName, "cell 0 0,alignx trailing");
		}
		{
			textFieldName = new JTextField();
			contentPanel.add(textFieldName, "cell 1 0,growx");
			textFieldName.setColumns(10);
		}
		{
			JLabel lblUrl = new JLabel("URL:");
			contentPanel.add(lblUrl, "cell 0 1,alignx trailing");
		}
		{
			textFieldUrl = new JTextField();
			contentPanel.add(textFieldUrl, "cell 1 1,growx");
			textFieldUrl.setColumns(10);
		}
		{
			JLabel lblRegex = new JLabel("RegEx:");
			contentPanel.add(lblRegex, "cell 0 2,alignx trailing");
		}
		{
			textFieldRegEx = new JTextField();
			contentPanel.add(textFieldRegEx, "cell 1 2,growx");
			textFieldRegEx.setColumns(10);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				okButton.addActionListener(this);
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				cancelButton.addActionListener(this);
				buttonPane.add(cancelButton);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getActionCommand().equals("OK")) {
			a = new Anime(textFieldName.getText(), textFieldUrl.getText(), textFieldRegEx.getText());
			mwa.addAnime(a);
			this.dispose();
		} else {
			this.dispose();
		}
	}

}
