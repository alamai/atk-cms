package atk.cms.accounts;


import java.io.File;
import java.io.Writer;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.http.Part;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import atk.cms.courses.FileUploadUtility;
import atk.cms.database.ConnectionPool;
import atk.cms.database.DatabaseSchemas;
import atk.cms.database.DatabaseUtility;

//Bean lives as long as the HTTP session lives
@ManagedBean(name="instructorAccount")
@SessionScoped
public class InstructorAccount extends Account {
	
	private Part testFile;
	private Part homeworkFile;
	private int homeworkNumber;
	private String selectedCourse;
	private ArrayList<String> instructorCoursesList;
	private ArrayList<String> instructorStudentsList;
	
	private String folderPathTests = System.getProperty("user.home") + File.separator + "Desktop" + File.separator 
    		+ "CMSFiles" + File.separator + "AssignedTests" + File.separator + selectedCourse;
	
	private String folderPathHomework = System.getProperty("user.home") + File.separator + "Desktop" + File.separator 
    		+ "CMSFiles" + File.separator + "AssignedHomework" + File.separator + selectedCourse;
	
	public InstructorAccount() throws SQLException {
		getInstructorCourses();
		getInstructorStudents();
	}

	public void setTestFile(Part testFile) {
		this.testFile = testFile;
	}

	public Part getTestFile() {
		return testFile;
	}

	public void setHomeworkFile(Part homeworkFile) {
		this.homeworkFile = homeworkFile;
	}
	
	public Part getHomeworkFile() {
		return homeworkFile;
	}

	public void setSelectedCourse(String selectedCourse) {
		this.selectedCourse = selectedCourse;
	}

	public String getSelectedCourse() {
		return selectedCourse;
	}
	
	public void setInstructorCoursesList(ArrayList<String> instructorCoursesList) {
		this.instructorCoursesList = instructorCoursesList;
	}
	
	public ArrayList<String> getInstructorCoursesList() {
		return instructorCoursesList;
	}
	
	public void setInstructorStudentsList(ArrayList<String> instructorStudentsList) {
		this.instructorStudentsList = instructorStudentsList;
	}
	
	public ArrayList<String> getInstructorStudentsList() {
		return instructorStudentsList;
	}
	
	public void instructorSelectedCourse(ValueChangeEvent e) {
		selectedCourse = e.getNewValue().toString();
	}
	
	public String instructorCoursePage() {
				
		if (!selectedCourse.equals("courselist")) { 
			return "/Instructor/CoursePage.xhtml";
		}
		else { 
			return "/Instructor/LandingPage.xhtml";
		}
	}

	/**
	 * Get Courses Instructor is teaching
	 * @return instructorCoursesList
	 * @throws SQLException 
	 */
	private ArrayList<String> getInstructorCourses() throws SQLException {
		
		String sqljoin = "select Courses.course_id, Courses.coursename, InstructorCourses.user_id "
				+ "from Courses "
				+ "inner join InstructorCourses "
				+ "on Courses.course_id = InstructorCourses.course_id "
				+ "where Courses.coursestatus = 1 "
				+ "and InstructorCourses.assignstatus = 1 "
				+ "and InstructorCourses.user_id = ?";
		
		user_id = DatabaseSchemas.getUser_id();
		return instructorCoursesList = DatabaseSchemas.getUserCoursesInfo(sqljoin, user_id, instructorCoursesList);
	}
	
	/**
	 * Get Students Instructor is teaching
	 * @return instructorStudentsList
	 * @throws SQLException
	 */
	private ArrayList<String> getInstructorStudents() throws SQLException {
		
		String sqljoin = "select Courses.course_id, Courses.coursename, InstructorCourses.user_id, UserAccounts.username "
				+ "from Courses, InstructorCourses, UserAccounts "
				+ "where Courses.course_id = InstructorCourses.course_id "
				+ "and UserAccounts.user_id = InstructorCourses.user_id "
				+ "and Courses.coursestatus = 1 "
				+ "and InstructorCourses.assignstatus = 1 "
				+ "and UserAccounts.user_id = ?";
		
		user_id = DatabaseSchemas.getUser_id();
		return instructorStudentsList = DatabaseSchemas.getUserCoursesInfo(sqljoin, user_id, instructorStudentsList);
	}
	
	public String uploadTestFile() throws IOException {
		
		FileUploadUtility.uploadCourseFile(testFile, folderPathTests);
		return "/Instructor/CoursePage.xhtml";
	}
	
	public String uploadHomeworkFile() throws IOException {
		
		FileUploadUtility.uploadCourseFile(homeworkFile, folderPathHomework);
		return "/Instructor/CoursePage.xhtml";
	}

	/**
	 * Assign homework to a specific course
	 * @param Course to assign homework to = "course"
	 * @param Homework = "assignedHomework"
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public void assignHomework(String course, String assignedHomework) throws IOException, SQLException {
		
		homeworkNumber = getHomeworkAmount(course);
		setHomeworkAmount(course, ++homeworkNumber);
		addHomeworkToHomework(course);
		Writer output = null;
		File file = new File(System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "CMSFiles" 
				+ File.separator + "AssignedHomework" + File.separator + course + File.separator + "HW" + homeworkNumber + ".txt");
		try {
			output = new BufferedWriter(new FileWriter(file));
			output.write(assignedHomework);
		} catch (IOException ex) {
			System.out.println(ex);		
		} finally { 
			output.close();
		} 
	}

	/**
	 * Append homework assignment to course table
	 * @param Course to add homework to = "course"
	 * @throws SQLException 
	 */
	private void addHomeworkToHomework(String course) throws SQLException {

		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement pstmt = null;
		String sql = "alter table " + course + " add Hw" + homeworkNumber + " int";

		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e);		
		} finally { 
			DatabaseUtility.closePreparedStatement(pstmt);
			pool.freeConnection(connection);
		}
	}

	/**
	 * Set total amount of homework for course
	 * @param Course to set amount of homework for = "course"
	 * @param Amount of homework assigned = "numberOfHw"
	 * @throws SQLException 
	 */
	private void setHomeworkAmount(String course, int numberOfHw) throws SQLException {
	
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement pstmt = null;		
		String sql = "update courses set numHw = " + Integer.toString(numberOfHw) 
				+ " where coursename = \'" + course + "\'";
		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e);		
		} finally { 
			DatabaseUtility.closePreparedStatement(pstmt);
			pool.freeConnection(connection);
		}	 
	}

	/**
	 * Get total amount of homework for course
	 * @param Course to find total amount of homework for = "course"
	 * @return Total amount of homework
	 * @throws SQLException 
	 */
	public int getHomeworkAmount(String course) throws SQLException {

		int ret = 0;
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement pstmt = null;	
		ResultSet rs = null;
		String sql = "select numHw from courses where coursename = \'" + course + "\'";

		try {
			pstmt = connection.prepareStatement(sql);
			rs = pstmt.executeQuery();

			if(rs.next()) {
				ret = rs.getInt("numHw");
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
	 * Set Student grade for specific homework assignment
	 * @param Student to look for = "student"
	 * @param Course enrolled in = "course"
	 * @param Homework number = "hw"
	 * @param Grade for homework = "grade"
	 * @throws SQLException 
	 */
	public void gradeHomework(String student, String course, String hw, String grade) throws SQLException {

		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement pstmt = null;	
		String sql = "update " + course + "course set " + hw + " = \'" + grade 
				+ "\' where studentusername = \'" + student + "\'";
		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e);		
		} finally { 
			DatabaseUtility.closePreparedStatement(pstmt);
			pool.freeConnection(connection);
		}
	}
}
