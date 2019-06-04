package hgv.smash.resources;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class Music {
    private File fileGameMusic;
    private static Music ourInstance = new Music();
    private Clip clip;
    public Music(){
        try {
            fileGameMusic=new File("./resources/Sounds_and_Music/Sandstorm.wav");}
        catch (Exception e)
            {e.printStackTrace();
        }
        try {
            clip= AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(fileGameMusic));

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
    public static Music getInstance() {
        return ourInstance;
    }
}


