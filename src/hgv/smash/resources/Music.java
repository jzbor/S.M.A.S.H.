package hgv.smash.resources;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class Music {

    //Files von den verschiedenen Liedern
    private File fileGameMusic;
    private File fileMenuMusic;
    private File fileScoreMusic;

    //File von dem Lied, dass spielt
    private File chosenMusic;

    //diese drei verschiedenen Objekte können erzeugt werden und von jeder anderen Klasse aufgerufen werden
    private static Music ourInstanceGameMusic=new Music("GameMusic");
    private static Music ourInstanceMenuMusic=new Music ("MenuMusic");
    private static Music ourInstanceScoreMusic=new Music ("ScoreMusic");

    //auf den Clip wird das Lied geladen und der Clip kann dann auch abgespielt werden.
    private Clip clip;

    //Man kann sich entscheiden ob man ein Objekt der Klasse Musik erzeugen will, dass die Menümusik,
    //oder die Scoremusik abspielt.
    public Music(String musicYouNeed){

        fileGameMusic = new File("./resources/Sounds_and_Music/Sandstorm.wav");
        fileMenuMusic=new File ("./resources/Sounds_and_Music/Jeopardy.wav");
        fileScoreMusic=new File ("./resources/Sounds_and_Music/National_Anthem_Sowjet.wav");

        //hier wird entschieden welcher File dann geladen wird
        switch (musicYouNeed) {
            case "GameMusic":
                chosenMusic=fileGameMusic;
                break;
            case "MenuMusic":
                chosenMusic=fileMenuMusic;
                break;
            case "ScoreMusic":
                chosenMusic=fileScoreMusic;
                break;
            default:
                break;
        }

        //hier wird auf das Audiosystem zugegriffen und der ausgesuchte File auf den Clip geladen
        try {
            clip= AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(chosenMusic));

        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void play (){
        clip.start();
    }
    public void stop(){
        clip.stop();
    }

    //Über diese Methode drei Methoden kann jede Klasse auf die drei verschiedenen singleton
    // Objekte der Klasse Musik zugreifen.

    public static Music getInstanceMenuMusic() {
        return ourInstanceMenuMusic;
    }

    public static Music getInstanceGameMusic(){
        return ourInstanceGameMusic;
    }
    public static Music getOurInstanceScoreMusic(){
        return ourInstanceScoreMusic;
    }
}


