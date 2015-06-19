package FTPService;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;

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
      // pass directory path on server to connect
      client.connect(server);

      // pass username and password, returned true if authentication is
      // successful
      boolean login = client.login(user, pass);

      if (login) {
        System.out.println("Connection established...");
        client.changeWorkingDirectory(ftpPath);
        client.setFileType(FTP.BINARY_FILE_TYPE);
        client.setFileTransferMode(FTP.BINARY_FILE_TYPE);
        FileOutputStream fos = null;
        fos = new FileOutputStream(imgFolderPath+filename);
        boolean download = client.retrieveFile(filename,
          fos);
        if (download) {
          System.out.println("File downloaded successfully");
        } else {
          System.out.println("Error in downloading file !");
        }
        // logout the user, returned true if logout successfully
        boolean logout = client.logout();
        if (logout) {
          System.out.println("Fermeture connexion ftp");
        }
      } else {
        System.out.println("Erreur lors du login");
      }

    } catch (SocketException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        client.disconnect();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return this.imgFolderPath+filename;
  }
}
