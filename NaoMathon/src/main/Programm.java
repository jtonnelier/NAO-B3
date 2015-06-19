package main;

import java.util.concurrent.TimeUnit;

import DAO.EmailDAO;
import DAO.PersonDTO;
import DAO.StatsDAO;
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

  /**
   * Static connexion NAO
   */
  public static String NAO_IP = "172.16.6.117";
  public static int NAO_PORT = 9559;
  /**
   * NAO utils
   */
  private static ALTextToSpeech tts;
  private static ALAudioPlayer audioService;
  private static com.aldebaran.qimessaging.Object photoCapture;
  /**
   * Service connexion
   */
  private static EmailSender emailService;
  private static FTPService ftpService = new FTPService("172.16.6.117");
  private static EmailDAO emailDAO;
  private static StatsDAO statsDAO;

  private static String folderPhoto = "recordings/cameras/naomathon/";
  public static void main (String Args[]){
    try {
      Session session = new Session();
      Future<Void> future = session.connect("tcp://"+NAO_IP +":"+NAO_PORT);
      future.get();
      //Declare Variable Object
      tts = new ALTextToSpeech(session);
      audioService = new ALAudioPlayer(session);
      photoCapture = session.service("ALPhotoCapture");
      emailDAO = new EmailDAO();
      emailService = new EmailSender();
      statsDAO = new StatsDAO();
      ALMemory memoryProxy = new ALMemory(session);

      String photoName = "naoMathon"; //Add name from dao
        tts.setVolume((float) 1.0);
        tts.say("Sourié je prends la photo");
        photoCapture.call("setPictureFormat", new java.lang.Object[]{"jpg"}).get();
        photoCapture.call("setResolution", new java.lang.Object[]{2}).get();
        photoCapture.call("takePicture", new java.lang.Object[]{"/home/nao/" + folderPhoto, photoName, true}).get();
        audioService.playFile("/home/nao/recordings/cameras/naomathon/flash.wav");
        int compteur = statsDAO.getCompteur();

        if(compteur%3 == 0 ){
          tts.say("La photo est raté, j'envois un email");
          PersonDTO person = emailDAO.getEmailFromName("Jocelyn");
          emailService.emailSender(person.getEmail(), null, true);
          tts.say("L'email est envoyé, et pour le plaisir");
          audioService.playFile("/home/nao/recordings/cameras/naomathon/cri.wav");
        }
        else{
          tts.say("Je t'envois la photo par email mon chou");
          String filePath = ftpService.getImageFromNao("recordings/cameras/", photoName + ".jpg");
          PersonDTO person = emailDAO.getEmailFromName("Jocelyn");
          emailService.emailSender(person.getEmail(), filePath, false);
          tts.say("L'email est envoyé, et pour le plaisir");
          audioService.playFile("/home/nao/recordings/cameras/naomathon/castagne.wav");
        }
      statsDAO.incrementCompteur();

      //Create Bundle Future
    } catch (Exception e) {
      System.out.println("Erreur lors de l'execution du programme");
    }
  }
}
