package DAO;

/**
 * Created by Jocelyn on 16/06/2015.
 */
public class PersonDTO {

  private String name;

  private String email;

  public PersonDTO(String name, String email){
      this.name = name;
      this.email = email;
  }

  public String getName(){
    return this.name;
  }

  public String getEmail(){
    return this.email;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
