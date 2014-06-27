package toollabs.wikilist;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Config {

	public static final String JDBC_DRIVERCLASS = "jdbc.driverclass";

	public static final String JDBC_URL = "jdbc.url";

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("toollabs.wikilist.config");

	public static String get(String key) throws MissingResourceException {
		return RESOURCE_BUNDLE.getString(key);
	}

}
