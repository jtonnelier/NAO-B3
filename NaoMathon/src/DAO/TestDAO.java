package DAO;

/**
 * Created by Jocelyn on 16/06/2015.
 */
public class TestDAO {

  public static void main (String Args[]) {
    EmailDAO emailDao = null;
    try {
      emailDao = new EmailDAO();
      PersonDTO person = null;
      person = emailDao.getEmailFromName("Jocelyn");
      if(person != null){
        System.out.println(person.getName());
      }
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }
}
