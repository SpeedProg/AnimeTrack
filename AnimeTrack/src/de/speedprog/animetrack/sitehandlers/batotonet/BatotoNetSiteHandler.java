package de.speedprog.animetrack.sitehandlers.batotonet;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.speedprog.animetrack.sitehandlers.SiteHandler;

public class BatotoNetSiteHandler implements SiteHandler {
    public static void main(final String[] args) {
        final String testUrl1 = "http://bato.to/comic/_/comics/tokyo-ghoul-r3056";
        URL url = null;
        try {
            url = new URL(testUrl1);
        } catch (final MalformedURLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return;
        }
        
        final BatotoNetSiteHandler handler = new BatotoNetSiteHandler();
        if (handler.isHandled(url)) {
            try {
                final int last = handler.getLastEpisodeFor(testUrl1, null);
                System.out.println("Last Episode was: " + last);
            } catch (final MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            System.err.println("URL not handled!");
        }
    }

    private static final String REX_CHAPTERS = "<tr class=\"row lang_English chapter_row\">\\s+<td style=\"border-top:0;\">\\s+<a href=\"([\\w\\W&&[^\"]]+)\".*?><img src=\"http://bato.to/book_open.png\" style=\"vertical-align:middle;\"/> (?:Vol.\\d+ ){0,1}Ch.(\\d+(?:\\.\\d+){0,1}).*</a>";
    private static final Pattern PATTERN_CHAPTERS = Pattern
            .compile(REX_CHAPTERS);

    @Override
    public int getLastEpisodeFor(final String url, final String regEx)
            throws MalformedURLException {
        int lastChap = 0;
        final URL siteUrl = new URL(url);
        final String webSource = de.speedprog.utils.WebContentUtils
                .getContentOfUrl(siteUrl);
        
        final Matcher chaptersMatcher = PATTERN_CHAPTERS.matcher(webSource);
        while (chaptersMatcher.find()) {
            final String id = chaptersMatcher.group(2);
            try {
                final int thisChap = Integer.parseInt(id);
                if (thisChap > lastChap) {
                    lastChap = thisChap;
                }
            } catch (final NumberFormatException e) {
                System.err.println("Couldn't parse chap id " + id);
            }
        }
        return lastChap;
    }

    @Override
    public boolean isHandled(final URL url) {
        if (url.getHost().equals("bato.to")) {
            return true;
        }
        return false;
    }
}
