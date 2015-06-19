package DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Jocelyn on 19/06/2015.
 */
public class StatsDAO extends AbstractDAO {

  public static final String GET_COMPTEUR_TODAY = "SELECT compteurPhoto FROM statistiques where date = ?";

  public static final String INCREMENT_COMPTEUR = "INSERT INTO statistiques VALUES(?,0);";

  public static final String UPDATE_COMPTEUR = "UPDATE statistiques SET compteurPhoto=? WHERE date=?";

  public StatsDAO(){
  }
  
  public int getCompteur(){
    int compteur = 0;
    try {
      PreparedStatement statement = connection.prepareStatement(GET_COMPTEUR_TODAY);
      java.sql.Date sqlDate = getDatToday();
      statement.setDate(1, sqlDate);
      ResultSet resultSet = statement.executeQuery();
      if (!resultSet.next() ) {
        //On incremente le compteur
        PreparedStatement statementIncrementation = connection.prepareStatement(INCREMENT_COMPTEUR);
        statementIncrementation.setDate(1, sqlDate);
        statementIncrementation.executeUpdate();
        compteur = 0;
      }
      else{
        compteur = resultSet.getInt("compteurPhoto");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return compteur;
  }

  public void incrementCompteur(){

    try {
      int compteur = this.getCompteur();
      compteur+=1;
      PreparedStatement statement = connection.prepareStatement(UPDATE_COMPTEUR);
      java.sql.Date sqlDate = getDatToday();
      statement.setInt(1, compteur);
      statement.setDate(2, sqlDate);
      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private java.sql.Date getDatToday(){
    String format = "dd/MM/yy";
    java.text.SimpleDateFormat formater = new java.text.SimpleDateFormat( format );
    java.util.Date date = new java.util.Date();
    return new java.sql.Date(date.getTime());
  }
}
