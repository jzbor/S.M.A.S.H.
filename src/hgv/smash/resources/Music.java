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
    public Music(){
        fileGameMusic=new File("./resources/Sounds_and_Music/Darude-Sandstorm.mp3");
    }

    public void play (){
        try {
            Clip clip= AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(fileGameMusic));
            clip.start();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void stop(){

    }
    public static Music getInstance() {
        return ourInstance;
    }
}


