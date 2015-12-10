package atk.cms.courses;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.sql.Connection;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import atk.cms.database.ConnectionPool;
import atk.cms.database.DatabaseSchemas;
import atk.cms.database.DatabaseUtility;

// Bean lives as long as the HTTP session lives
@ManagedBean(name="course")
@SessionScoped
public class Course {
	
	private int course_id;
	private int user_id;
	private int instructor_id;
	private int student_id;
	private String courseName;
	private String confirmCourseName;
	private boolean courseStatus;
	private boolean assignStatus;
	private List<Course> coursesList;
	private ArrayList<String> activeCoursesList;
	private ArrayList<String> instructorsList;	
	private ArrayList<String> studentsList;
	private ArrayList<String> selectCourse;
	private ArrayList<String> selectInstructor;
	private ArrayList<String> selectStudents;
	private ArrayList<Course> instructorCoursesList;
	private ArrayList<Course> studentCoursesList;
	private ArrayList<String> selectStatus;
	private ArrayList<String> courseidList;

	public Course() throws SQLException {
		activeCoursesFromCourses();
		instructorsListFromUserAccounts();
		studentsListFromUserAccounts();
		coursesCourses();
	}
	
	public void setCourse_id(int course_id) {
		this.course_id = course_id;
	}
	
	public int getCourse_id() {
		return course_id;
	}
	
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public int getUser_id() {
		return user_id;
	}
	
	public void setInstructor_id(int instructor_id) {
		this.instructor_id = instructor_id;
	}
	
	public int getInstructor_id() {
		return instructor_id;
	}
	
	public void setStudent_id(int student_id) {
		this.student_id = student_id;
	}
	
	public int getStudent_id() {
		return student_id;
	}
	
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	
	public String getCourseName() {
		return courseName;
	}
	
	public void setConfirmCourseName(String confirmCourseName) {
		this.confirmCourseName = confirmCourseName;
	}
	
	public String getConfirmCourseName() {
		return confirmCourseName;
	}

	public void setCourseStatus(boolean courseStatus) {
		this.courseStatus = courseStatus;
	}
	
	public boolean isCourseStatus() {
		return courseStatus;
	}
	
	public void setAssignStatus(boolean assignStatus) {
		this.assignStatus = assignStatus;
	}

	public boolean isAssignStatus() {
		return assignStatus;
	}

	public void setCoursesList(List<Course> coursesList) {
		this.coursesList = coursesList;
	}

	public List<Course> getCoursesList() {
		return coursesList;
	}
	
	public void setActiveCoursesList(ArrayList<String> activeCoursesList) {
		this.activeCoursesList = activeCoursesList;
	}
	
	public ArrayList<String> getActiveCoursesList() {
		return activeCoursesList;
	}

	public void setInstructorsList(ArrayList<String> instructorsList) {
		this.instructorsList = instructorsList;
	}
	
	public ArrayList<String> getInstructorsList() {
		return instructorsList;
	}
	
	public void setStudentsList(ArrayList<String> studentsList) {
		this.studentsList = studentsList;
	}

	public ArrayList<String> getStudentsList() {
		return studentsList;
	}

	public void setSelectCourse(ArrayList<String> selectCourse) {
		this.selectCourse = selectCourse;
	}
	
	public ArrayList<String> getSelectCourse() {
		return selectCourse;
	}

	public void setSelectInstructor(ArrayList<String> selectInstructor) {
		this.selectInstructor = selectInstructor;
	}
	
	public ArrayList<String> getSelectInstructor() {
		return selectInstructor;
	}

	public void setSelectStudents(ArrayList<String> selectStudents) {
		this.selectStudents = selectStudents;
	}
	
	public ArrayList<String> getSelectStudents() {
		return selectStudents;
	}
	
	public void setInstructorCoursesList(ArrayList<Course> instructorCoursesList) {
		this.instructorCoursesList = instructorCoursesList;
	}
	
	public ArrayList<Course> getInstructorCoursesList() {
		return instructorCoursesList;
	}
	
	public void setStudentCoursesList(ArrayList<Course> studentCoursesList) {
		this.studentCoursesList = studentCoursesList;
	}
	
	public ArrayList<Course> getStudentCoursesList() {
		return studentCoursesList;
	}

	public void setSelectStatus(ArrayList<String> selectStatus) {
		this.selectStatus = selectStatus;
	}

	public ArrayList<String> getSelectStatus() {
		return selectStatus;
	}

	public void setCourseidList(ArrayList<String> courseidList) {
		this.courseidList = courseidList;
	}

	public ArrayList<String> getCourseidList() {
		return courseidList;
	}

	/**
	 * Get list of ALL (active and inactive) Courses from Courses
	 * @return coursesList
	 * @throws SQLException
	 */
	public List<Course> coursesFromCourses() throws SQLException {

		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Course course = null;
		coursesList = new ArrayList<Course>();
		String sql = "select * from Courses order by course_id";

		try {
			pstmt = connection.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				course = new Course();
				course.setCourseName(rs.getString("coursename"));
				course.setCourse_id(rs.getInt("course_id"));
				course.setCourseStatus(rs.getBoolean("coursestatus"));
				coursesList.add(course);				
			}
		} catch (SQLException e) {
			System.out.println(e);
		} finally {			
			DatabaseUtility.closePreparedStatement(pstmt);
			DatabaseUtility.closeResultSet(rs);
			pool.freeConnection(connection);
		}
		return coursesList;
	}
	
	/**
	 * Get list of active Instructor course assignments
	 * @return instructorCoursesList
	 * @throws SQLException
	 */
	public List<Course> instructorCourses() throws SQLException {
	
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Course course = null;
		setInstructorCoursesList(new ArrayList<Course>());
		String sql = "select * from InstructorCourses order by user_id";
	
		try {
			pstmt = connection.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				course = new Course();
				course.setUser_id(rs.getInt("user_id"));
				course.setCourse_id(rs.getInt("course_id"));
				course.setAssignStatus(rs.getBoolean("assignstatus"));
				instructorCoursesList.add(course);				
			}
		} catch (SQLException e) {
			System.out.println(e);
		} finally {			
			DatabaseUtility.closePreparedStatement(pstmt);
			DatabaseUtility.closeResultSet(rs);
			pool.freeConnection(connection);
		}
		return instructorCoursesList;
	}

	/**
	 * Get list of active Student course assignments
	 * @return instructorCoursesList
	 * @throws SQLException
	 */
	public List<Course> studentCourses() throws SQLException {
	
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Course course = null;
		setStudentCoursesList(new ArrayList<Course>());
		String sql = "select * from StudentCourses order by user_id";
	
		try {
			pstmt = connection.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				course = new Course();
				course.setUser_id(rs.getInt("user_id"));
				course.setCourse_id(rs.getInt("course_id"));
				course.setAssignStatus(rs.getBoolean("assignstatus"));
				studentCoursesList.add(course);			
			}
		} catch (SQLException e) {
			System.out.println(e);
		} finally {			
			DatabaseUtility.closePreparedStatement(pstmt);
			DatabaseUtility.closeResultSet(rs);
			pool.freeConnection(connection);
		}
		return studentCoursesList;
	}

	/**
	 * Get list of active Courses & Course IDs from Courses
	 * @return activeCoursesList
	 * @throws SQLException
	 */
	private ArrayList<String> activeCoursesFromCourses() throws SQLException {

		String sql = "select course_id, coursename from Courses where courseStatus = 1";
		return activeCoursesList = DatabaseSchemas.idListFromTable(sql, activeCoursesList);
	}
	
	/**
	 * Get list of active Instructors & User IDs from UserAccounts
	 * @return instructorsList
	 * @throws SQLException
	 */
	private ArrayList<String> instructorsListFromUserAccounts() throws SQLException {

		String sql = "select user_id, username from UserAccounts where accountStatus = 1 and role = 'instructor'";
		return instructorsList = DatabaseSchemas.idListFromTable(sql, instructorsList);
	}

	/**
	 * Get list of active Students & User IDs from UserAccounts
	 * @return studentsList
	 * @throws SQLException
	 */
	private ArrayList<String> studentsListFromUserAccounts() throws SQLException {

		String sql = "select user_id, username from UserAccounts where accountStatus = 1 and role = 'student'";
		return studentsList = DatabaseSchemas.idListFromTable(sql, studentsList);
	}
	
	/**
	 * Get list of Courses & Course IDs from Courses
	 * @return courseidList
	 * @throws SQLException
	 */
	private ArrayList<String> coursesCourses() throws SQLException {
		
		String sql = "select course_id, coursename from Courses order by course_id";
		return courseidList = DatabaseSchemas.idListFromTable(sql, courseidList);
	}
	
	/**
	 * Check consistency of Course fields before adding to Courses table
	 * @return Go-to web page
	 * @throws SQLException 
	 */
	public String courseCreationProcess() throws SQLException {
			
		String sql1 = "select * from Courses where courseName = ? and courseStatus = ?";
					
		String sql2 = "insert into Courses (coursename, coursestatus) values (?, ?)";
		
		if (!courseName.equals(confirmCourseName) || !String.valueOf(courseStatus).equals("true")) {
			return "/Course/CreationErrorPage1";
		}
		else if (DatabaseSchemas.isCourseInCourses(sql1, this.courseName, this.courseStatus)) {
			return "/Course/CreationErrorPage2";
		} 
		else if (DatabaseSchemas.isCourseInCourses(sql1, this.courseName, !this.courseStatus)) {
			return "/Course/CreationErrorPage3";
		}
		else if (!DatabaseSchemas.isCourseInCourses(sql1, this.courseName, this.courseStatus)) {
			int rowsUpdated = DatabaseSchemas.insertCourseIntoCourses(sql2, this.courseName, this.courseStatus);
			DatabaseSchemas.createHwFolder(this.courseName);
			DatabaseSchemas.createTestsFolder(this.courseName);
			
			if (rowsUpdated == 1) {
				return "/Course/CreatedPage";
			}
			else {
				return "/Course/CreationErrorPage4";
			}
		}
		else {
			return "/Course/CreationErrorPage4";	
		}
	}
	
	/**
	 * Insert Instructors and Students into Courses and Gradebook, respectively 
	 * @return Go-to web page
	 * @throws SQLException 
	 */
	public String courseAssignmentProcess() throws SQLException { 
				
		String sql1 = "insert into InstructorCourses (user_id, course_id, assignstatus) values (?, ?, 1)";
		String sql2 = "insert into StudentCourses (user_id, course_id, assignstatus) values (?, ?, 1)";
		String sql3 = "insert into CourseGradebook (user_id, course_id) values (?, ?)";
		
		if (noRoleConflict(selectInstructor, selectStudents)) {
								
			if ((selectCourse.size() == 1) && (selectInstructor.size() == 1) && (selectStudents.size() >= 2) 
					&& (selectStudents.size() <= 25)) {
				assignInstructorStudentsToCourse(sql1, sql2, sql3, selectCourse, selectInstructor, selectStudents);
				return "/Course/AssignedPage";
			}
			else {
				return "/Course/ReassignPage";
			}	
		}
		else {
			return "/Course/AssignErrorPage";
		}
	}
	
	/**
	 * Check if Instructor is assigned as Student in same course 
	 * @param selectInstructor
	 * @param selectStudents
	 * @return TRUE if selected Instructor is NOT a Student in same course
	 */
	private boolean noRoleConflict(ArrayList<String> selectInstructor, ArrayList<String> selectStudents) {
	
		List<String> studentNameList = new ArrayList<String>();
		for (int i = 0; i < selectStudents.size(); i++) {
			String studentName = selectStudents.get(i).substring(selectStudents.get(i).lastIndexOf('-') + 1);
			studentNameList.add(studentName);
		}
		
		List<String> instructorNameList = new ArrayList<String>();
		for (int i = 0; i < selectInstructor.size(); i++) {
			String instructorName = selectInstructor.get(i).substring(selectInstructor.get(i).lastIndexOf('-') + 1);
			instructorNameList.add(instructorName);
		}
		
		Set<String> studentsNameSet = new HashSet<String>(studentNameList);
	    for (String instructorNameSet : instructorNameList) {
	        if (studentsNameSet.contains(instructorNameSet)) {
	        	return false;
	        }
	    }
		return true;
	}
	
	public boolean noAssignmentConflict() throws SQLException {
		
		String sql1 = "select * from InstructorCourses where user_id = ? and course_id = ?";
		String sql2 = "select * from StudentCourses where user_id = ? and course_id = ?";
		String sql3 = "select * from CourseGradebook where user_id = ? and course_id = ?";
	
		if (!DatabaseSchemas.isCourseAssigned(sql1, instructor_id, course_id) 
				&& !DatabaseSchemas.isCourseAssigned(sql2, student_id, course_id) 
				&& !DatabaseSchemas.isCourseAssigned(sql3, student_id, course_id)) {
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * Choose and Assign Instructors and Students to Course
	 * @return 
	 * @throws SQLException
	 */
	private void assignInstructorStudentsToCourse(String sql1, String sql2, String sql3, ArrayList<String> selectCourse, 
			ArrayList<String> selectInstructor, ArrayList<String> selectStudents) throws SQLException {

		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		PreparedStatement pstmt3 = null;

		try {
			String id = selectCourse.get(0).substring(0, 4);
			course_id = Integer.parseUnsignedInt(id);

			pstmt1 = connection.prepareStatement(sql1);
			for (int i = 0; i < selectInstructor.size(); i++) {
				String id1 = selectInstructor.get(i).substring(0, 4);
				int instructor_id = Integer.parseUnsignedInt(id1);
				setInstructor_id(instructor_id);
				
				pstmt1.setInt(1, instructor_id);
				pstmt1.setInt(2, course_id);
				pstmt1.executeUpdate();
			}

			pstmt2 = connection.prepareStatement(sql2);
			for (int i = 0; i < selectStudents.size(); i++) {
				String id2 = selectStudents.get(i).substring(0, 4);
				int student_id = Integer.parseUnsignedInt(id2);
				setStudent_id(student_id);
				
				pstmt2.setInt(1, student_id);
				pstmt2.setInt(2, course_id);
				pstmt2.executeUpdate();
			}
			
			pstmt3 = connection.prepareStatement(sql3);
			pstmt3.setInt(1, student_id);
			pstmt3.setInt(2, course_id);
			pstmt3.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e);
		} finally { 
			DatabaseUtility.closePreparedStatement(pstmt1);
			DatabaseUtility.closePreparedStatement(pstmt2);
			DatabaseUtility.closePreparedStatement(pstmt3);
			pool.freeConnection(connection);
		}
	}
	
	/**
	 * Edit and update course fields for:
	 * Course Name and Course Status 
	 * @return Page with updated courses or error message
	 * @throws SQLException
	 */
	public String updateCourseProcess() throws SQLException {

		String sql1 = "update Courses set coursename = ?, coursestatus = ? where course_id = ?";
		
		String sql2 = "update InstructorCourses set assignstatus = ? where course_id = ?";
		
		String sql3 = "update StudentCourses set assignstatus = ? where course_id = ?";
		
		if (selectStatus.size() == 1 && selectCourse.size() == 1) {

			int rowsUpdated1 = DatabaseSchemas.updateCourseInCourses(sql1, this.courseName, selectStatus, selectCourse);
			int rowsUpdated2 = DatabaseSchemas.updateCourseAssignmentStatus(sql2);
			int rowsUpdated3 = DatabaseSchemas.updateCourseAssignmentStatus(sql3);

			if ((rowsUpdated1 == 1) && (rowsUpdated2 == 1) && (rowsUpdated3 >= 1)) {
				return "/Course/EditedPage";
			}
			else {
				return "/Course/ReeditPage";
			}
		}
		else {
			return "/Course/ReeditPage";
		}
	}
}