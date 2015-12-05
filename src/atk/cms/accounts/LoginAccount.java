package atk.cms.accounts;

import java.io.Serializable;
import java.sql.SQLException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.servlet.http.HttpSession;

import atk.cms.accounts.ManageSession;
import atk.cms.database.DatabaseSchemas;

//Bean lives as long as the HTTP session lives
@ManagedBean(name="loginAccount")
@SessionScoped
public class LoginAccount implements Serializable {
	
	private boolean accountStatus;
	private String username;
	private String password;
	private String role;
	private static final long serialVersionUID = 1L;

	public LoginAccount() {
	}

	public void setAccountStatus(boolean accountStatus) {
		this.accountStatus = accountStatus;
	}

	public boolean isAccountStatus() {
		return accountStatus;
	}
	
	public void setUsername(String username) {
        this.username = username;
    }

	public String getUsername() {
		return username;
	}
	
	public void setPassword(String password) {
        this.password = password;
    }

	public String getPassword() {
		return password;
	}
	
	public void setRole(String role) {
        this.role = role;
    }

	public String getRole() {
		return role;
	}

	public String validateLogin() throws SQLException {

		String sql1 = "select * from AdministratorAccounts where accountstatus = ? and username = ? "
				+ "and password = ? and role = ?";

		String sql2 = "select * from UserAccounts where accountstatus = ? and username = ? "
				+ "and password = ? and role = ?";

		if (DatabaseSchemas.isAccountInDatabase(sql1, true, this.username, this.password, this.role)) {
			// Get HTTP session and store username, to be associated with Session ID
			HttpSession session = ManageSession.getSession();
			session.setAttribute("username", username);
			return "/Administrator/LandingPage.xhtml";
		}
		else if (DatabaseSchemas.isAccountInDatabase(sql2, true, this.username, this.password, this.role)) {
			// Get HTTP session and store username, to be associated with Session ID
			HttpSession session = ManageSession.getSession();
			session.setAttribute("username", username);

			if (role.equals("instructor")) {
				return "/Instructor/LandingPage.xhtml";
			}
			else if (role.equals("student")) {
				return "/Student/LandingPage.xhtml";
			}
			else {
				return "/LoginErrorPage.xhtml";
			}
		}
		else {
			return "/LoginErrorPage.xhtml";
		}
	}
	
	public String logout() {
		HttpSession session = ManageSession.getSession();
		session.invalidate();
		return "/LoginPage.xhtml";
	}
}