package tk.speedprog.anime.animetrack.sitehandlers.animehere;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tk.speedprog.anime.animetrack.Anime;
import tk.speedprog.anime.animetrack.sitehandlers.SiteHandler;

public class AnimehereSiteHandler implements SiteHandler {

	public AnimehereSiteHandler() {
	}

	@Override
	public boolean isHandled(URL url) {
		if (url.getHost().equals("www.animehere.com"))
			return true;
		return false;
	}

	@Override
	public int getLastEpisodeFor(String Url, String regEx)
			throws MalformedURLException {
		URL siteUrl = new URL(Url);
		BufferedReader siteReader;
		int currentMax = -1;
		try {
			siteReader = new BufferedReader(new InputStreamReader(
					siteUrl.openStream()));
			String line;
			StringBuilder siteCode = new StringBuilder();
			while ((line = siteReader.readLine()) != null) {
				siteCode.append(line + "\n");
			}
			String siteSource = siteCode.toString();
			int indexStart = siteSource
					.indexOf("<section class=\"date-list\">");
			System.out.println("StartIndex: " + indexStart);
			int indexEnd = siteSource.indexOf("</table>", indexStart);
			System.out.println("EndIndex: " + indexEnd);
			String table = siteSource.substring(indexStart, indexEnd);
			System.out.println(table);
			LinkedList<TR> trs = parseTable(table);
			LinkedList<String> titles = new LinkedList<String>();
			for (TR tr : trs) {
				titles.add(parseTr(tr.getContent()));
			}

			// finde max episode number
			for (String s : titles) {
				int number = parseTitle(s, regEx);
				currentMax = Math.max(currentMax, number);
			}
			System.out.println("Max: " + currentMax);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return currentMax;
	}

	private int parseTitle(String title, String regEx) {
		int ret = 0;
		System.out.println("Title: " + title);
		if (regEx != null && !regEx.isEmpty()) {

			Pattern p = Pattern.compile(regEx);
			Matcher m = p.matcher(title);
			// some one gave us a wrong regEx or it didn't match
			if (m.groupCount() != 1) {
				System.out.println("RegEx is wrong!");
				return 0;
			}
			if (!m.find()) {
				System.out.println("No Match!");
				return 0;
			}

			String episode = m.group(1);
			System.out.println("Matched: " + episode);
			try {
				ret = Integer.parseInt(episode);
			} catch (NumberFormatException e) {
				ret = 0;
			}

		} else {
			if (title.isEmpty()) {
				return 0;
			}
			int sEp = -1;
			sEp = title.lastIndexOf("Episode ");
			if (sEp == -1)
				sEp = title.lastIndexOf("episode ");
			if (sEp == -1) {
				return 0;
			}
			int startNumber = sEp + "Episode ".length();
			int endNumber = title.indexOf(" ", startNumber);
			if (endNumber == -1) {
				System.out.println("Normal Title, go to the end!");
				endNumber = title.length();
			} else {
				System.out.println("Sth is wrong, using till next space.");
			}
			String rest = title.substring(startNumber, endNumber);
			System.out.println("Rest: " + rest);
			if (rest.contains("-")) {
				String[] parts = rest.split("-");
				int max = 0;
				for (String part : parts) {
					max = Math.max(max, Integer.parseInt(part));
				}
				return max;
			}
			try {
				ret = Integer.parseInt(rest);
			} catch (NumberFormatException e) {
				ret = 0;
			}
		}
		return ret;
	}

	private LinkedList<TR> parseTable(String table) {
		int sTr = -1;
		int eTr = -1;
		int lastIndex = 0;
		LinkedList<TR> trList = new LinkedList<TR>();
		while ((sTr = table.indexOf("<tr>", lastIndex)) != -1
				&& (eTr = table.indexOf("</tr>", sTr)) != -1) {
			String trContent = table.substring(sTr + "<tr>".length(), eTr);
			TR tr = new TR(sTr, eTr, trContent);
			trList.add(tr);
			lastIndex = eTr;
		}
		return trList;
	}

	private String parseTr(String tr) {
		int sStartATag = -1;
		int eStartATag = -1;
		int sEndATag = -1;
		boolean hasSub = tr.contains("<span class=\"sub\"></span>");
		if (hasSub) {
			sStartATag = tr.indexOf("<a title=\"");
			eStartATag = tr.indexOf(">", sStartATag);
			sEndATag = tr.indexOf("</a>", eStartATag);

			return tr.substring(eStartATag + 1, sEndATag);
		}
		return "";
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// String url = "http://www.animehere.com/anime/07-ghost.html";
		String url = "http://www.animehere.com/anime/haiyoru-nyarukosan.html";
		String name = "Haiyoru! Nyaruko-san W";
		String regEx = "Haiyoru! Nyaruko-san W Episode (\\d+)";
		AnimehereSiteHandler siteHandler = new AnimehereSiteHandler();
		try {
			if (siteHandler.isHandled((new URL(url)))) {
				System.out.println("Site is handled.");
				Anime a = new Anime(name, url, regEx);
				int episode = siteHandler.getLastEpisodeFor(a.getUrl(),
						a.getRegEx());
				System.out.println("Last Episode is: " + episode);
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
