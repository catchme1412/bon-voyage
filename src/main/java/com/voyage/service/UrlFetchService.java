package com.voyage.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Logger;

public class UrlFetchService {

	private static Logger logger = Logger.getLogger(UrlFetchService.class.getName());

	public StringBuilder fetch(URL url) throws IOException {
		BufferedReader reader = null;
		try {
			InputStream in = fetchInputStream(url);
			String line;
			StringBuilder builder = new StringBuilder();
			reader = new BufferedReader(new InputStreamReader(in));
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
			return builder;
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}

	public InputStream fetchInputStream(URL url) throws IOException {
		URLConnection connection = url.openConnection();
		connection.setRequestProperty("User-Agent", "(movie-arbiter.rhcloud.com catchme1412@gmail.com)");
		connection.setRequestProperty("Referrer", "movie-arbiter.rhcloud.com");
		connection.setRequestProperty("contact", "catchme1412@gmail.com");
		logger.info("URL:" + url.toExternalForm());
		return connection.getInputStream();
	}

	public static void main(String[] args) throws IOException {
		URL url;
		url = new URL(
				"https://www.googleapis.com/freebase/v1/mqlread/?lang=%2Flang%2Fen&query=[{+%22a%3Astarring%22%3A+[{+%22actor%22%3A+%22Mohanlal%22+}]%2C+%22b%3Astarring%22%3A+[{+%22actor%22%3A+%22Sreenivasan%22+}]%2C+%22name%22%3A+null%2C+%22language%22%3A+[]%2C+%22directed_by%22%3A+[]%2C+%22rating%22%3A+null%2C+%22type%22%3A+%22%2Ffilm%2Ffilm%22+}]");
		StringBuilder r = new UrlFetchService().fetch(url);
	}
}
