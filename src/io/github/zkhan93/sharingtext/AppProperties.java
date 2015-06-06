package io.github.zkhan93.sharingtext;

import io.github.zkhan93.sharingtext.contants.Constants;
import java.util.prefs.Preferences;

public class AppProperties {
	static Preferences pref = Preferences
			.userNodeForPackage(AppProperties.class);

	public static void set(String key, String value) {
		System.out.println("Some one want ot change port number to "+value);
		try {
			// set the properties value
			pref.put(key, value);

		} catch (Exception io) {
			io.printStackTrace();
		}
	}

	public static String get(String key) {
		System.out.println("Some one want to see port number");
		String res;
		try {

			if ((res = pref.get(key, String.valueOf(Constants.PORT))) == null) {
				// set default key

				res = String.valueOf(Constants.PORT);
				set(key, res);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			res = null;
		}
		return res;
	}

	public interface KEYS {
		String PORT = "port";
	}
}
