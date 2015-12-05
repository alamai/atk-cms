package atk.cms.messages;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMailJavaAPI {

	public static void main(String[] args) throws Exception {

		String to="recipient@example.com";
		String from="sender@example.com";

		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		String msgBody = "Sending email using JavaMail API...";

		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(from, "NoReply"));
			msg.addRecipient(Message.RecipientType.TO,
					new InternetAddress(to, "Mr. Recipient"));
			msg.setSubject("Welcome To Java Mail API");
			msg.setText(msgBody);
			Transport.send(msg);
			System.out.println("Email sent successfully...");
			
		} catch (AddressException e) {
			throw new RuntimeException(e);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}
