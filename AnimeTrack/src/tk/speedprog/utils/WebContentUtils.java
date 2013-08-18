package tk.speedprog.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import javax.xml.ws.handler.MessageContext.Scope;

public final class WebContentUtils {
	private static final String REX_CHARSET = "charset=([\\w\\-_]+)";
	private static final Pattern PATTERN_CHARSET = Pattern.compile(REX_CHARSET);

	/**
	 * @param url
	 *            Url to fetch the source from
	 * @return source of the url as String
	 */
	public static String getContentOfUrl(final URL url, Charset cs) {
		boolean done = false;
		StringBuilder siteCode;
		String siteSource = "";
		while (!done) {
			try {
				siteCode = new StringBuilder();
				BufferedReader siteReader;
				URLConnection con = url.openConnection();
				String encoding = con.getContentEncoding();
				if (cs == null) {
					String contentType = con.getContentType();
					Matcher charsetM = PATTERN_CHARSET.matcher(contentType);
					String charsetName = null;
					if (charsetM.find()) {
						charsetName = charsetM.group(1);
					}
					if (charsetName == null) {
						charsetName = "UTF-8";
					}
					try {
						cs = Charset.forName(charsetName);
					} catch (IllegalCharsetNameException e) {
						cs = Charset.forName("UTF-8");
					}
				}

				InputStream stream;
				if (encoding != null && encoding.equals("gzip")) {
					stream = new GZIPInputStream(con.getInputStream());
				} else {
					stream = con.getInputStream();
				}
				siteReader = new BufferedReader(new InputStreamReader(stream,
						cs));
				String line;
				int len = 0;
				int bufferSize = 4096;
				char[] buffer = new char[bufferSize];
				while ((len = siteReader.read(buffer)) > 0) {
					siteCode.append(buffer, 0, len);
				}
				siteSource = siteCode.toString();
				done = true;
			} catch (IOException e) {
				if (url != null) {
					System.out.print("Error on URL: " + url.toExternalForm()
							+ " Trying again!\n");
				}
				e.printStackTrace(System.out);
			}
		}
		return siteSource;
	}

	public static String getContentOfUrl(final URL url) {
		return getContentOfUrl(url, null);
	}
}
