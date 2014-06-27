package toollabs.wikilist;

import java.sql.ResultSet;
import java.sql.SQLException;

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

}
