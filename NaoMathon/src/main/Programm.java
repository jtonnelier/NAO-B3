package main;

import java.util.concurrent.TimeUnit;

import DAO.EmailDAO;
import DAO.PersonDTO;
import EmailService.EmailSender;
import FTPService.FTPService;
import com.aldebaran.qimessaging.*;
import com.aldebaran.qimessaging.Object;
import com.aldebaran.qimessaging.helpers.al.ALAudioPlayer;
import com.aldebaran.qimessaging.helpers.al.ALMemory;
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
  private static com.aldebaran.qimessaging.Object photoCapture;
  private static EmailSender emailService;
  private static FTPService ftpService = new FTPService("172.16.6.117");
  private static EmailDAO emailDAO;

  private static String folderPhoto = "recordings/cameras/";
  public static void main (String Args[]){
    try {
      Session session = new Session();
      Future<Void> future = session.connect("tcp://"+NAO_IP +":"+NAO_PORT);
      future.get();
      //Declare Variable Object
      tts = new ALTextToSpeech(session);
      audioService = new ALAudioPlayer(session);
      photoCapture = session.service("ALPhotoCapture");


      String photoName = "naoMathon"; //Add name from dao

      //audioService.playFile("/home/nao/recordings/cameras/cri.wav");
      //Photo Capture
      tts.say("Arttention je prends une photo");
      photoCapture.call("setPictureFormat", new java.lang.Object[]{"jpg"}).get();
      photoCapture.call("setResolution", new java.lang.Object[]{2}).get();
      photoCapture.call("takePicture", new java.lang.Object[]{"/home/nao/"+folderPhoto, photoName, true}).get();

      tts.say("Je t'envois la photo par email mon chou");
      String filePath = ftpService.getImageFromNao("recordings/cameras/", photoName +".jpg");
      PersonDTO person = emailDAO.getEmailFromName("Jocelyn");
      emailService.emailSender(person.getEmail(), filePath);
      tts.say("L'email est envoyé, et pour le plaisir");
      audioService.playFile("/home/nao/recordings/cameras/cri.wav");

      //Create Bundle Future
    } catch (Exception e) {
      System.out.println("Erreur lors de l'execution du programme");
    }
  }
}
