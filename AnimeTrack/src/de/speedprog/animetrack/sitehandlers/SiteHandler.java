package de.speedprog.animetrack.sitehandlers;

import java.net.MalformedURLException;
import java.net.URL;

public interface SiteHandler {
	public boolean isHandled(URL url);
	public int getLastEpisodeFor(String Url, String regEx) throws MalformedURLException;
}
