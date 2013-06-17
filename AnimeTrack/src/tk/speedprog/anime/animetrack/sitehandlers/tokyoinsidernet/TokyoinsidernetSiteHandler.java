package tk.speedprog.anime.animetrack.sitehandlers.tokyoinsidernet;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tk.speedprog.anime.animetrack.sitehandlers.DefaultSiteHandler;

public class TokyoinsidernetSiteHandler extends DefaultSiteHandler {

	public TokyoinsidernetSiteHandler() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isHandled(String host) {
		if (host.equals("tokyoinsider.net")) {
			return true;
		}
		return false;
	}

	@Override
	public int getLastEpisodeFor(String url, String regEx)
			throws MalformedURLException {
		URL siteUrl = new URL(url);
		int episode = -1;
		try {
			String siteContent = getContentOfUrl(siteUrl);
			/*
			 * <div class="c_h1b" style="border-top: 1px solid #fff;">Downloads</div>
			 * <div class="episode c_h2">
			 * <div class="episode c_h2b">
			 */
			String episodeDivRegEx = "<div class=\"episode c_h2[b]{0,1}\">\\s+<div><a class=\"download-link\" href=\"/anime/\\w/.+/episode/\\d+\">.+ <em>episode</em> <strong>(\\d+)</strong></a>.*\\s+</div>\\s+<div class=\"clear\"></div>\\s+</div>";
			Pattern episodeDivPattern = Pattern.compile(episodeDivRegEx);
			Matcher matcher = episodeDivPattern.matcher(siteContent);
			while (matcher.find()) {
				episode = Math.max(episode, Integer.parseInt(matcher.group(1)));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return episode;
	}

}
