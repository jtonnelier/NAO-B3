package FTPService;

import it.sauronsoftware.ftp4j.*;

import java.io.*;
import java.net.SocketException;
import java.util.Random;

/**
 * Created by Jocelyn on 18/06/2015.
 */
public class FTPService {

  String server;
  int port = 21;
  String user = "nao";
  String pass = "HAL-2001";
  String imgFolderPath = "/var/www/naomathon-gallery/gallery-images/";

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
    String downloadFileName = "";

    try {
      // pass directory path on server to connect
      client.connect(server);
      client.login(user, pass);

      System.out.println("Connection au FTP de Nao etablie...");
      client.changeDirectory(ftpPath);
      downloadFileName = this.generateRandomFileName();
      client.download(filename, new java.io.File(imgFolderPath+downloadFileName));
      System.out.println("Fichier telechargé: " + imgFolderPath+downloadFileName);
      client.deleteFile(filename);

      // logout the user, returned true if logout successfully
      client.logout();

    } catch (FTPException e1) {
      System.out.println("Fonction non problematique");
      e1.printStackTrace();
    } catch (FileNotFoundException e1) {
      e1.printStackTrace();
    } catch (IOException e1) {
      e1.printStackTrace();
    } catch (FTPIllegalReplyException e1) {
      e1.printStackTrace();
    } catch (FTPAbortedException e1) {
      e1.printStackTrace();
    } catch (FTPDataTransferException e1) {
      e1.printStackTrace();
    }
    return this.imgFolderPath+downloadFileName;
  }

  /*
   * Fontion generant un nom de fichier
   * random
   */
  public String generateRandomFileName(){
    char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    StringBuilder sb = new StringBuilder();
    Random random = new Random();
    for (int i = 0; i < 20; i++) {
      char c = chars[random.nextInt(chars.length)];
      sb.append(c);
    }
    return sb.toString();
  }
}
