package DAO;

import java.sql.*;

/**
 * Created by Jocelyn on 16/06/2015.
 * Singleton de connexion à la BDD
 */
public class SGBDConnexion {

  /**
   * URL de connection
   */
  private static String url = "jdbc:mysql://sql4.freemysqlhosting.net:3306/sql480901";
  /**
   * Nom du user
   */
  private static String user = "sql480901";
  /**
   * Mot de passe du user
   */
  private static String passwd = "hW9*zG6*";
  /**
   * Objet Connection
   */
  private static Connection connect;

  /**
   * Méthode qui va nous retourner notre instance
   * et la créer si elle n'existe pas...
   * @return
   */

  public static Connection getInstance(){
    if(connect == null){
      try {
        Class.forName("com.mysql.jdbc.Driver");
        connect = DriverManager.getConnection(url, user, passwd);
        //getInformationConnectivity();
      } catch (SQLException e) {
        System.out.println("Erreur lors de la connexion à la BDD");
      } catch (ClassNotFoundException e) {
        System.out.println("Bug de drivers");
      }
    }
    return connect;
  }

  public static void getInformationConnectivity(){
    DatabaseMetaData md = null;
    try {
      md = connect.getMetaData();
      ResultSet rs = md.getTables(null, null, "%", null);
      while (rs.next()) {
        System.out.println(rs.getString(3));
      }
    } catch (SQLException e) {
      System.out.println("Connexion non valide");
    }
  }
}
