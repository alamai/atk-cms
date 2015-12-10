package atk.cms.accounts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import atk.cms.accounts.Account;
import atk.cms.database.ConnectionPool;
import atk.cms.database.DatabaseSchemas;
import atk.cms.database.DatabaseUtility;

// Bean lives as long as the HTTP session lives
@ManagedBean(name="manageAccount")
@SessionScoped
public class ManageAccount {

	private int user_id;
	private String firstName;
	private String lastName;
	private String email;
	private boolean accountStatus;
	private String username;
	private String password;
	private String role;
	private String confirmUsername;
	private String confirmPassword;
	private ArrayList<String> selectStatus;
	private ArrayList<String> selectAccount;
	private List<Account> usersList;
	private ArrayList<String> useridList;

	public ManageAccount() throws SQLException {
		userAccounts();
	}
	
	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(boolean accountStatus) {
		this.accountStatus = accountStatus;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setConfirmUsername(String confirmUsername) {
		this.confirmUsername = confirmUsername;
	}

	public String getConfirmUsername() {
		return confirmUsername;
	}
	
	public void setSelectStatus(ArrayList<String> selectStatus) {
		this.selectStatus = selectStatus;
	}


	public ArrayList<String> getSelectStatus() {
		return selectStatus;
	}
	
	public void setSelectAccount(ArrayList<String> selectAccount) {
		this.selectAccount = selectAccount;
	}
	
	public ArrayList<String> getSelectAccount() {
		return selectAccount;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setConfirmPassword(String ConfirmPassword) {
		this.confirmPassword = ConfirmPassword;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getRole() {
		return role;
	}
	
	public void setUseridList(ArrayList<String> useridList) {
		this.useridList = useridList;
	}
	
	public ArrayList<String> getUseridList() {
		return useridList;
	}

	/**
	 * Get list of Users from UserAccounts 
	 * @return usersList
	 * @throws SQLException
	 */
	public List<Account> usersFromUserAccounts() throws SQLException {
	
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Account account = null;
		usersList = new ArrayList<Account>();
		String sql = "select * from UserAccounts order by role";
	
		try {
			pstmt = connection.prepareStatement(sql);
			rs = pstmt.executeQuery();
	
			while (rs.next()) {
				account = new Account();
				account.setFirstName(rs.getString("firstname"));
				account.setLastName(rs.getString("lastname"));
				account.setEmail(rs.getString("email"));
				account.setAccountStatus(rs.getBoolean("accountstatus"));
				account.setRole(rs.getString("role"));
				account.setUser_id(rs.getInt("user_id"));
				usersList.add(account);
			}
		} catch (SQLException e) {
			System.out.println(e);
		} finally {			
			DatabaseUtility.closePreparedStatement(pstmt);
			DatabaseUtility.closeResultSet(rs);
			pool.freeConnection(connection);
		}
		return usersList;
	}
	
	private ArrayList<String> userAccounts() throws SQLException {
		
		String sql = "select user_id, username from UserAccounts order by user_id";
		return useridList = DatabaseSchemas.idListFromTable(sql, useridList);
	}
	
	/**
	 * Check consistency of account fields before adding to UserAccounts
	 * @return Go-to web page
	 * @throws SQLException 
	 */
	public String registrationProcess() throws SQLException {

		String sql1 = "select * from UserAccounts where accountstatus = ? and username = ? and role = ?";
				
		String sql2 = "insert into UserAccounts (firstname, lastname, email, accountstatus, "
				+ "username, password, role) values (?, ?, ?, ?, ?, ?, ?)";

		if (!username.equals(confirmUsername) || !password.equals(confirmPassword) 
				|| !String.valueOf(accountStatus).equals("true")) {
			return "/Account/CreationErrorPage1.xhtml";
		}
		else if (DatabaseSchemas.isUserInUserAccounts(sql1, this.accountStatus, this.username, this.role)) {
			return "/Account/CreationErrorPage2.xhtml";
		}
		else if (DatabaseSchemas.isUserInUserAccounts(sql1, !this.accountStatus, this.username, this.role)) {
			return "/Account/CreationErrorPage3.xhtml";
		}
		else if (!DatabaseSchemas.isUserInUserAccounts(sql1, this.accountStatus, this.username, this.role)) {

			if (this.role.equals("student")) { 
				int rowsUpdated = DatabaseSchemas.insertUserIntoUserAccounts(sql2, this.firstName, 
						this.lastName, this.email, this.accountStatus, this.username, this.password, this.role);
				
				if (rowsUpdated == 1) {
					return "/Account/CreatedPage.xhtml";
				}
				else {
					return "/Account/CreationErrorPage4.xhtml";
				}
			}
			else if (this.role.equals("instructor")) {
				int rowsUpdated = DatabaseSchemas.insertUserIntoUserAccounts(sql2, this.firstName, 
						this.lastName, this.email, this.accountStatus, this.username, this.password, this.role);
				
				if (rowsUpdated == 1) {
					return "/Account/CreatedPage.xhtml";
				}
				else {
					return "/Account/CreationErrorPage4.xhtml";
				}
			}
			else {
				return "/Account/CreationErrorPage4.xhtml";
			}
		}
		else if (!DatabaseSchemas.isUserInUserAccounts(sql1, this.accountStatus, this.username, this.role)) {

			if (this.role.equals("student")) { 
				int rowsUpdated = DatabaseSchemas.insertUserIntoUserAccounts(sql2, this.firstName, 
						this.lastName, this.email, this.accountStatus, this.username, this.password, this.role);
				
				if (rowsUpdated == 1) {
					return "/Account/CreatedPage.xhtml";
				}
				else {
					return "/Account/CreationErrorPage4.xhtml";
				}
			}
			else if (this.role.equals("instructor")) {
				int rowsUpdated = DatabaseSchemas.insertUserIntoUserAccounts(sql2, this.firstName, 
						this.lastName, this.email, this.accountStatus, this.username, this.password, this.role);
				if (rowsUpdated == 1) {
					return "/Account/CreatedPage.xhtml";
				}
				else {
					return "/Account/CreationErrorPage4.xhtml";
				}
			}
			else {
				return "/Account/CreationErrorPage4.xhtml";
			}
		}
		else {
			return "/Account/CreationErrorPage4.xhtml";
		}
	}

	/**
	 * Edit and update account fields for:
	 * First Name, Last Name, Email, and Account Status 
	 * @return Page with updated accounts or error message
	 * @throws SQLException
	 */
	public String updateAccountProcess() throws SQLException {
	
		String sql1 = "update UserAccounts set firstname = ?, lastname = ?, email = ?, accountstatus = ? where user_id = ?";
		
		if (selectStatus.size() == 1 && selectAccount.size() == 1) {
	
			int rowsUpdated = DatabaseSchemas.updateUserInUserAccounts(sql1, this.firstName, 
					this.lastName, this.email, selectStatus, selectAccount);
		
			if (rowsUpdated == 1) {
				return "/Account/EditedPage.xhtml";
			}
			else {
				return "/Account/ReeditPage.xhtml";
			}
		}
		else {
			return "/Account/ReeditPage.xhtml";
		}
	}
}