package de.uniwue.apps.jm1.db;

import java.util.ResourceBundle;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;

import org.apache.tomcat.jdbc.pool.DataSource;

public class ConnectionPoolFactory {

	public static ConnectionPool createConnectionPool(
			final String bundleName,
			final String dsPrefix) {

		final ResourceBundle b = ResourceBundle.getBundle(bundleName);
		
		DataSource ds = null;
		try {
			
			final String dsContext = b.getString(dsPrefix + "context");
			final String dsDatasource = b.getString(dsPrefix + "datasource");
			
			final Context initCtx = new InitialContext();
			final Context envCtx = (Context) initCtx.lookup(dsContext);
			ds = (DataSource) envCtx.lookup(dsDatasource);
			
		} catch (final Exception ex) {
			if (ex instanceof NameNotFoundException
					&& ex.getMessage().contains("Name jdbc is not bound in this Context")
				|| ex instanceof ClassCastException
					&& ex.getMessage().contains("org.apache.naming.NamingContext cannot be cast to javax.sql.DataSource"))
				warn("seems like you are not inside tomcat");
			else
				ex.printStackTrace();
		}
				
		return new ConnectionPool(ds);
	}
	
	private static void warn(final String message) {
		System.err.println("warning while setting up ConnectionPool: " + message);
	}

}
