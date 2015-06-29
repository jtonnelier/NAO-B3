package main;

import com.aldebaran.qimessaging.*;
import com.aldebaran.qimessaging.helpers.al.ALLeds;
import com.aldebaran.qimessaging.helpers.al.ALMotion;
import com.aldebaran.qimessaging.helpers.al.ALRobotPosture;


/**
 * Created by Jocelyn on 02/06/2015.
 */
public class Programm {

  /**
   * Static connexion NAO
   */
  public static String NAO_IP = "172.16.6.117";
  public static int NAO_PORT = 9559;


  public static void main (String Args[]) throws InterruptedException, CallError {

    try {
      Session session = new Session();
      Future<Void> future = null;
      future = session.connect("tcp://" + NAO_IP + ":" + NAO_PORT);
      future.get();
      PhotoProgramm headProgramm = new PhotoProgramm(4, session);
      headProgramm.takePhoto();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
