package atk.cms.database;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.PreparedStatement;
import java.sql.DatabaseMetaData;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.security.NoSuchAlgorithmException;
import atk.cms.database.ConnectionPool;
import atk.cms.database.DatabaseUtility;
import atk.cms.database.PasswordUtility;

@ManagedBean(name="schemas")
@SessionScoped

public class DatabaseSchemas {
	
	private static int user_id;
	private static int course_id;
	private static String firstName;
	private static String lastName;
	private static String email;

	public static void setUser_id(int user_id) {
		DatabaseSchemas.user_id = user_id;
	}
	
	public static int getUser_id() {
		return user_id;
	}

	public static void setCourse_id(int course_id) {
		DatabaseSchemas.course_id = course_id;
	}

	public static int getCourse_id() {
		return course_id;
	}

	public static void setFirstName(String firstName) {
		DatabaseSchemas.firstName = firstName;
	}

	public static String getFirstName() {
		return firstName;
	}

	public static void setLastName(String lastName) {
		DatabaseSchemas.lastName = lastName;
	}

	public static String getLastName() {
		return lastName;
	}
	
	public static void setEmail(String email) {
		DatabaseSchemas.email = email;
	}

	public static String getEmail() {
		return email;
	}

	/**
	 * Check if table exists
	 * @return TRUE if table is already in database
	 * @throws SQLException
	 */
	public static boolean tableExists(String catalog, String schemaPattern, 
			String tableNamePattern, String[] types) throws SQLException {

		boolean ret = false;
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		DatabaseMetaData dbmeta = null;
		ResultSet rs = null;

		try {
			dbmeta = connection.getMetaData();
			rs = dbmeta.getTables(catalog, schemaPattern, tableNamePattern, types);
			
			if (rs.next()) {
				ret = true;
			}
		} catch (SQLException e) {
			System.out.println(e);
		} finally { 
			DatabaseUtility.closeResultSet(rs);
			pool.freeConnection(connection);
		}
		return ret;
	}
	
	/**
	 * Create table in database
	 * @return Number of affected rows
	 * @throws SQLException
	 */
	public static int createTable(String sql) throws SQLException {

		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement pstmt = null;

		try {
			pstmt = connection.prepareStatement(sql);
			return pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e);
			return 0;
		} finally { 
			DatabaseUtility.closePreparedStatement(pstmt);
			pool.freeConnection(connection);
		}
	}
	
	/**
	 * Alter created table
	 * @return Number of affected rows
	 * @throws SQLException
	 */
	public static int alterTable(String sql) throws SQLException {

		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement pstmt = null;

		try {
			pstmt = connection.prepareStatement(sql);
			return pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e);
			return 0;
		} finally { 
			DatabaseUtility.closePreparedStatement(pstmt);
			pool.freeConnection(connection);
		}
	}
	
	/**
	 * Check if User is in database
	 * @param Account Status, Username, Password, and Role
	 * @return TRUE if User is in database
	 * @throws SQLException
	 */
	public static boolean isAccountInDatabase(String sql, boolean accountStatus, String username, 
			String password, String role) throws SQLException {

		boolean ret = false;
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String hashedPassword;

		try {
			hashedPassword = PasswordUtility.hashPassword(password);
			pstmt = connection.prepareStatement(sql);
			pstmt.setBoolean(1, accountStatus);
			pstmt.setString(2, username);
			pstmt.setString(3, hashedPassword);
			pstmt.setString(4, role);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				InitializeAccount(rs);
				ret = true;
			}

		} catch (SQLException e) {
			System.out.println(e);
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e);
		} finally {
			DatabaseUtility.closePreparedStatement(pstmt);
			DatabaseUtility.closeResultSet(rs);
			pool.freeConnection(connection);
		}
		return ret;
	}
	
	/**
	 * Check if user is in UserAccounts before adding
	 * @return TRUE if user is already in UserAccounts
	 * @throws SQLException
	 */
	public static boolean isUserInUserAccounts(String sql, boolean accountStatus, String username, 
			String role) throws SQLException {
	
		boolean ret = false;
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;        
		
		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.setBoolean(1, accountStatus);
			pstmt.setString(2, username);
			pstmt.setString(3, role);
			rs = pstmt.executeQuery();
	
			if (rs.next()) {
				ret = true;
			}
		} catch (SQLException e) {
			System.out.println(e);
		} finally { 
			DatabaseUtility.closePreparedStatement(pstmt);
			DatabaseUtility.closeResultSet(rs);
			pool.freeConnection(connection);
		}
		return ret;
	}

	/**
	 * Check if user is in Gradebook before adding
	 * @return TRUE if user is already in Gradebook
	 * @throws SQLException
	 */
	public static boolean isUserInGradebook(String sql, int user_id) throws SQLException {
	
		boolean ret = false;
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;        
		
		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, user_id);
			rs = pstmt.executeQuery();
	
			if (rs.next()) {
				ret = true;
			}
		} catch (SQLException e) {
			System.out.println(e);
		} finally { 
			DatabaseUtility.closePreparedStatement(pstmt);
			DatabaseUtility.closeResultSet(rs);
			pool.freeConnection(connection);
		}
		return ret;
	}

	/**
	 * Check if Course is in Courses before adding
	 * @return TRUE if Course is already in Courses
	 * @throws SQLException
	 */
	public static boolean isCourseInCourses(String sql, String courseName, boolean accountStatus) throws SQLException {
	
		boolean ret = false;
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;        
		
		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, courseName);
			pstmt.setBoolean(2, accountStatus);
			rs = pstmt.executeQuery();
	
			if (rs.next()) {
				ret = true;
			}
		} catch (SQLException e) {
			System.out.println(e);
		} finally { 
			DatabaseUtility.closePreparedStatement(pstmt);
			DatabaseUtility.closeResultSet(rs);
			pool.freeConnection(connection);
		}
		return ret;
	}
	
	/**
	 * Check if Course is assigned to Instructor or Student
	 * @return TRUE if Course is already assigned
	 * @throws SQLException
	 */
	public static boolean isCourseAssigned(String sql, int user_id, int course_id) throws SQLException {
	
		boolean ret = false;
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;        
		
		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, user_id);
			pstmt.setInt(2, course_id);
			rs = pstmt.executeQuery();
	
			if (rs.next()) {
				ret = true;
			}
		} catch (SQLException e) {
			System.out.println(e);
		} finally { 
			DatabaseUtility.closePreparedStatement(pstmt);
			DatabaseUtility.closeResultSet(rs);
			pool.freeConnection(connection);
		}
		return ret;
	}

	private static void InitializeAccount(ResultSet rs) {
		
		try {
			user_id = rs.getInt(1);
			firstName = rs.getString(2);
			lastName = rs.getString(3);
			email = rs.getString(4);
		} catch (SQLException e) {
			System.out.println(e);
		}
	}
	
	/**
	 * Get list of IDs and Names from Database table
	 * @return idList
	 * @throws SQLException
	 */
	public static ArrayList<String> getIdListFromTable(String sql, ArrayList<String> idList) throws SQLException {

		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = connection.prepareStatement(sql);
			rs = pstmt.executeQuery();
			idList = new ArrayList<String>();
			
			while (rs.next()) {		
				idList.add(rs.getString(1) + " - " + rs.getString(2));
			}
		} catch (SQLException e) {
			System.out.println(e);
		} finally {			
			DatabaseUtility.closePreparedStatement(pstmt);
			DatabaseUtility.closeResultSet(rs);
			pool.freeConnection(connection);
		}
		return idList;
	}
	
	/**
	 * Get Courses Instructor is assigned to teach
	 * @return userCoursesList
	 * @throws SQLException 
	 */
	public static ArrayList<String> getUserCoursesInfo(String sqljoin, int user_id, 
			ArrayList<String> userCoursesInfoList) throws SQLException {

		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = connection.prepareStatement(sqljoin);
			pstmt.setInt(1, user_id);
			rs = pstmt.executeQuery();
			userCoursesInfoList = new ArrayList<String>();

			while (rs.next()) {
				userCoursesInfoList.add(rs.getString(2));
			}
		} catch (SQLException e) {
			System.out.println(e);
		} finally { 
			DatabaseUtility.closePreparedStatement(pstmt);
			DatabaseUtility.closeResultSet(rs);
			pool.freeConnection(connection);
		}
		return userCoursesInfoList;
	}

	/**
	 * Add User (Instructor or Student) into UserAccounts
	 * @return Number of affected rows
	 * @throws SQLException
	 */
	public static int insertUserIntoUserAccounts(String sql, String firstName, String lastName, String email, 
			boolean accountStatus, String username, String password, String role) throws SQLException {

		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement pstmt = null;
		String hashedPassword;

		try {
			hashedPassword = PasswordUtility.hashPassword(password);
			pstmt = connection.prepareStatement(sql);
    		pstmt.setString(1, firstName);
    		pstmt.setString(2, lastName);
    		pstmt.setString(3, email);
    		pstmt.setBoolean(4, accountStatus);
    		pstmt.setString(5, username);
    		pstmt.setString(6, hashedPassword);
    		pstmt.setString(7, role);
			return pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e);
			return 0;
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e);
			return 0;
		} finally { 
			DatabaseUtility.closePreparedStatement(pstmt);
			pool.freeConnection(connection);
		}
	}
	
	/**
	 * Add Student to Gradebook
	 * @return Number of affected rows
	 * @throws SQLException
	 */
	public static int insertUserIntoGradebook(String sql, String username, String role) throws SQLException {

		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement pstmt = null;

		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, username);
			pstmt.setString(2, role);
			return pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e);
			return 0;
		} finally { 
			DatabaseUtility.closePreparedStatement(pstmt);
			pool.freeConnection(connection);
		}
	}
	
	/**
	 * Add new Course to Courses table
	 * @return Number of affected rows
	 * @throws SQLException 
	 */
	public static int insertCourseIntoCourses(String sql, String courseName, boolean courseStatus) throws SQLException {
	
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement pstmt = null;
	
		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, courseName);
			pstmt.setBoolean(2, courseStatus);
			return pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e);
			return 0;
		} finally { 
			DatabaseUtility.closePreparedStatement(pstmt);
			pool.freeConnection(connection);
		}
	}

	/**
	 * Update user fields in UserAccounts
	 * Delete user by setting accountStatus field = (0 or false)
	 * @return Number of affected rows
	 * @throws SQLException
	 */
	public static int updateUserInUserAccounts(String sql, String firstName, String lastName, String email, 
			ArrayList<String> selectStatus, ArrayList<String> selectAccount) throws SQLException {

		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement pstmt = null;

		try {
			String status = selectStatus.get(0);
			boolean accountStatus = Boolean.parseBoolean(status);
			
			String id = selectAccount.get(0).substring(0, 4);
			int user_id = Integer.parseUnsignedInt(id);
			setUser_id(user_id);
			
			pstmt = connection.prepareStatement(sql);
    		pstmt.setString(1, firstName);
    		pstmt.setString(2, lastName);
    		pstmt.setString(3, email);
    		pstmt.setBoolean(4, accountStatus);
    		pstmt.setInt(5, user_id);
			return pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e);
			return 0;
		} finally { 
			DatabaseUtility.closePreparedStatement(pstmt);
			pool.freeConnection(connection);
		}
	}
	
	/**
	 * Update course fields in Courses
	 * Delete course by setting courseStatus field = (0 or false)
	 * @return Number of affected rows
	 * @throws SQLException
	 */
	public static int updateCourseInCourses(String sql, String courseName, 
			ArrayList<String> selectStatus, ArrayList<String> selectCourse) throws SQLException {

		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement pstmt = null;

		try {
			String status = selectStatus.get(0);
			boolean courseStatus = Boolean.parseBoolean(status);
			
			String id = selectCourse.get(0).substring(0, 4);
			int course_id = Integer.parseUnsignedInt(id);
			setCourse_id(course_id);
			
			pstmt = connection.prepareStatement(sql);
    		pstmt.setString(1, courseName);
    		pstmt.setBoolean(2, courseStatus);
    		pstmt.setInt(3, course_id);
			return pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e);
			return 0;
		} finally { 
			DatabaseUtility.closePreparedStatement(pstmt);
			pool.freeConnection(connection);
		}
	}
	
	/**
	 * Create Homework folder when Course is created
	 */
	public static void createHwFolder(String courseName) {
		
		File assignedHwFolder = new File(System.getProperty("user.home") + File.separator + "Desktop" + File.separator 
				+ "CMSFiles" + File.separator + "AssignedHomework" + File.separator + courseName);
		
		File submittedHwFolder = new File(System.getProperty("user.home") + File.separator + "Desktop" + File.separator 
				+ "CMSFiles" + File.separator + "SubmittedHomework" + File.separator + courseName);
		
		if (!assignedHwFolder.exists() && !submittedHwFolder.exists()) {
			assignedHwFolder.mkdir();
			submittedHwFolder.mkdir();
		}
		else if (!assignedHwFolder.exists() && submittedHwFolder.exists()) {
			assignedHwFolder.mkdir();
		}
		else if (assignedHwFolder.exists() && !submittedHwFolder.exists()) {
			submittedHwFolder.mkdir();
		}
	}
	
	/**
	 * Create Tests folder when Course is created
	 */
	public static void createTestsFolder(String courseName) {
		
		File assignedTestsFolder = new File(System.getProperty("user.home") + File.separator + "Desktop" + File.separator 
				+ "CMSFiles" + File.separator + "AssignedTests" + File.separator + courseName);
		
		if (!assignedTestsFolder.exists()) {
			assignedTestsFolder.mkdir();
		}
	}
}