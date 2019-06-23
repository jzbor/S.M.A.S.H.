package hgv.smash.resources;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class Sound {


    //diese drei verschiedenen Objekte können erzeugt werden und von jeder anderen Klasse aufgerufen werden
    private static Sound ourInstanceSoundHit = new Sound("Hit");
    private static Sound ourInstanceSoundJump = new Sound("Jump");

    //Files von den verschiedenen Sounds
    private File fileHit;
    private File fileJump;

    //File von dem Sound, der gebraucht
    private File chosenSound;
    //auf den Clip wird der sound geladen und der Clip kann dann auch abgespielt werden.
    private Clip clip;

        private Sound(String musicYouNeed) {

        fileHit = new File("./resources/Sounds_and_Music/hit2.wav");
        fileJump = new File("./resources/Sounds_and_Music/jump.wav");

        //hier wird entschieden welcher File dann geladen wird
        switch (musicYouNeed) {
            case "Hit":
                chosenSound = fileHit;
                break;
            case "Jump":
                chosenSound = fileJump;
                break;

            default:
                break;
        }

        //hier wird auf das Audiosystem zugegriffen und der ausgesuchte File auf den Clip geladen
        try {
            clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(chosenSound));

        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
    }


    public static Sound getInstanceSoundHit() {
        return ourInstanceSoundHit;
    }
    public static Sound getInstanceSoundJump() {
        return ourInstanceSoundJump;
    }
    public void play() {
        clip.setFramePosition(0);
        clip.start();
    }
}

