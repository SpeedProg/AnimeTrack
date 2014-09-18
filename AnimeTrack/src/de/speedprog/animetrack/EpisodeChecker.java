package de.speedprog.animetrack;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;

import de.speedprog.animetrack.sitehandlers.SiteHandler;

public class EpisodeChecker {

	private LinkedList<SiteHandler> handlers;
	public EpisodeChecker() {
		handlers = new LinkedList<SiteHandler>();
		// TODO Auto-generated constructor stub
	}
	
	public boolean registerHandler(SiteHandler h) {
		return handlers.add(h);
	}
	
	public int getLastEpisode(Anime a) throws MalformedURLException {
		URL url = new URL(a.getUrl());
		for (SiteHandler sh : handlers) {
			if (sh.isHandled(url)) {
				return sh.getLastEpisodeFor(a.getUrl(), a.getRegEx());
			}
		}
		return 0;
	}
}
