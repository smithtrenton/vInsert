package org.vinsert.api.util;

import java.io.*;
import java.net.*;

/**
 * A class that can be used to get the Zybez current price of an item
 * @author Cheddy
 *
 */
public class PriceGrabber {

	private static final String ZYBEZ_URL = "http://forums.zybez.net/runescape-2007-prices/api/item/";

	public enum PriceGrabType {
		MIN, AVERAGE, MAX
	};

	public int getPrice(String itemName, PriceGrabType command) {
		if (itemName == null) {
			return 0;
		}
		final String AVERAGE = "average", LOW = "recent_high", HIGH = "recent_low";
		String item = format(itemName), extracted;
		int price = 0;
		URLConnection connection = openStream(item);
		if (connection == null) {
			return -1;
		}
		extracted = retrieveData(connection, item);
		if (extracted == null) {
			return -1;
		}
		switch (command) {
		case MIN:
			return parseInfo(extracted, LOW);

		case AVERAGE:
			return parseInfo(extracted, AVERAGE);

		case MAX:
			return parseInfo(extracted, HIGH);
		}
		return price;
	}

	private static String format(final String string) {
		if (string.contains(" "))
			return string.replaceAll(" ", "+");
		else
			return string;
	}

	private URLConnection openStream(final String param) {
		String appended = ZYBEZ_URL.concat(param);
		try {
			URL zybez = new URL(appended);
			URLConnection urlConnection = zybez.openConnection();
			urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
			return urlConnection;
		} catch (MalformedURLException e) {
			System.out.println("Web address formatted incorrectly, printing stack trace");
			e.printStackTrace();
		} catch (IOException exception) {
			System.out.println("Url connection has thrown an IOException, printing stack trace");
			exception.printStackTrace();
		}
		return null;
	}

	private String retrieveData(URLConnection connection, final String param) {
		String output = null;
		BufferedReader inputScan = null;
		try {
			openStream(param);
			inputScan = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			output = inputScan.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				inputScan.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return output;
	}

	private int parseInfo(String extracted, String value) {
		int start, end, price = 0;
		if (extracted.contains(value)) {
			start = extracted.indexOf(value);
			end = extracted.indexOf(",", start);
			price = Integer.parseInt(extracted.substring(start, end).replaceFirst(".*?(\\d+).*", "$1"));
		} else
			System.out.println("Could not retrieve price");
		return price;
	}
}
