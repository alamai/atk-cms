package atk.cms.accounts;

/**
 * Class defines user accounts
 * Accounts must have: first name, last name, email, 
 * account status, username, password, and user role 
 */
public class Account {
	
	protected int user_id;
	protected String firstName;
	protected String lastName;
	protected String email;
	protected boolean accountStatus;
	protected String username;
	protected String password;
	protected String role;
	
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	
	public int getUser_id() {
		return user_id;
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
    
    public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
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
}

