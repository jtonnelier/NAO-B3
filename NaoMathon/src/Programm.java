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
      Session session = new Session();
      session.connect("tcp://" + NAO_IP + ":9559").sync(500, TimeUnit.MILLISECONDS);
      Object tts = session.service("ALTextToSpeech");


      boolean ping = tts.<Boolean>call("ping").get();
      if (!ping) {
        System.out.println("Could not ping TTS");
      } else {
        System.out.println("Ping ok");
      }

      System.out.println("Calling say");
      tts.call("say", "Hello, world");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
