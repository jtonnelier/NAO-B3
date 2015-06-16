package DAO;

import java.sql.Connection;

/**
 * Created by Jocelyn on 16/06/2015.
 */
public abstract class AbstractDAO {

  public Connection connection;

  public AbstractDAO() throws ClassNotFoundException {
    Class.forName("com.mysql.jdbc.Driver");
    this.connection = SGBDConnexion.getInstance();
  }
}
