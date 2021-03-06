package main;

import DAO.EmailDAO;
import DAO.PersonDTO;
import DAO.StatsDAO;
import EmailService.EmailSender;
import FTPService.FTPService;
import com.aldebaran.qimessaging.CallError;
import com.aldebaran.qimessaging.Session;
import com.aldebaran.qimessaging.helpers.al.*;

import javax.mail.MessagingException;

/**
 * Created by Jocelyn on 29/06/2015.
 */
public class PhotoProgramm {

  private int id;
  /**
   * NAO utils
   */
  private static Session session;
  private static ALTextToSpeech tts;
  private static ALAudioPlayer audioService;
  public static ALRobotPosture robotPosture;
  private static com.aldebaran.qimessaging.Object photoCapture;
  /**
   * Service connexion
   */
  private static EmailSender emailService;
  private static FTPService ftpService = new FTPService("172.16.6.117");
  private static EmailDAO emailDAO;
  private static StatsDAO statsDAO;
  private static String folderPhoto = "recordings/cameras/naomathon/";
  private static String robotPhotoName = "naoMathon";
  public static ALLeds leds;
  public static ALMotion motion;

  public PhotoProgramm(int id, Session session){
    this.id = id;
    this.session = session;
  }

  public void onTouch(java.lang.Object value) {
    if(this.id == 2){
      int data = (Integer) value;
      if (data == 1.0) {
        System.out.println("Touch detected");
        this.takePhoto();
      }
    }
    else{
      float data = (Float) value;
      if (data == 1.0) {
        System.out.println("Touch detected");
        this.takePhoto();
      }
    }
  }
  public void takePhoto(){

    try{
      //Declare Variable Object
      tts = new ALTextToSpeech(session);
      audioService = new ALAudioPlayer(session);
      photoCapture = session.service("ALPhotoCapture");
      robotPosture = new ALRobotPosture(session);
      leds = new ALLeds(session);
      motion = new ALMotion(session);
      emailDAO = new EmailDAO();
      emailService = new EmailSender();
      statsDAO = new StatsDAO();
      PersonDTO person= null;

      if(id !=0){
        person = emailDAO.getEmailByID(id);
      }

      //Debut interaction robot
      robotPosture.goToPosture("StandZero", (float) 1.0);
      //motion.rest();
      tts.setVolume((float) 1.0);
      tts.setLanguage("French");
      tts.say("On se prépare pour la photo!");
      tts.say("3");
      tts.say("2");
      tts.say("1");
      photoCapture.call("setPictureFormat", new java.lang.Object[]{"jpg"}).get();
      photoCapture.call("setResolution", new java.lang.Object[]{2}).get();
      motion.openHand("RHand");
      photoCapture.call("takePicture", new java.lang.Object[]{"/home/nao/" + folderPhoto, robotPhotoName, true}).get();
      audioService.playFile("/home/nao/recordings/cameras/naomathon/flash.wav");
      motion.closeHand("RHand");
      int compteur = statsDAO.getCompteur();

      //Cas bumper enregistré on on envois un ID
      if(id != 0){
        if(compteur%3 == 0 ){
          tts.say("Désolé, " + person.getName() + "ta photo est raté");
          String filePath = ftpService.getImageFromNao("recordings/cameras/naomathon/", robotPhotoName + ".jpg");
          emailService.emailSender(person.getEmail(), null, true);
          tts.say("Les détails sont dans l'email que je t'ai envoyé.");
          tts.say("La photo est quand même disponible sur la galerie a l'adresse naomathon point f r.");
          audioService.playFile("/home/nao/recordings/cameras/naomathon/castagne.wav");
        }
        else{
          tts.say(person.getName() + "Je t'envois la photo par email");
          String filePath = ftpService.getImageFromNao("recordings/cameras/naomathon/", robotPhotoName + ".jpg");
          emailService.emailSender(person.getEmail(), filePath, false);
          tts.say("L'email est bien envoyé.");
          tts.say("La photo est également disponible sur la galerie a l'adresse naomathon point f r.");
        }
        statsDAO.incrementCompteur();
      }
      //Cas sans ID, par touch sur bumper du front
      else{
        tts.say("Transfert de la photo sur la galerie");
        String filePath = ftpService.getImageFromNao("recordings/cameras/naomathon/", robotPhotoName + ".jpg");
        tts.say("La photo est disponible sur la galerie a l'adresse naomathon point f r.");
      }
      robotPosture.goToPosture("SitRelax", (float) 1.0);
      leds.rasta((float) 5.0);
      //robotPosture.goToPosture("LyingBelly", (float) 1.0);
      motion.rest();
      System.out.println("Programme photo terminé");
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (CallError callError) {
      callError.printStackTrace();
    } catch (MessagingException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
