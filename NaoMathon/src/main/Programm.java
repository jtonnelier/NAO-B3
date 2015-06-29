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

  /**
   * Fonction principale pour prendre une photo via NAO
   * @param id
   */
  public static void takePhoto(int id){
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
      tts.setLanguage("French");
      tts.say("On se prépare pour la photo!");
      tts.say("3");
      tts.say("2");
      tts.say("1");
      photoCapture.call("setPictureFormat", new java.lang.Object[]{"jpg"}).get();
      photoCapture.call("setResolution", new java.lang.Object[]{2}).get();
      photoCapture.call("takePicture", new java.lang.Object[]{"/home/nao/" + folderPhoto, photoName, true}).get();
      audioService.playFile("/home/nao/recordings/cameras/naomathon/flash.wav");
      int compteur = statsDAO.getCompteur();

      //Cas de la detection on on envois un ID
      if(id == 0){
        if(compteur%3 == 0 ){
          tts.say("Photo raté, désolé");
          PersonDTO person = emailDAO.getEmailByID(id);
          emailService.emailSender(person.getEmail(), null, true);
          tts.say("L'email est envoyé.");
          audioService.playFile("/home/nao/recordings/cameras/naomathon/cri.wav");
        }
        else{
          tts.say("Je vous envois la photo par email");
          String filePath = ftpService.getImageFromNao("recordings/cameras/naomathon/", photoName + ".jpg");
          PersonDTO person = emailDAO.getEmailFromName("Jocelyn");
          emailService.emailSender(person.getEmail(), filePath, false);
          tts.say("L'email est bien envoyé.");
          audioService.playFile("/home/nao/recordings/cameras/naomathon/castagne.wav");
        }
        statsDAO.incrementCompteur();
      }
      //Cas sans ID, par touch sur bumper
      else{
        tts.say("Transfert de la photo sur la galerie");
        String filePath = ftpService.getImageFromNao("recordings/cameras/naomathon/", photoName + ".jpg");
        tts.say("La photo est disponible sur la galerie a l'adresse naomathon point f r.");
      }

      //Create Bundle Future
    } catch (Exception e) {
      System.out.println("Erreur lors de l'execution du programme");
    }
  }

  public static void main (String Args[]){
    takePhoto(0);
  }
}
