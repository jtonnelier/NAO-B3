package FTPService;

import org.apache.commons.net.ftp.FTPClient;

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
  String user = "";
  String pass = "";
  String imgFolderPath = "C:/";

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
      boolean login = client.login(user, pass);

      if (login) {
        System.out.println("Connexion FTP établie...");
        client.changeWorkingDirectory(ftpPath);
        File photo = new File(imgFolderPath+filename);
        FileOutputStream fos = new FileOutputStream(photo);
        photo.createNewFile();
        boolean download = client.retrieveFile(filename, fos);
        if (download) {
          System.out.println("Fichier download correctement !");
        } else {
          System.out.println("Fichier non retrouvé sur le FTP");
        }
        // logout the user, returned true if logout successfully
        boolean logout = client.logout();
        if (logout) {
          System.out.println("Connection close...");
        }
      } else {
        System.out.println("Connection fail...");
      };
    } catch (IOException e) {
      System.out.println("Erreur lors de la récupération du fichier sur le FTP de NAO");
      System.out.println(e);
    }
    return this.imgFolderPath+filename;
  }
}
