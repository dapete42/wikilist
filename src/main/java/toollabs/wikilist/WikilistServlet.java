package toollabs.wikilist;

import java.beans.PropertyVetoException;
import java.io.IOException;
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

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class WikilistServlet extends HttpServlet {

	private static final long serialVersionUID = 2388469652008063655L;

	private ComboPooledDataSource pooledDataSource;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doRequest(req, resp);
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doRequest(req, resp);
	}

	private void doRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		final List<WikiEntry> wikiEntries = new ArrayList<>();

		try (final Connection connection = this.pooledDataSource.getConnection()) {

			// final PreparedStatement statement = connection.prepareStatement("SELECT * FROM wiki WHERE dbname=?");
			// statement.setString(1, "dewiki");
			final PreparedStatement statement = connection
					.prepareStatement("SELECT * FROM wiki WHERE is_closed=0 ORDER BY dbname");
			try (ResultSet rs = statement.executeQuery()) {
				while (rs.next()) {
					wikiEntries.add(new WikiEntry(rs));
				}
			}

		} catch (SQLException e) {
			throw new ServletException(e);
		}

		req.setAttribute("wikiEntries", wikiEntries);
		req.getRequestDispatcher("WEB-INF/wikilist.jsp").forward(req, resp);

	}

	@Override
	public void init(ServletConfig config) throws ServletException {

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
