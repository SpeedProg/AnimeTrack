package de.speedprog.animetrack;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteConfig.LockingMode;

import de.speedprog.animetrack.sitehandlers.animehere.AnimehereSiteHandler;
import de.speedprog.animetrack.sitehandlers.batotonet.BatotoNetSiteHandler;
import de.speedprog.animetrack.sitehandlers.starkanacom.StarkanacomSiteHandler;
import de.speedprog.animetrack.sitehandlers.tokyoinsidernet.TokyoinsidernetSiteHandler;

import java.awt.Font;
import java.awt.List;

import javax.swing.UIManager;

public class MainWindow implements MainWindowInterface, ActionListener,
		WindowListener, ItemListener {

	private static final String MANGARIPPER_PATH = "F:\\Programme\\MangaRipper\\MangaRipper.jar";
	private static final String MANGARIPPER_WORKINGDIR = "F:\\Programme\\MangaRipper";
	private static final String SQLPREP_ENTRY_RENAME = "UPDATE Anime SET name=? where name=?;";
	private JFrame frame;
	private Connection dbCon;
	private LinkedList<Anime> animes;
	private static String createAnimeTableIfNotExistsQuery = "create table if not exists Anime (name TXT PRIMARY KEY, url TEXT, lastWatched INTEGER, lastOnline INTEGER, note TEXT, regEx TEXT);";
	private static String getAllAnimesQuery = "select name, url, lastWatched, lastOnline, note, regEx from Anime;";
	private JComboBox<Anime> comboBoxAnimes;
	private JComboBox<AnimeStatus> comboBox;
	private EpisodeChecker checker;
	JSpinner spinnerLastOnlineEpisode;
	JSpinner spinnerLastSeenEpisode;
	JTextPane textPaneNotes;
	private JTextField textFieldRegex;
	private JTextArea textAreaLog;
	private JButton btnRename;
	private JScrollPane scrollPane;
	private JLabel lblUrl;
	private JTextField textFieldUrl;
	private JButton btnExecuteRipper;

	/**
	 * Create the application.
	 */
	public MainWindow() {
		dbCon = null;
		animes = new LinkedList<Anime>();
		checker = new EpisodeChecker();
		checker.registerHandler(new AnimehereSiteHandler());
		checker.registerHandler(new TokyoinsidernetSiteHandler());
		checker.registerHandler(new StarkanacomSiteHandler());
		checker.registerHandler(new BatotoNetSiteHandler());
		initializeDb();
		createTableIfNotExists();
		loadAnimesFromDb();
		initialize();
	}

	private void initializeDb() {
		SQLiteConfig config = new SQLiteConfig();
		config.setLockingMode(LockingMode.EXCLUSIVE);
		try {
			dbCon = DriverManager.getConnection("jdbc:sqlite:database.db");
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	private void createTableIfNotExists() {
		if (dbCon == null) {
			System.exit(0);
		}

		try {
			Statement stat = dbCon.createStatement();
			stat.execute(createAnimeTableIfNotExistsQuery);
			stat.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void loadAnimesFromDb() {
		try {
			Statement stat = dbCon.createStatement();
			ResultSet results = stat.executeQuery(getAllAnimesQuery);
			String name = "", url = "", note = "", regEx = "";
			int lastWatched = 0, lastOnline = 0;
			while (results.next()) {
				name = results.getString("name");
				url = results.getString("url");
				note = results.getString("note");
				lastWatched = results.getInt("lastWatched");
				lastOnline = results.getInt("lastOnline");
				regEx = results.getString("regEx");
				Anime anime = new Anime(name, url, lastWatched, lastOnline,
						note, regEx);
				animes.add(anime);
			}
			results.close();
			stat.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Collections.sort(animes, new AnimeComperator());
	}

	private void addAnimeToDb(final Anime a) {
		Statement stat = null;
		try {
			stat = dbCon.createStatement();
			stat.executeUpdate("insert into Anime (name, url, lastWatched, lastOnline, note, regEx) values ('"
					+ a.getName()
					+ "', '"
					+ a.getUrl()
					+ "', "
					+ a.getLastWatched()
					+ ", "
					+ a.getLastOnline()
					+ ", '"
					+ a.getNote() + "', '" + a.getRegEx() + "');");
			stat.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(
				new MigLayout("", "[][grow][grow]",
						"[][][][][][][][grow][][grow]"));
		frame.addWindowListener(this);

		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		frame.getContentPane().add(toolBar, "flowx,cell 0 0 3 1");

		JButton btnCheck = new JButton("Check");
		btnCheck.setActionCommand("Check");
		btnCheck.addActionListener(this);

		JButton btnUpdateAll = new JButton("Update All");
		btnUpdateAll.setActionCommand("Update All");
		btnUpdateAll.addActionListener(this);
		toolBar.add(btnUpdateAll);
		toolBar.add(btnCheck);

		JButton btnAddNew = new JButton("Add New");
		btnAddNew.setActionCommand("Add New");
		btnAddNew.addActionListener(this);

		comboBox = new JComboBox<AnimeStatus>();
		comboBox.setModel(new DefaultComboBoxModel<AnimeStatus>(AnimeStatus
				.values()));
		comboBox.addItemListener(this);
		toolBar.add(comboBox);
		toolBar.add(btnAddNew);

		btnRename = new JButton("Rename");
		btnRename.setActionCommand("rename");
		btnRename.addActionListener(this);
		toolBar.add(btnRename);

		comboBoxAnimes = new JComboBox<Anime>();
		comboBoxAnimes.setEditable(false);
		comboBoxAnimes.setRenderer(new ComboBoxRenderer<Anime>());
		comboBoxAnimes.setModel(new DefaultComboBoxModel<Anime>());
		frame.getContentPane().add(comboBoxAnimes, "cell 0 1 3 1,growx");
		DefaultComboBoxModel<Anime> comboBoxModel = (DefaultComboBoxModel<Anime>) comboBoxAnimes
				.getModel();
		for (Anime a : animes) {
			comboBoxModel.addElement(a);
		}
		comboBoxAnimes.addItemListener(this);
		Anime a = (Anime) comboBoxAnimes.getSelectedItem();

		JLabel lblLetzeGesehenFolge = new JLabel("Letze gesehen Folge:");
		lblLetzeGesehenFolge.setFont(UIManager.getFont("Label.font"));
		frame.getContentPane().add(lblLetzeGesehenFolge, "flowx,cell 0 2");

		spinnerLastSeenEpisode = new JSpinner();
		spinnerLastSeenEpisode.setModel(new SpinnerNumberModel(new Integer(0),
				null, null, new Integer(1)));
		if (a != null) {
			spinnerLastSeenEpisode.setValue(a.getLastWatched());
		}

		frame.getContentPane()
				.add(spinnerLastSeenEpisode, "cell 1 2 2 1,growx");

		JLabel lblLetzeOnlineFolge = new JLabel("Letze online Folge:");
		frame.getContentPane().add(lblLetzeOnlineFolge, "cell 0 3");

		spinnerLastOnlineEpisode = new JSpinner();
		if (a != null) {
			spinnerLastOnlineEpisode.setValue(a.getLastOnline());
		}
		frame.getContentPane().add(spinnerLastOnlineEpisode,
				"cell 1 3 2 1,growx");

		JLabel lblRegex = new JLabel("RegEx:");
		lblRegex.setHorizontalAlignment(SwingConstants.LEFT);
		frame.getContentPane().add(lblRegex, "cell 0 4,alignx trailing");

		textFieldRegex = new JTextField();
		if (a != null) {
			textFieldRegex.setText(a.getRegEx());
		}
		frame.getContentPane().add(textFieldRegex, "cell 1 4 2 1,growx");
		textFieldRegex.setColumns(10);

		textPaneNotes = new JTextPane();
		if (a != null) {
			textPaneNotes.setText(a.getNote());
		}

		lblUrl = new JLabel("Url:");
		frame.getContentPane().add(lblUrl, "cell 0 5,alignx trailing");

		textFieldUrl = new JTextField();
		frame.getContentPane().add(textFieldUrl, "cell 1 5 2 1,growx");
		textFieldUrl.setColumns(10);

		btnExecuteRipper = new JButton("Execute Ripper");
		btnExecuteRipper.setActionCommand("ExecRipper");
		btnExecuteRipper.addActionListener(this);
		frame.getContentPane().add(btnExecuteRipper, "cell 0 6");
		frame.getContentPane().add(textPaneNotes, "cell 0 7 3 1,grow");

		JButton btnSaveChanges = new JButton("Save Changes");
		btnSaveChanges.setMnemonic(KeyEvent.VK_S);
		btnSaveChanges.setActionCommand("Save Changes");
		btnSaveChanges.addActionListener(this);
		frame.getContentPane().add(btnSaveChanges, "cell 0 8");

		scrollPane = new JScrollPane();
		frame.getContentPane().add(scrollPane, "cell 0 9 3 1,grow");

		textAreaLog = new JTextArea();
		scrollPane.setViewportView(textAreaLog);
		textAreaLog.setEditable(false);
	}

	private void cleanUp() {
		PreparedStatement preparedStatementUpdateAnime;
		try {
			preparedStatementUpdateAnime = dbCon
					.prepareStatement("UPDATE Anime SET url = ?, lastWatched = ?, lastOnline = ?, note = ?, regEx = ? WHERE name = ?;");
			for (Anime a : animes) {
				preparedStatementUpdateAnime.setString(1, a.getUrl());
				preparedStatementUpdateAnime.setInt(2, a.getLastWatched());
				preparedStatementUpdateAnime.setInt(3, a.getLastOnline());
				preparedStatementUpdateAnime.setString(4, a.getNote());
				preparedStatementUpdateAnime.setString(5, a.getRegEx());
				preparedStatementUpdateAnime.setString(6, a.getName());
				System.out.println("Anime: " + a.getName());
				System.out.println("Commit RegEx: " + a.getRegEx());
				preparedStatementUpdateAnime.addBatch();
			}
			dbCon.setAutoCommit(false);
			int[] results = preparedStatementUpdateAnime.executeBatch();
			StringBuilder output = new StringBuilder();
			output.append("[");
			for (int i = 0; i < results.length; i++) {
				output.append(results[i] + ", ");
			}
			output.append("]");
			System.out.println(output.toString());
			dbCon.setAutoCommit(true);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			dbCon.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public final void addAnime(final Anime a) {
		if (!animes.contains(a)) {
			animes.add(a);
			addAnimeToDb(a);
			((DefaultComboBoxModel<Anime>) comboBoxAnimes.getModel())
					.addElement(a);
		}
	}

	@Override
	public final void actionPerformed(final ActionEvent arg0) {
		if (arg0.getActionCommand().equals("Add New")) {
			AddAnimeDialog dialog = new AddAnimeDialog(this);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} else if (arg0.getActionCommand().equals("Save Changes")) {
			Anime a = getSelectedAnime();
			a.setLastOnline((int) spinnerLastOnlineEpisode.getValue());
			a.setLastWatched((int) spinnerLastSeenEpisode.getValue());
			a.setNote(textPaneNotes.getText());
			a.setRegEx(textFieldRegex.getText());
			a.setUrl(textFieldUrl.getText());
			System.out.println("Set RegEx: " + textFieldRegex.getText());
		} else if (arg0.getActionCommand().equals("Check")) {
			Anime a = getSelectedAnime();
			try {
				int lastEpisode = checker.getLastEpisode(a);
				if (lastEpisode != -1) {
					a.setLastOnline(lastEpisode);
					spinnerLastOnlineEpisode.setValue(a.getLastOnline());
				} else {
					logln("Updating " + a.getName() + " failed!");
				}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (arg0.getActionCommand().equals("Update All")) {
			Runnable check = new Runnable() {
				@Override
				public void run() {
					logln("Started Updating");
					for (Anime a : animes) {
						int lastEpisode = 0;
						try {
							lastEpisode = checker.getLastEpisode(a);
							int cLast = a.getLastOnline();
							if (lastEpisode != cLast) {
								logln("Last Episode for " + a.getName()
										+ " is " + lastEpisode + ".");
								if (lastEpisode != -1) {
									a.setLastOnline(lastEpisode);
									spinnerLastOnlineEpisode.setValue(a
											.getLastOnline());
									logln("Updated " + a.getName() + " from "
											+ cLast + " to " + lastEpisode);
								} else {
									logln("Updating " + a.getName()
											+ " failed!");
								}
							} else {
								logln(a.getName() + " was uptodate.");
							}
						} catch (MalformedURLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					logln("Finished Updating");

				}
			};
			Thread t = new Thread(check);
			t.start();
		} else if (arg0.getActionCommand().equals("rename")) {
			Anime a = getSelectedAnime();
			String aName = a.getName();
			String newName = JOptionPane.showInputDialog(
					"Please enter the new name of the tracking object.", aName);
			System.out.println("New Name: " + newName);
			try {
				PreparedStatement stat = dbCon
						.prepareStatement(SQLPREP_ENTRY_RENAME);
				stat.setString(1, newName);
				stat.setString(2, aName);
				if (stat.executeUpdate() > 0) {
					a.setName(newName);
				}
				stat.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (arg0.getActionCommand().equals("ExecRipper")) {
			int startAt = ((int) spinnerLastSeenEpisode.getValue()) + 1;
			String urlString = textFieldUrl.getText();
			Anime anime = getSelectedAnime();
			String name = anime.getName();
			if (name.endsWith(" - Anime")) {
				name = name.substring(0, name.length() - 8);
			} else if (name.endsWith(" - Manga")) {
				name = name.substring(0, name.length() - 8);
			}
			java.util.List commands = new ArrayList<String>(5);
			commands.add("java");
			commands.add("-jar");
			commands.add(MANGARIPPER_PATH);
			commands.add(urlString);
			commands.add(name.replace("\"", "\\\""));
			commands.add(String.valueOf(startAt));
			ProcessBuilder pBuilder = new ProcessBuilder(commands);
			pBuilder.directory(new File(MANGARIPPER_WORKINGDIR));
			try {
				pBuilder.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	@Override
	public void windowActivated(final WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(final WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public final void windowClosing(final WindowEvent e) {
		cleanUp();
	}

	@Override
	public void windowDeactivated(final WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(final WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(final WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(final WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public final void itemStateChanged(final ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			if (e.getSource() == comboBoxAnimes) {
				Anime a = (Anime) e.getItem();
				textPaneNotes.setText(a.getNote());
				spinnerLastOnlineEpisode.setValue(a.getLastOnline());
				spinnerLastSeenEpisode.setValue(a.getLastWatched());
				textFieldRegex.setText(a.getRegEx());
				textFieldUrl.setText(a.getUrl());
			} else if (e.getSource() == comboBox) {
				AnimeStatus animeStatus = (AnimeStatus) e.getItem();
				DefaultComboBoxModel<Anime> model = (DefaultComboBoxModel<Anime>) comboBoxAnimes
						.getModel();
				model.removeAllElements();
				switch (animeStatus) {
				case FOLLOWING:
					for (Anime a : animes) {
						model.addElement(a);
					}
					break;
				case FINISHED:
					for (Anime a : animes) {
						if (a.getLastOnline() != 0
								&& a.getLastOnline() == a.getLastWatched()) {
							model.addElement(a);
						}
					}
					break;
				case WATCHING:
					for (Anime a : animes) {
						if (a.getLastOnline() != 0 && a.getLastWatched() > 0
								&& a.getLastOnline() > a.getLastWatched()) {
							model.addElement(a);
						}
					}
					break;
				case NOTSTARTED:
					for (Anime a : animes) {
						if (a.getLastWatched() == 0) {
							model.addElement(a);
						}
					}
					break;
				default:
					break;
				}
			}
		}

	}

	public void setVisible(boolean vis) {
		frame.setVisible(vis);
	}

	private Anime getSelectedAnime() {
		return (Anime) comboBoxAnimes.getSelectedItem();
	}

	private void logln(final String s) {
		textAreaLog.append(s + "\n");
	}

	class AnimeComperator implements Comparator<Anime> {
		@Override
		public int compare(final Anime o1, final Anime o2) {
			if (o1.getName() == null) {
				if (o2.getName() == null) {
					return 0;
				}
				return -1;
			}
			if (o2.getName() == null) {
				return 1;
			}
			return o1.getName().compareTo(o2.getName());
		}

	}
}
