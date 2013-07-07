package tk.speedprog.anime.animetrack.sitehandlers.starkanacom;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tk.speedprog.anime.animetrack.sitehandlers.DefaultSiteHandler;
import tk.speedprog.anime.animetrack.sitehandlers.SiteHandler;

public class StarkanacomSiteHandler extends DefaultSiteHandler {

	public StarkanacomSiteHandler() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// tests
		URL url;
		try {
			url = new URL("http://starkana.com/manga/F/Freezing");
			SiteHandler h = new StarkanacomSiteHandler();
			if (h.isHandled(url.getHost())) {
				int lC = h.getLastEpisodeFor(url.toString(), "");
				System.out.println("Last chapter: "+lC);
			} else {
				System.out.println("Host: "+url.getHost()+" is not handled.");
			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public boolean isHandled(String host) {
		if (host.equals("starkana.com")) {
			return true;
		}
		return false;
	}

	@Override
	public int getLastEpisodeFor(String url, String regEx)
			throws MalformedURLException {
		// check if it ends in ?mature_confirm=1 because of underage protection
		if (!url.endsWith("?mature_confirm=1"))
			url = url + "?mature_confirm=1";
		URL siteUrl = new URL(url);
		int episode = -1;
		try {
			String siteContent = getContentOfUrl(siteUrl);
			System.out.println("Site Content: "+siteContent);
			/*
			 * <div class="c_h1b" style="border-top: 1px solid #fff;">Downloads</div>
			 * <div class="episode c_h2">
			 * <div class="episode c_h2b">
			 */
			String episodeDivRegEx = "<div class=\"zxz episode c_h2[b]{0,1}\">\\s+<div><a class=\"download-link\" href=\"/manga/\\w/.+/chapter/[\\d\\.]+\">.+ <em>chapter</em> <strong>([\\d\\.]+)</strong></a>.*\\s+</div>\\s+<div class=\"clear\"></div>\\s+</div>";
			Pattern episodeDivPattern = Pattern.compile(episodeDivRegEx);
			Matcher matcher = episodeDivPattern.matcher(siteContent);
			while (matcher.find()) {
				int ne = -1;
				try {
					ne = Integer.parseInt(matcher.group(1));
				} catch (NumberFormatException e) {
					ne = (int) Float.parseFloat(matcher.group(1));
				}
				episode = Math.max(episode, ne);
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
