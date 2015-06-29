package DAO;

import java.sql.*;

/**
 * Created by Jocelyn on 16/06/2015.
 */
public class EmailDAO extends AbstractDAO{

  private final String GET_EMAIL_BY_NAME_QUERY = "SELECT Name, Email_adress FROM emails WHERE name = ?;";

  private final String GET_EMAIL_BY_ID_QUERY = "SELECT Name, Email_adress FROM emails WHERE ID = ?;";

  private final String GET_ALL_EMAILS_INFORMATION_QUERY = "SELECT * FROM emails";

  public EmailDAO() throws ClassNotFoundException {
  }

  public PersonDTO getEmailFromName(String name){
    PersonDTO person = null;
    try {
      PreparedStatement statement = connection.prepareStatement(GET_EMAIL_BY_NAME_QUERY);
      statement.setString(1, name);
      ResultSet resultSet = statement.executeQuery();

      String nameBDD = null;
      String email = null;
      if (resultSet.isBeforeFirst() ) {
        while (resultSet.next()) {
          nameBDD = resultSet.getString("Name");
          email = resultSet.getString("Email_adress");
        }
      }
      person = new PersonDTO(nameBDD, email);

    } catch (SQLException e) {
      System.out.println("Erreur lors de la récupération des emails");
      System.out.println(e);
    }
    return person;
  }

  public PersonDTO getEmailByID(int id){
    PersonDTO person = null;
    try {
      System.out.println("Debut getEmailByID");
      PreparedStatement statement = connection.prepareStatement(GET_EMAIL_BY_ID_QUERY);
      statement.setInt(1, id);
      ResultSet resultSet = statement.executeQuery();

      String nameBDD = null;
      String email = null;
      if (resultSet.isBeforeFirst() ) {
        while (resultSet.next()) {
          nameBDD = resultSet.getString("Name");
          email = resultSet.getString("Email_adress");
        }
      }
      person = new PersonDTO(nameBDD, email);
      System.out.println("Mail de " + person.getName() +" récupere: " + person.getEmail());
    } catch (SQLException e) {
      System.out.println("Erreur lors de la récupération des emails");
      System.out.println(e);
    }
    return person;
  }
}
