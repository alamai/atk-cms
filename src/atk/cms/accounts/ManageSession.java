package atk.cms.accounts;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;

/**
 * Obtain and manage a session for every logged in user 
 * Done through getUserID method
 * Associates a session ID to a particular User ID
 * @return User-Session ID
 */
public class ManageSession {

	public static HttpSession getSession() {
		return (HttpSession)
				FacesContext.getCurrentInstance().getExternalContext().getSession(false);
	}

	public static HttpServletRequest getRequest() {
		return (HttpServletRequest) 
				FacesContext.getCurrentInstance().getExternalContext().getRequest();
	}

	public static String getUsername() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		return  session.getAttribute("username").toString();
	}
	
	public static String getUserId() {
		HttpSession session = getSession();

		if (session != null) {
			return (String) session.getAttribute("userid");
		}
		else {
			return null;
		}
	}
}