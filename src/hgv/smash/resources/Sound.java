package hgv.smash.resources;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class Sound {

    public static final int HIT_SOUND=0;
    public static final int JUMP_SOUND=1;

    //Files von den verschiedenen Sounds
    private File fileHit;
    private File fileJump;

    //File von dem Sound, der gebraucht
    private File chosenSound;
    //auf den Clip wird der sound geladen und der Clip kann dann auch abgespielt werden.
    private Clip clip;

        public Sound(int sound) {

        fileHit = new File("./resources/Sounds_and_Music/hit2.wav");
        fileJump = new File("./resources/Sounds_and_Music/jump.wav");

        //hier wird entschieden welcher File dann geladen wird
        switch (sound) {
            case HIT_SOUND:
                chosenSound = fileHit;
                break;
            case JUMP_SOUND:
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

    public void play() {
        clip.stop();
        clip.setFramePosition(0);
        clip.start();
    }

    public void stop() {
        clip.stop();
    }
}

