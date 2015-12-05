package atk.cms.messages;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;

public class MailService {

	private static MailService atkMailService = null;
	private static Session mailSession;
	private static final String HOST = "smtp.d.umn.edu";
	private static final int PORT = 1025;
	private static final String USER = "";   
	private static final String PASSWORD = ""; 
	private static final String FROM = "";

	/**
	 * Sends message to recipient
	 * @param recipient Internet address
	 * @param message subject
	 * @param actual message 
	 * @throws MessagingException
	 */
	public static void sendMessage(String recipient, String subject, String message) throws MessagingException {

		if (atkMailService == null) {
			atkMailService = new MailService();
		}

		MimeMessage mimeMessage = new MimeMessage(mailSession);
		mimeMessage.setFrom(new InternetAddress(FROM));
		mimeMessage.setSender(new InternetAddress(FROM));
		mimeMessage.setSubject(subject);
		mimeMessage.setContent(message, "text/plain");

		mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));

		Transport transport = mailSession.getTransport("smtps");
		transport.connect(HOST, PORT, USER, PASSWORD);

		transport.sendMessage(mimeMessage, mimeMessage.getRecipients(Message.RecipientType.TO));
		transport.close();

	}

	private MailService() {
		
		Properties props = new Properties();
		props.put("mail.transport.protocol", "smtps");
		props.put("mail.smtps.host", HOST);
		props.put("mail.smtps.auth", "true");
		props.put("mail.smtp.from", FROM);
		props.put("mail.smtps.quitwait", "false");

		mailSession = Session.getDefaultInstance(props);
		mailSession.setDebug(true);
	}
}
