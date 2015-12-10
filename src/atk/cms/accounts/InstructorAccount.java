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
import java.util.List;
import java.util.ArrayList;
import javax.servlet.http.Part;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import atk.cms.courses.FileDownloadUtility;
import atk.cms.courses.FileUploadUtility;
import atk.cms.database.ConnectionPool;
import atk.cms.database.DatabaseSchemas;
import atk.cms.database.DatabaseUtility;

//Bean lives as long as the HTTP session lives
@ManagedBean(name="instructorAccount")
@SessionScoped
public class InstructorAccount extends Account {
	
	private String selectedCourse;
	private String selectedStudent;
	private Part testFile;
	private Part homeworkFile;
	private int homeworkNumber;
	private ArrayList<String> instructorCoursesList;
	private ArrayList<Integer> instructorStudentsList;
	private List<Account> instructorStudentsAccountsTable;
	
	public InstructorAccount() throws SQLException {
		instructorCourses();
		instructorStudents();
	}
	
	public void setSelectedCourse(String selectedCourse) {
		this.selectedCourse = selectedCourse;
	}

	public String getSelectedCourse() {
		return selectedCourse;
	}

	public void setSelectedStudent(String selectedStudent) {
		this.selectedStudent = selectedStudent;
	}

	public String getSelectedStudent() {
		return selectedStudent;
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
	
	public void setInstructorCoursesList(ArrayList<String> instructorCoursesList) {
		this.instructorCoursesList = instructorCoursesList;
	}
	
	public ArrayList<String> getInstructorCoursesList() {
		return instructorCoursesList;
	}
	
	public void setInstructorStudentsList(ArrayList<Integer> instructorStudentsList) {
		this.instructorStudentsList = instructorStudentsList;
	}
	
	public ArrayList<Integer> getInstructorStudentsList() {
		return instructorStudentsList;
	}

	public void setInstructorStudentsAccountsTable(List<Account> instructorStudentsAccountsTable) {
		this.instructorStudentsAccountsTable = instructorStudentsAccountsTable;
	}
	
	public List<Account> getInstructorStudentsAccountsTable() {
		return instructorStudentsAccountsTable;
	}
	
	public void instructorSelectedCourse(ValueChangeEvent e) {
		setSelectedCourse(e.getNewValue().toString());
	}

	public void instructorSelectedStudent(ValueChangeEvent e) {
		setSelectedStudent(e.getNewValue().toString());
	}
	
	public String instructorCoursePage() {
		
		if (!selectedCourse.equals("courselist")) { 
			return "/Instructor/CoursePage.xhtml";
		}
		else { 
			return "/Instructor/LandingPage.xhtml";
		}
	}
	
	public String assignGradesPage() {
		
		if (!selectedStudent.equals("studentlist")) { 
			return "/Instructor/AssignGradesPage.xhtml";
		}
		else { 
			return "/Instructor/CoursePage.xhtml";
		}
	}

	/**
	 * Get Courses Instructor is teaching
	 * @return instructorCoursesList
	 * @throws SQLException 
	 */
	public ArrayList<String> instructorCourses() throws SQLException {
		
		String sqljoin = "select distinct Courses.course_id, Courses.coursename, InstructorCourses.user_id "
				+ "from Courses "
				+ "join InstructorCourses "
				+ "on Courses.course_id = InstructorCourses.course_id "
				+ "where Courses.coursestatus = 1 "
				+ "and InstructorCourses.assignstatus = 1 "
				+ "and InstructorCourses.user_id = ?";
		
		user_id = DatabaseSchemas.getUser_id();
		return instructorCoursesList = 
				DatabaseSchemas.userCoursesInfo(sqljoin, user_id, instructorCoursesList);
	}
	
	/**
	 * Get Students Instructor is teaching
	 * @return instructorStudentsList
	 * @throws SQLException
	 */
	public ArrayList<Integer> instructorStudents() throws SQLException {
		
		String sqljoin = "select distinct StudentCourses.user_id, Courses.course_id "
				+ "from StudentCourses "
				+ "join Courses "
				+ "on Courses.course_id = StudentCourses.course_id "
				+ "join InstructorCourses "
				+ "on InstructorCourses.course_id = StudentCourses.course_id "
				+ "where StudentCourses.assignstatus = 1 "
				+ "and InstructorCourses.assignstatus = 1 "
				+ "and InstructorCourses.user_id = ? "
				+ "and Courses.coursename = ?";
		
		user_id = DatabaseSchemas.getUser_id();
		return instructorStudentsList = 
				DatabaseSchemas.studentsIdListFromCourses(sqljoin, user_id, selectedCourse, instructorStudentsList);
	}
	
	/**
	 * Get Students Instructor is teaching in table
	 * @return instructorStudentsAccountsTable
	 * @throws SQLException
	 */
	public List<Account> instructorStudentsTable() throws SQLException {
		
		String sqljoin = "select distinct UserAccounts.firstname, UserAccounts.lastname, UserAccounts.user_id, Courses.coursename "
				+ "from UserAccounts "
				+ "join Courses "
				+ "join StudentCourses "
				+ "on UserAccounts.user_id = StudentCourses.user_id "
				+ "join InstructorCourses "
				+ "on InstructorCourses.course_id = StudentCourses.course_id "
				+ "where StudentCourses.assignstatus = 1 "
				+ "and InstructorCourses.assignstatus = 1 "
				+ "and InstructorCourses.user_id = ? "
				+ "and Courses.coursename = ?";
		
		user_id = DatabaseSchemas.getUser_id();
		return instructorStudentsAccountsTable = 
				DatabaseSchemas.studentsInCourse(sqljoin, user_id, selectedCourse, instructorStudentsAccountsTable);
	}
	
	public String uploadTestFile() throws IOException {

		if (testFile.getSize() > 1) {
			
			String folderPathTests = System.getProperty("user.home") + File.separator + "Desktop" + File.separator 
					+ "CMSFiles" + File.separator + "AssignedTests" + File.separator + selectedCourse + File.separator;

			FileUploadUtility.uploadCourseFile(testFile, folderPathTests);

			File uploadedTestFile = new File(folderPathTests + FileUploadUtility.getFileName());

			if (uploadedTestFile.exists()) {
				return "/Instructor/CoursePageUploaded.xhtml";
			}
			else {
				return "/Instructor/CoursePageError1-1.xhtml";
			}
		}
		else {
			return "/Instructor/CoursePageError2.xhtml";
		}
	}
	
	public String uploadHomeworkFile() throws IOException {

		if (homeworkFile.getSize() > 1) {
			
			String folderPathHomework = System.getProperty("user.home") + File.separator + "Desktop" + File.separator 
					+ "CMSFiles" + File.separator + "AssignedHomework" + File.separator + selectedCourse + File.separator;

			FileUploadUtility.uploadCourseFile(homeworkFile, folderPathHomework);

			File uploadedHomeworkFile = new File(folderPathHomework + FileUploadUtility.getFileName());

			if (uploadedHomeworkFile.exists()) {
				return "/Instructor/CoursePageUploaded.xhtml";
			}
			else {
				return "/Instructor/CoursePageError1-2.xhtml";
			}
		}
		else {
			return "/Instructor/CoursePageError3.xhtml";
		}
	}
	
	public String downloadTestsTemplate() throws IOException {

		String folderPathTestsTemplate = System.getProperty("user.home") + File.separator + "Desktop" + File.separator 
				+ "CMSFiles" + File.separator + "TestsTemplate" + File.separator + "tests-template.xml";

		File testsTemplate = new File(folderPathTestsTemplate);

		if (testsTemplate.exists()) {

			FileDownloadUtility.downloadFile(folderPathTestsTemplate);
			return "/Instructor/CoursePageTemplate.xhtml";
		}
		else {
			return "/Instructor/CoursePageError4.xhtml";
		}
	}

	/**
	 * Assign homework to a specific course
	 * @param Course to assign homework to = "course"
	 * @param Homework = "assignedHomework"
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public void assignHomework(String course, String assignedHomework) throws IOException, SQLException {
		
		homeworkNumber = checkHomeworkAmount(course);
		updateHomeworkAmount(course, ++homeworkNumber);
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
	 * Append Homework to Homework table
	 * @throws SQLException 
	 */
	private void addHomeworkToHomework(String courseName) throws SQLException {

		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement pstmt = null;
		String sql = "alter table " + courseName + " add Hw" + homeworkNumber + " int";

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
	 * Set total amount of Homework for course
	 * @throws SQLException 
	 */
	private void updateHomeworkAmount(String courseName, int numberOfHw) throws SQLException {
	
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement pstmt = null;		
		String sql = "update courses set numHw = " + Integer.toString(numberOfHw) 
				+ " where coursename = \'" + courseName + "\'";
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
	 * Get total amount of Homework for course
	 * @return Total amount of Homework
	 * @throws SQLException 
	 */
	public int checkHomeworkAmount(String courseName) throws SQLException {

		int ret = 0;
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement pstmt = null;	
		ResultSet rs = null;
		String sql = "select numHw from courses where coursename = \'" + courseName + "\'";

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
	 * Set Student grade for specific Homework
	 * @throws SQLException 
	 */
	public void gradeHomework(int user_id, String courseName, String hw, String grade) throws SQLException {

		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement pstmt = null;	
		String sql = "update " + courseName + "course set " + hw + " = \'" + grade 
				+ "\' where user_id = \'" + user_id + "\'";
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
