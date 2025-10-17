package de.uniwue.apps.jm1.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import org.apache.tomcat.jdbc.pool.DataSource;

public class ConnectionPool {

	private final DataSource ds;

	protected ConnectionPool(final DataSource ds) {
		this.ds = ds;

		String url = null;
		{
			final Connection con = getConnection();
			try {
				final DatabaseMetaData dmd = con.getMetaData();
				url = dmd.getURL();
			} catch (final Exception e) {
				/* e.printStackTrace(); */
			} finally {
				if (con != null)
					try {
						con.close();
					} catch (final Exception ex) {}
			}
		}
		System.out.println("ConnectionPool.init " + (url == null ? "no Connection" : url));
	}

	public Connection getConnection() {
		Connection res = null;
		int i = 0;
		while (res == null && i < 1000)
			try {
				res = _getConnection();
			} catch (final Exception ex) {
				i++;
			}
		return res;
	}

	private Connection _getConnection() throws SQLException {
		if (this.ds != null)
			return this.ds.getConnection();
		else
			return null;
	}

}
