package atk.cms.accounts;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import atk.cms.database.ConnectionPool;
import atk.cms.database.DatabaseUtility;

//Bean lives as long as the HTTP session lives
@ManagedBean(name="studentAccount")
@SessionScoped
public class StudentAccount extends Account {

	private ArrayList<String> courses;

	public StudentAccount() {
	}

	/**
	 * Get courses that Student is taking
	 * @return All courses Student is taking
	 * @throws SQLException 
	 */
	public ArrayList<String> studentCourses() throws SQLException {
	
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		String sql = "select coursename from courses";
	
		try {
			pstmt = connection.prepareStatement(sql);
			rs1 = pstmt.executeQuery();
			courses = new ArrayList<String>();
	
			while(rs1.next()) {
				sql = "select * from " + rs1.getString(1) + "course where studentusername = " + username;
				pstmt = connection.prepareStatement(sql);
				rs2 = pstmt.executeQuery();
	
				if(rs2.next()) {
					courses.add(rs1.getString(1));
				}
			}
		} catch (SQLException e) {
			System.out.println(e);
		} finally { 
			DatabaseUtility.closePreparedStatement(pstmt);
			DatabaseUtility.closeResultSet(rs1);
			DatabaseUtility.closeResultSet(rs2);
			pool.freeConnection(connection);
		}
		return courses;
	}
}
