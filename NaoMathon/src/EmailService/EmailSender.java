package EmailService;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

/**
 * Created by Jocelyn on 17/06/2015.
 */
public class EmailSender {

  final String host = "smtp.gmail.com";
  final String Password = "epsinantesb3";
  final String from = "naomaton@gmail.com";

  public void sendEmail (String emailDestination) throws MessagingException {
    emailSender(emailDestination, null);
  }
  public void emailSender(String emailDestination, String filePath) throws MessagingException {
    // Get system properties
    Properties props = System.getProperties();
    props.put("mail.smtp.host", host);
    props.put("mail.smtps.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    Session session = Session.getInstance(props, null);

    //Email Creation
    MimeMessage message = new MimeMessage(session);
    message.setFrom(new InternetAddress(from));
    message.setRecipients(Message.RecipientType.TO, emailDestination);
    message.setSubject("NAOMathon: Votre photo");
    BodyPart messageBodyPart = new MimeBodyPart();

    String MailMessage = "<html> Bonjour, <br /> " +
      "Votre photo se trouve en pièce jointe. <br /><br />" +
      "Cordialement, <br />" +
      "AL Votre Photographe <br /> " +
      "<img src=\"http://img11.hostingpics.net/pics/176043nao.jpg\"> </html>";
    messageBodyPart.setContent(MailMessage, "text/html; charset=utf-8");

    Multipart multipart = new MimeMultipart();
    multipart.addBodyPart(messageBodyPart);

    if (filePath != null) {
      messageBodyPart = new MimeBodyPart();
      DataSource source = new FileDataSource(filePath);
      messageBodyPart.setDataHandler(new DataHandler(source));
      messageBodyPart.setFileName(filePath);
      multipart.addBodyPart(messageBodyPart);
    }
    message.setContent(multipart);
    //Envois
    try {
      Transport tr = session.getTransport("smtps");
      tr.connect(host, from, Password);
      tr.sendMessage(message, message.getAllRecipients());
      System.out.println("Mail Sent Successfully to" + emailDestination);
      tr.close();
    } catch (SendFailedException sfe) {
      System.out.println(sfe);
    }
  }
}
