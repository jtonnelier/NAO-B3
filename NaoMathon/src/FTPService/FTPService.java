package FTPService;

import it.sauronsoftware.ftp4j.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Jocelyn on 18/06/2015.
 */
public class FTPService {

  String server;
  int port = 21;
  String user = "nao";
  String pass = "HAL-2001";
  String imgFolderPath = "/home/epsi3/Bureau/";

  public FTPService(String server){
    this.server = server;
  }

  /**
   * Fonnction recupérant la photo sur la mémoire de NAO
   * @param filename
   * @return le path enregistré sur le disque
   */
  public String getImageFromNao(String ftpPath, String filename){
    FTPClient client = new FTPClient();
    FileInputStream fis = null;
    String completePath = ftpPath + "/" + filename;

    try {
      client.connect(server, port);
      client.login(user, pass);

      System.out.println("Connexion FTP établie...");
      client.changeDirectory("/home/ftpuser");
      File photo = new File(imgFolderPath+filename);
      client.download(filename, photo);

      // logout the user
      client.logout();
    } catch (IOException e) {
      System.out.println("Erreur lors de la récupération du fichier sur le FTP de NAO");
      System.out.println(e);
    } catch (FTPIllegalReplyException e) {
      System.out.println("Erreur lors de la connexion ftp");
      e.printStackTrace();
    } catch (FTPException e) {
      e.printStackTrace();
    } catch (FTPAbortedException e) {
      System.out.println("Erreur lors du transfert du fichier sur le FTP de NAO");
      e.printStackTrace();
    } catch (FTPDataTransferException e) {
      e.printStackTrace();
    }
    return this.imgFolderPath+filename;
  }
}
