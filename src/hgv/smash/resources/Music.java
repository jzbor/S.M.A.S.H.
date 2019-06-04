package hgv.smash.resources;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class Music {
    private File fileGameMusic;
    private File fileMenuMusic;
    private File fileScoreMusic;
    private File chosenMusic;
    private static Music ourInstanceGameMusic=new Music("GameMusic");
    private static Music ourInstanceMenuMusic=new Music ("MenuMusic");
    private static Music ourInstanceScoreMusic=new Music ("ScoreMusic");
    private Clip clip;
    public Music(String musicYouNeed){
        fileGameMusic = new File("./resources/Sounds_and_Music/Sandstorm.wav");
        fileMenuMusic=new File ("./resources/Sounds_and_Music/Jeopardy.wav");
        fileScoreMusic=new File ("./resources/Sounds_and_Music/National_Anthem_Sowjet.wav");
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


