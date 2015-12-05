package atk.cms.database;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Creates a collection of "Connection Objects" stored in a "Database Connection Pool Object"
 * Connection objects in the connection pool are shared throughout the web application
 */
public class ConnectionPool {

	private static ConnectionPool pool = null;
	private static DataSource dataSource = null;
	private ConnectionPool() {
		
		try {
			System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.naming.java.javaURLContextFactory");
			System.setProperty("java.naming.provider.url", "localhost:3306");
			
			// Get initial context with references to all configurations and resources defined web application
			Context initCtx = new InitialContext();
			
			// Get Context for all environment naming (JNDI) - Resources configured for web application
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			
			// Get DataSource object mapping from Context
			dataSource = (DataSource) envCtx.lookup("jdbc/poolDB");
		} catch (NamingException e) {
			System.out.println(e);
		}
	}
	
	/**
	 * Returns ConnectionPool object
	 * @return pool
	 */
	public static synchronized ConnectionPool getInstance() {
		if (pool == null) {
			pool = new ConnectionPool();
		}
		return pool;
	}
	
	/**
	 * Returns Connection object from ConnectionPool to access Database
	 * @return connection
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException {
		try {
			Connection connection = dataSource.getConnection();
			return connection;
		} catch (SQLException e) {
			System.out.println(e);
			return null;
		}
	}

	/**
	 * Returns Connection object to ConnectionPool after accessing Database
	 * @param c
	 */
	public void freeConnection (Connection c) {
		try {
			c.close();
		} catch (SQLException e) {
			System.out.println(e);
		}
	}
}
