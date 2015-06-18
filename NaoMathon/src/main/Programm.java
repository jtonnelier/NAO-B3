package main;

import java.util.concurrent.TimeUnit;
import com.aldebaran.qimessaging.*;
import com.aldebaran.qimessaging.Object;
import com.aldebaran.qimessaging.helpers.al.ALAudioPlayer;
import com.aldebaran.qimessaging.helpers.al.ALPhotoCapture;
import com.aldebaran.qimessaging.helpers.al.ALTextToSpeech;

/**
 * Created by Jocelyn on 02/06/2015.
 */
public class Programm {

  public static String NAO_IP = "172.16.6.117";
  public static int NAO_PORT = 9559;
  private static ALTextToSpeech tts;
  private static ALAudioPlayer audioService;
  private static ALPhotoCapture photoCapture;

  public static void main (String Args[]){
    try {
      Session session = new Session();
      Future<Void> future = session.connect("tcp://"+NAO_IP +":"+NAO_PORT);
      future.get();
      //Declare Variable Object
      tts = new ALTextToSpeech(session);
      audioService = new ALAudioPlayer(session);
      photoCapture = new ALPhotoCapture(session);

      tts.say("Test");
      audioService.playFile("/home/epsi3/Bureau/test/cri.wav");
      photoCapture.setPictureFormat("jpg");
      photoCapture.takePicture("/home/epsi3/Bureau/test", "photo");
      //Create Bundle Future
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
