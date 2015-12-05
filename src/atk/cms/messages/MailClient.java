package atk.cms.messages;

import javax.mail.MessagingException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 * Stores message information
 */
@ManagedBean(name="mailClient")
@RequestScoped
public class MailClient {
	
	private String recipient;
    private String subject;
    private String message;
    private String statusMessage = "";
	
	public void setMessage(String message) {
        this.message = message;
    }
	
	public String getMessage() {
        return message;
    }

	public void setRecipient(String recipient) {
        this.recipient = recipient;
    }
	
	public String getRecipient() {
        return recipient;
    }

	public void setSubject(String subject) {
        this.subject = subject;
    }
	
	public String getSubject() {
        return subject;
    }

    public String getStatusMessage() {
        return statusMessage;
    }
    
    public String send() {
        statusMessage = "Message Sent";
        
        try {
            MailService.sendMessage(recipient, subject, message);
        } catch(MessagingException ex) {
        	statusMessage = ex.getMessage();
        }
        return "MessageCompose";
    }
}
