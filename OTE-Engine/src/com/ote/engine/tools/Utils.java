package com.ote.engine.tools;

import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.ote.engine.kernel.atom.Atom;

public class Utils {
	private static final String PLAIN_ASCII = "AaEeIiOoUu" // grave
			+ "AaEeIiOoUuYy" // acute
			+ "AaEeIiOoUuYy" // circumflex
			+ "AaOoNn" // tilde
			+ "AaEeIiOoUuYy" // umlaut
			+ "Aa" // ring
			+ "Cc" // cedilla
			+ "OoUu" // double acute
	;

	private static final String UNICODE = "\u00C0\u00E0\u00C8\u00E8\u00CC\u00EC\u00D2\u00F2\u00D9\u00F9" + "\u00C1\u00E1\u00C9\u00E9\u00CD\u00ED\u00D3\u00F3\u00DA\u00FA\u00DD\u00FD" + "\u00C2\u00E2\u00CA\u00EA\u00CE\u00EE\u00D4\u00F4\u00DB\u00FB\u0176\u0177" + "\u00C3\u00E3\u00D5\u00F5\u00D1\u00F1" + "\u00C4\u00E4\u00CB\u00EB\u00CF\u00EF\u00D6\u00F6\u00DC\u00FC\u0178\u00FF"
			+ "\u00C5\u00E5" + "\u00C7\u00E7" + "\u0150\u0151\u0170\u0171";

	public static final String normalizeFilename(String name) {
		String filenameOrigin = name;

		int i = filenameOrigin.lastIndexOf("\\");
		if (i > 0) {
			filenameOrigin = filenameOrigin.substring(i + 1, filenameOrigin.length());
		}

		i = filenameOrigin.lastIndexOf("/");
		if (i > 0) {
			filenameOrigin = filenameOrigin.substring(i + 1, filenameOrigin.length());
		}

		String s1 = Normalizer.normalize(filenameOrigin, java.text.Normalizer.Form.NFD);
		s1 = s1.replaceAll("[^\\.A-Za-z0-9]", "_");
		// s1=s1.replaceAll("[^\\p{ASCII}]","").replaceAll(" ","_");

		return s1;
	}

	public static String convertNonAscii(String s) {
		if (s == null)
			return null;
		StringBuilder sb = new StringBuilder();
		int n = s.length();
		for (int i = 0; i < n; i++) {
			char c = s.charAt(i);
			int pos = UNICODE.indexOf(c);
			if (pos > -1) {
				sb.append(PLAIN_ASCII.charAt(pos));
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	// URI
	public static String getStringForURI(String origin) {
		String tmp = origin;
		tmp = tmp.toUpperCase(Locale.ENGLISH).toLowerCase(Locale.ENGLISH).replaceAll(" ", "_").replaceAll("'", "-").replaceAll("<", "").replaceAll(">", "").replaceAll("/n", "_").replaceAll("\\.", "").replaceAll("\\?", "").replaceAll("\"", "");
		tmp = java.text.Normalizer.normalize(tmp, java.text.Normalizer.Form.NFD);
		tmp = tmp.replaceAll("[^a-zA-Z0-9_]", "");
		return tmp;
	}

	// DATES
	public static String getCurrentDateInString() {
		return getDateInString(new Date());
	}

	public static String getDateInString(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date.getTime());
	}

	public static Date getDateFromString(String value) {
		if (value == null)
			return null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = sdf.parse(value);
		} catch (ParseException e) {

		}

		if (date == null) {
			sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
			try {
				date = sdf.parse(value);
			} catch (ParseException e) {

			}
		}
		return date;
	}

	public static String getLastDate(Atom elt1, String date2) {
		String date1 = null;

		if (elt1 != null) {
			if (elt1.getModified() != null)
				date1 = elt1.getModified();
			else
				date1 = elt1.getCreated();
		}

		return getLastDate(date1, date2);
	}

	public static String getLastDate(Atom elt1, Atom elt2) {
		String date1 = null;
		String date2 = null;

		if (elt1 != null) {
			if (elt1.getModified() != null)
				date1 = elt1.getModified();
			else
				date1 = elt1.getCreated();
		}

		if (elt2 != null) {
			if (elt2.getModified() != null)
				date2 = elt2.getModified();
			else
				date2 = elt2.getCreated();
		}

		return getLastDate(date1, date2);

	}

	public static String getLastDate(String date1, String date2) {
		if (date1 == null)
			return date2;
		if (date2 == null)
			return date1;

		Date d1 = getDateFromString(date1);
		Date d2 = getDateFromString(date2);

		if (d1.compareTo(d2) > 0) {
			return date1;
		} else
			return date2;
	}

}
