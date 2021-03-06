package toollabs.wikilist;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class WikiEntry {

	public final String dbname;

	public final String lang;

	public final String name;

	public final String family;

	public final String url;

	public boolean is_closed;

	public WikiEntry(ResultSet rs) throws SQLException {
		this.dbname = rs.getString("dbname");
		this.lang = rs.getString("lang");
		this.name = rs.getString("name");
		this.family = rs.getString("family");
		this.url = rs.getString("url");
		this.is_closed = rs.getInt("is_closed") != 0;
	}

	public JsonObject toJsonObject() {
		final JsonObjectBuilder builder = Json.createObjectBuilder();
		if (this.dbname != null) {
			builder.add("dbname", this.dbname);
		}
		if (this.lang != null) {
			builder.add("lang", this.lang);
		}
		if (this.name != null) {
			builder.add("name", this.name);
		}
		if (this.family != null) {
			builder.add("family", this.family);
		}
		if (this.url != null) {
			builder.add("url", this.url);
		}
		return builder.build();
	}

}
