package DAO;

import java.sql.*;

/**
 * Created by Jocelyn on 16/06/2015.
 */
public class EmailDAO extends AbstractDAO{

  private final String GET_EMAIL_BY_NAME_QUERY = "SELECT Name, Email_adress FROM emails WHERE name = ?;";

  private final String GET_ALL_EMAILS_INFORMATION = "SELECT * FROM emails";

  public EmailDAO() throws ClassNotFoundException {
    super();
  }

  public PersonDTO getEmailFromName(String name){
    PersonDTO person = null;
    try {
      PreparedStatement statement = connection.prepareStatement("select * from emails");
      //statement.setString(1, name);
      ResultSet resultSet = statement.executeQuery(this.GET_EMAIL_BY_NAME_QUERY);
      System.out.println(resultSet.getFetchSize());
      if(resultSet.getFetchSize() !=0) {
        while (resultSet.next()) {
          person.setName(resultSet.getString("Name"));
          person.setName(resultSet.getString("Email_adress"));
        }
      }
      resultSet.close();
      statement.close();
    } catch (SQLException e) {
      System.out.println("Erreur lors de la récupération des emails");
      System.out.println(e);
    }
    return person;
  }
}
