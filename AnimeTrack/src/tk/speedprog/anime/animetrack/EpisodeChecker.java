package tk.speedprog.anime.animetrack;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;

import tk.speedprog.anime.animetrack.sitehandlers.SiteHandler;

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
		String host = url.getHost();
		for (SiteHandler sh : handlers) {
			if (sh.isHandled(host)) {
				return sh.getLastEpisodeFor(a.getUrl(), a.getRegEx());
			}
		}
		return 0;
	}
}
