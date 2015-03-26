package de.speedprog.animetrack.sitehandlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public abstract class DefaultSiteHandler implements SiteHandler {


	protected String getContentOfUrl(URL url) throws IOException {
		BufferedReader siteReader;
		siteReader = new BufferedReader(new InputStreamReader(
				url.openStream()));
		String line;
		StringBuilder siteCode = new StringBuilder();
		while ((line = siteReader.readLine()) != null) {
			siteCode.append(line + "\n");
		}
		String siteSource = siteCode.toString();
		return siteSource;
	}

}
