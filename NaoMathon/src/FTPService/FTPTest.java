package FTPService;

/**
 * Created by Jocelyn on 18/06/2015.
 */
public class FTPTest {

  public static void main(String Args[]){
    FTPService ftpService = new FTPService("172.16.6.117");

    ftpService.getImageFromNao("/recording/cameras", "photo.jpg");
  }
}
