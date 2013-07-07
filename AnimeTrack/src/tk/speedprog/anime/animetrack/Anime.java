package tk.speedprog.anime.animetrack;

public class Anime {

	private int lastOnline, lastWatched;
	private String url, name, note, regEx;

	public Anime(String name, String url, int lastWatchedEpisode,
			int lastKnownEpisode, String note, String regEx) {
		this.url = url;
		this.name = name;
		this.lastOnline = lastKnownEpisode;
		this.lastWatched = lastWatchedEpisode;
		this.note = note;
		if (regEx == null)
			this.regEx = "";
		else
			this.regEx = regEx;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Anime(String name, String url, String regEx, int lastWatchedEpisode) {
		this(name, url, lastWatchedEpisode, 0, "", regEx);
	}

	public Anime(String name, String url, String regEx) {
		this(name, url, 0, 0, "", regEx);
	}

	/**
	 * @return the lastKnownEpisode
	 */
	public int getLastOnline() {
		return lastOnline;
	}

	/**
	 * @param lastKnownEpisode
	 *            the lastKnownEpisode to set
	 */
	public void setLastOnline(int lastOnline) {
		this.lastOnline = lastOnline;
	}

	/**
	 * @return the lastWatchedEpisode
	 */
	public int getLastWatched() {
		return lastWatched;
	}

	/**
	 * @param lastWatchedEpisode
	 *            the lastWatchedEpisode to set
	 */
	public void setLastWatched(int lastWatched) {
		this.lastWatched = lastWatched;
	}

	@Override
	public String toString() {
		return name;
	}

	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @param note
	 *            the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Anime && ((Anime) o).getName().equals(this.name)) {
			return true;
		}
		return false;
	}

	/**
	 * @return the regEx
	 */
	public String getRegEx() {
		return regEx;
	}

	/**
	 * @param regEx
	 *            the regEx to set
	 */
	public void setRegEx(String regEx) {
		if (regEx == null) {
			regEx = "";
		}
		this.regEx = regEx;
	}

}
