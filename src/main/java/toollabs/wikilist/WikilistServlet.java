package toollabs.wikilist;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public final class WikilistServlet extends HttpServlet {

	private static final long serialVersionUID = -518859421793443837L;

	private static final long DAY_MILLIS = 24 * 60 * 60 * 1000;

	private final Log log = LogFactory.getLog(this.getClass());

	private ComboPooledDataSource pooledDataSource;

	private final List<WikiEntry> wikiEntries = new ArrayList<>();

	private long wikiEntriesFilledTime = 0;

	@Override
	public void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException,
			IOException {
		this.doRequest(req, resp);
	}

	@Override
	public void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException,
			IOException {
		this.doRequest(req, resp);
	}

	private void doRequest(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException,
			IOException {

		final long nowTime = System.currentTimeMillis();

		// Fill wiki entries if older than 24 hours minutes or empty
		if (this.wikiEntries.isEmpty()) {
			this.log.info("Wiki entries empty, filling");
			this.fillWikiEntries();
		} else if (nowTime - this.wikiEntriesFilledTime > DAY_MILLIS) {
			this.log.info("Wiki entries older than 24 hours, refreshing");
			this.fillWikiEntries();
		}

		final String requestUrl = req.getRequestURL().toString();

		if (requestUrl.endsWith("json")) {

			final JSONArray json = new JSONArray();
			for (WikiEntry entry : this.wikiEntries) {
				json.put(entry.toJSONObject());
			}

			resp.setContentType("application/json; charset=UTF8");

			try (final OutputStreamWriter writer = new OutputStreamWriter(resp.getOutputStream(),
					StandardCharsets.UTF_8)) {
				json.write(writer);

			}

		} else {

			req.setAttribute("wikiEntries", this.wikiEntries);
			req.getRequestDispatcher("WEB-INF/wikilist.jsp").forward(req, resp);

		}

	}

	private void fillWikiEntries() throws ServletException {

		try (final Connection connection = this.pooledDataSource.getConnection()) {

			this.wikiEntries.clear();

			final PreparedStatement statement = connection
					.prepareStatement("SELECT * FROM wiki WHERE is_closed=0 ORDER BY dbname");

			try (ResultSet rs = statement.executeQuery()) {
				while (rs.next()) {
					this.wikiEntries.add(new WikiEntry(rs));
				}
			}

			wikiEntriesFilledTime = System.currentTimeMillis();

		} catch (SQLException e) {
			throw new ServletException(e);
		}

	}

	@Override
	public long getLastModified(final HttpServletRequest req) {

		final long nowTime = System.currentTimeMillis();

		super.getLastModified(req);

		if (this.wikiEntries.isEmpty()) {
			// No entries, never updated
			return -1;
		} else if (nowTime - this.wikiEntriesFilledTime > DAY_MILLIS) {
			// Entries are more than 24 hours old, claim to have been changed just now
			return nowTime;
		} else {
			return wikiEntriesFilledTime;
		}

	}

	@Override
	public void init(final ServletConfig config) throws ServletException {

		try {

			final MyCnfConfig myCnf = new MyCnfConfig();

			// Pool for database connections
			this.pooledDataSource = new ComboPooledDataSource();
			this.pooledDataSource.setJdbcUrl(Config.get(Config.JDBC_URL));
			// Fails for some reason unless explicitly set
			this.pooledDataSource.setDriverClass(Config.get(Config.JDBC_DRIVERCLASS));
			this.pooledDataSource.setUser(myCnf.getUser());
			this.pooledDataSource.setPassword(myCnf.getPassword());
			this.pooledDataSource.setInitialPoolSize(0);
			this.pooledDataSource.setMinPoolSize(0);
			this.pooledDataSource.setMaxPoolSize(10);
			this.pooledDataSource.setAcquireIncrement(1);
			this.pooledDataSource.setMaxIdleTime(600);
			this.pooledDataSource.setMaxConnectionAge(3600);
			this.pooledDataSource.setTestConnectionOnCheckin(true);
			this.pooledDataSource.setTestConnectionOnCheckout(true);

		} catch (IOException | PropertyVetoException e) {
			throw new ServletException(e);
		}

	}

}
