package tk.speedprog.anime.animetrack.sitehandlers;

import java.net.MalformedURLException;

public interface SiteHandler {
	public boolean isHandled(String host);
	public int getLastEpisodeFor(String Url, String regEx) throws MalformedURLException;
}
