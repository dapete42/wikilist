package toollabs.wikilist;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MyCnfConfig {

	private final Log log = LogFactory.getLog(this.getClass());

	private static final String MY_CNF = "replica.my.cnf";

	private String password;

	private String user;

	public MyCnfConfig() throws IOException {

		File myCnfFile = new File(System.getProperty("user.home"), MY_CNF);

		Properties properties = new Properties();
		BufferedReader propertiesReader = new BufferedReader(new InputStreamReader(new FileInputStream(myCnfFile),
				StandardCharsets.UTF_8));
		properties.load(propertiesReader);

		this.user = properties.getProperty("user");
		if (this.user == null || this.user.isEmpty()) {
			log.error(String.format("Property '%s' missing or empty", "user"));
		}
		if (this.user.startsWith("'") && this.user.endsWith("'")) {
			this.user = this.user.substring(1, this.user.length() - 1);
		}

		this.password = properties.getProperty("password");
		if (this.password == null || this.password.isEmpty()) {
			log.error(String.format("Property '%s' missing or empty", "password"));
		}
		if (this.password.startsWith("'") && this.password.endsWith("'")) {
			this.password = this.password.substring(1, this.password.length() - 1);
		}

	}

	public String getPassword() {
		return this.password;
	}

	public String getUser() {
		return this.user;
	}

}
