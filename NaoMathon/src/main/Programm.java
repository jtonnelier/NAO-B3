package main;

import java.util.concurrent.TimeUnit;
import com.aldebaran.qimessaging.*;
import com.aldebaran.qimessaging.Object;

/**
 * Created by Jocelyn on 02/06/2015.
 */
public class Programm {

  public static String NAO_IP = "hal.local";
  public static int NAO_PORT = 9559;

  public static void main (String Args[]){
    try {
      System.out.println("FONCTION ON");
      Session session = new Session();
      Future<Void> future = session.connect("tcp://hal.local:9559");
      future.get();
      //Declare Variable Object
      com.aldebaran.qimessaging.Object tts = null;
      com.aldebaran.qimessaging.Object bm = null;
      com.aldebaran.qimessaging.Object listen = null;
      tts = session.service("ALAudioPlayer");

      //Create Bundle Future


      boolean ping = tts.<Boolean>call("ping").get();
      if (!ping) {
        System.out.println("Could not ping TTS");
      } else {
        System.out.println("Ping ok");
      }

      System.out.println("Calling say");
      //tts.call("say", "Hello, world");
      tts.call("playFile", "//home//cri.mp3");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
