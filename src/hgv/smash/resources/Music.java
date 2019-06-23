package hgv.smash.resources;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class Music {

    //diese drei verschiedenen Objekte können erzeugt werden und von jeder anderen Klasse aufgerufen werden
    private static Music ourInstanceGameMusic = new Music("GameMusic");
    private static Music ourInstanceMenuMusic = new Music("MenuMusic");
    private static Music ourInstanceScoreMusicSowjet = new Music("ScoreMusicSowjet");
    private static Music ourInstanceScoreMusicBavaria = new Music("ScoreMusicBavaria");
    private static Music ourInstanceScoreMusicScout = new Music("ScoreMusicScout");
    //Files von den verschiedenen Liedern
    private File fileGameMusic;
    private File fileMenuMusic;
    private File fileScoreMusicSowjet;
    private File fileScoreMusicBavaria;
    private File fileScoreMusicScout;
    //File von dem Lied, das spielt
    private File chosenMusic;
    //auf den Clip wird das Lied geladen und der Clip kann dann auch abgespielt werden.
    private Clip clip;

    //Man kann sich entscheiden ob man ein Objekt der Klasse Musik erzeugen will, das die Menümusik,
    //oder die Scoremusik abspielt.
    private Music(String musicYouNeed) {

        fileGameMusic = new File("./resources/Sounds_and_Music/Sandstorm.wav");
        fileMenuMusic = new File("./resources/Sounds_and_Music/Jeopardy.wav");
        fileScoreMusicSowjet = new File("./resources/Sounds_and_Music/National_Anthem_Sowjet.wav");
        fileScoreMusicBavaria=new File("./resources/Sounds_and_Music/Rede.wav");
        fileScoreMusicScout=new File("./resources/Sounds_and_Music/downUnder.wav");
        //hier wird entschieden welcher File dann geladen wird
        switch (musicYouNeed) {
            case "GameMusic":
                chosenMusic = fileGameMusic;
                break;
            case "MenuMusic":
                chosenMusic = fileMenuMusic;
                break;
            case "ScoreMusicSowjet":
                chosenMusic = fileScoreMusicSowjet;
                break;
            case "ScoreMusicBavaria":
                chosenMusic=fileScoreMusicBavaria;
                break;
            case "ScoreMusicScout":
                chosenMusic=fileScoreMusicScout;
                break;
            default:
                break;
        }

        //hier wird auf das Audiosystem zugegriffen und der ausgesuchte File auf den Clip geladen
        try {
            clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(chosenMusic));

        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
    }

    public static Music getInstanceMenuMusic() {
        return ourInstanceMenuMusic;
    }

    public static Music getInstanceGameMusic() {
        return ourInstanceGameMusic;
    }

    //Über diese Methode 5 Methoden kann jede Klasse auf die 5 verschiedenen
    // Objekte der Klasse Musik zugreifen.

    public static Music getInstanceScoreMusicSowjet() {
        return ourInstanceScoreMusicSowjet;
    }

    public static Music getInstanceScoreMusicScout() {
        return ourInstanceScoreMusicScout;
    }
    public static Music getInstanceScoreMusicBavaria() {
        return ourInstanceScoreMusicBavaria;
    }
    public void play() {
        clip.close();
        try {
            clip.open(AudioSystem.getAudioInputStream(chosenMusic));

        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        clip.stop();
    }
}


