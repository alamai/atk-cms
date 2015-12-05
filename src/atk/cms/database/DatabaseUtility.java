package atk.cms.database;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

/**
 * Utility to close Database Objects:
 * PreparedStatement, ResultSet, and ResultSetMetaData
 */
public class DatabaseUtility {

	public static void closePreparedStatement(Statement pstmt) {
		try {
			if (pstmt != null) {
				pstmt.close();
			}
		} catch (SQLException e) {
			System.out.println(e);
		}
	}

	public static void closeResultSet(ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			System.out.println(e);
		}
	}
	
	public static void closeResultSetMetaData(ResultSetMetaData rsmd) {
		try {
			if (rsmd != null) {
				((Statement) rsmd).close();
			}
		} catch (SQLException e) {
			System.out.println(e);
		}
	}
}