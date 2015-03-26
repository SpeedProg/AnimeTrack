package de.speedprog.animetrack;

/**
 * Class to hold all informations about an anime.
 * 
 * @author SpeedProg
 * 
 */
public class Anime {

	/**
	 * Fields hold lastOnline chapter number, and last watched chapter number.
	 */
	private int lastOnline, lastWatched;
	/**
	 * Fields holding other anime attributes.
	 */
	private String url, name, note, regEx;

	/**
	 * Create an anime Object.
	 * 
	 * @param animeName
	 *            Name the anime
	 * @param animeUrl
	 *            Url of the anime
	 * @param animeLastWatchedEpisode
	 *            Last watched episode
	 * @param lastKnownEpisode
	 *            last found episode
	 * @param animeNote
	 *            notices
	 * @param animeRegEx
	 *            REX to use for search
	 */
	public Anime(final String animeName, final String animeUrl,
			final int animeLastWatchedEpisode, final int lastKnownEpisode,
			final String animeNote, final String animeRegEx) {
		if (animeName == null || animeUrl == null || animeNote == null) {
			throw new IllegalArgumentException();
		}
		this.url = animeUrl;
		this.name = animeName;
		this.lastOnline = lastKnownEpisode;
		this.lastWatched = animeLastWatchedEpisode;
		this.note = animeNote;
		if (animeRegEx == null) {
			this.regEx = "";
		} else {
			this.regEx = animeRegEx;
		}
	}

	/**
	 * @return the url
	 */
	public final String getUrl() {
		return url;
	}

	/**
	 * Sets the url of the Anime
	 * @param url the url to set
	 */
	public final void setUrl(String url) {
		this.url = url;
	}
	
	/**
	 * @return the name
	 */
	public final String getName() {
		return name;
	}

	/**
	 * 
	 * @param animeName
	 *            the name
	 */
	public final void setName(final String animeName) {
		this.name = animeName;
	}

	/**
	 * Create an anime.
	 * 
	 * @param animeName
	 *            Name the anime
	 * @param animeUrl
	 *            Url of the anime
	 * @param animeLastWatchedEpisode
	 *            Last watched episode
	 * @param animeRegEx
	 *            REX to use for search
	 */
	public Anime(final String animeName, final String animeUrl,
			final String animeRegEx, final int animeLastWatchedEpisode) {
		this(animeName, animeUrl, animeLastWatchedEpisode, 0, "", animeRegEx);
	}

	/**
	 * Create an anime.
	 * 
	 * @param animeName
	 *            name of the Anime
	 * @param animeUrl
	 *            Animes url
	 * @param animeRegEx
	 *            REX to use for chapter finding
	 */
	public Anime(final String animeName, final String animeUrl,
			final String animeRegEx) {
		this(animeName, animeUrl, 0, 0, "", animeRegEx);
	}

	/**
	 * @return the lastKnownEpisode
	 */
	public final int getLastOnline() {
		return lastOnline;
	}

	/**
	 * @param lastKnownEpisode
	 *            the lastKnownEpisode to set
	 */
	public final void setLastOnline(final int lastKnownEpisode) {
		this.lastOnline = lastKnownEpisode;
	}

	/**
	 * @return the lastWatchedEpisode
	 */
	public final int getLastWatched() {
		return lastWatched;
	}

	/**
	 * @param animeLastWatched
	 *            the lastWatchedEpisode to set
	 */
	public final void setLastWatched(final int animeLastWatched) {
		this.lastWatched = animeLastWatched;
	}

	@Override
	public final String toString() {
		return (name + "(" + (lastOnline - lastWatched) + ")");
	}

	/**
	 * @return the note
	 */
	public final String getNote() {
		return note;
	}

	/**
	 * @param animeNote
	 *            the note to set
	 */
	public final void setNote(final String animeNote) {
		this.note = animeNote;
	}

	@Override
	public final boolean equals(final Object o) {
		if (o instanceof Anime) {
			Anime a = (Anime) o;
			if (a.name == null && this.name == null) {
				return true;
			}
			if (this.name == null) {
				return false;
			}
			return this.name.equals(a.name);
		}
		return false;
	}

	@Override
	public final int hashCode() {
		return getName().hashCode();
	}

	/**
	 * @return the regEx
	 */
	public final String getRegEx() {
		return regEx;
	}

	/**
	 * @param animeRegEx
	 *            the regEx to set
	 */
	public final void setRegEx(final String animeRegEx) {
		if (animeRegEx == null) {
			this.regEx = "";
		}
		this.regEx = animeRegEx;
	}

}
