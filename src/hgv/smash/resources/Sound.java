package hgv.smash.resources;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Sound {

    public static final int HIT_SOUND = 0;
    public static final int JUMP_SOUND = 1;
    public static final int SUPER_HIT_SOUND = 2;

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
        float volume = 0;
        switch (sound) {
            case HIT_SOUND:
                chosenSound = fileHit;
                volume = 0.7f;
                break;
            case JUMP_SOUND:
                chosenSound = fileJump;
                volume = 0.7f;
                break;
            case SUPER_HIT_SOUND:
                chosenSound = fileHit;
                volume = 1.0f;
            default:
                break;
        }

        //hier wird auf das Audiosystem zugegriffen und der ausgesuchte File auf den Clip geladen
        try {
            clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(chosenSound));
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float range = gainControl.getMaximum() - gainControl.getMinimum();
            float gain = (range * volume) + gainControl.getMinimum();
            gainControl.setValue(gain);

        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
    }

    public void play() {
        clip.stop();
        clip.setFramePosition(0);
        clip.start();
    }

}

