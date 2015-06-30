package main;

import com.aldebaran.qimessaging.*;
import com.aldebaran.qimessaging.Object;
import com.aldebaran.qimessaging.helpers.al.ALLeds;
import com.aldebaran.qimessaging.helpers.al.ALMemory;
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
  public static Object memory;

  public void run(String[] args) throws Exception {

    try {
      String[] url = new String[1];
      url[0] = "tcp://" + NAO_IP + ":" + NAO_PORT;
      Application application = new Application(url);
      Session session = new Session();
      Future<Void> future = null;
      future = session.connect(url[0]);
      future.get();
      PhotoProgramm headProgramm = new PhotoProgramm(0, session);
      PhotoProgramm centralButtonProgramm = new PhotoProgramm(1, session);
      PhotoProgramm leftFeetProgramm = new PhotoProgramm(2, session);
      PhotoProgramm rightFeetProgramm = new PhotoProgramm(4, session);
      memory = session.service("ALMemory");
      Object headSubscriber = memory.<Object>call("subscriber", "MiddleTactilTouched").get();
      headSubscriber.connect("signal::(m)", "onTouch::(m)", headProgramm);
      Object rightFeetSubscriber = memory.<Object>call("subscriber", "RightBumperPressed").get();
      rightFeetSubscriber.connect("signal::(m)", "onTouch::(m)", rightFeetProgramm);
      application.run();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  public static void main (String Args[]) throws Exception {
    Programm programmePrincipal = new Programm();
    programmePrincipal.run(Args);
  }
}
