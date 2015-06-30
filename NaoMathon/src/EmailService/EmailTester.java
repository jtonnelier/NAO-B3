package EmailService;

import javax.mail.MessagingException;

/**
 * Created by Jocelyn on 17/06/2015.
 */
public class EmailTester {
  public static void main(String Args[]){
    EmailSender emailService= new EmailSender();

    try {
      String filePath = "D:\\Users\\Jocelyn\\Pictures\\Memgen\\0wv00a.jpg";
      emailService.emailSender("jocelyn.tonnelier@gmail.com", filePath, false);
    } catch (MessagingException e) {
      System.out.println("Erreur lors de l'envois");
      e.printStackTrace();
    }
  }
}
