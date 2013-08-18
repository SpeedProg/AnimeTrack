package tk.speedprog.anime.animetrack.sitehandlers.batotonet;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tk.speedprog.anime.animetrack.sitehandlers.SiteHandler;

public class BatotoNetSiteHandler implements SiteHandler {
	private static final String REX_CHAPTERS = "<tr class=\"row lang_English\">\\s+<td style=\"border-top:0;\">\\s+<a href=\"([\\w\\W&&[^\"]]+)\"><img src=\"http://www.batoto.net/book_open.png\" style=\"vertical-align:middle;\"/> (?:Vol.\\d+ ){0,1}Ch.(\\d+(?:\\.\\d+){0,1}).*</a>";
	private static final Pattern PATTERN_CHAPTERS = Pattern
			.compile(REX_CHAPTERS);
	@Override
	public boolean isHandled(String host) {
		if (host.equals("www.batoto.net")) {
			return true;
		}
		return false;
	}

	@Override
	public int getLastEpisodeFor(String url, String regEx)
			throws MalformedURLException {
		int lastChap = 0;
		URL siteUrl = new URL(url);
		String webSource = tk.speedprog.utils.WebContentUtils.getContentOfUrl(siteUrl);
		// we need this list because there is more then one scanlation
		// some times on batoto
		Matcher chaptersMatcher = PATTERN_CHAPTERS.matcher(webSource);
		while (chaptersMatcher.find()) {
			String id = chaptersMatcher.group(2);
			try {
				int thisChap = Integer.parseInt(id);
				if (thisChap > lastChap) {
					lastChap = thisChap;
				}
			} catch (NumberFormatException e) {
				System.err.println("Couldn't parse chap id "+id);
			}
		}
		return lastChap;
	}

}
