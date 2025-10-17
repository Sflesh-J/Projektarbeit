package de.uniwue.apps.jm1.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DB {

	public final static ConnectionPool cp;
	
	static {
		cp = ConnectionPoolFactory.createConnectionPool("db", "datasource.");
	}
	
	private final static DateFormat DATETIME_FORMAT_PARSE = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public final static Date parse(final String s) {
		synchronized (DATETIME_FORMAT_PARSE) {
			try {
				return DATETIME_FORMAT_PARSE.parse(s);
			} catch (final Exception ex) {
				return null;
			}
		}
	}
	
	public final static synchronized String format(final Date d) {
		synchronized (DATETIME_FORMAT_PARSE) {
			return DATETIME_FORMAT_PARSE.format(d);
		}
	}
	
	public static void close(Connection con, ResultSet rs, Statement stmt) {
		if (rs != null)
			try {
				if (!rs.isClosed())
					rs.close();
			} catch (final Exception e) { /**/
			} catch (final Error e) { /* may be abstract */
			} finally {
				rs = null;
			}
		if (stmt != null)
			try {
				if (!stmt.isClosed())
					stmt.close();
			} catch (final Exception e) { /**/
			} catch (final Error e) { /* may be abstract */
			} finally {
				stmt = null;
			}
		if (con != null)
			try {
				if (!con.isClosed())
					con.close();
			} catch (final Exception e) { /**/
			} catch (final Error e) { /* may be abstract */
			} finally {
				con = null;
			}
	}


}
