
package hgv.smash.resources;

import hgv.smash.gui.KeySetPanel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class KeyBoardLayout implements Serializable {
    private final String PATH = "./resources/keyboard/keyboardLayout.ser";

    private static char[] player1DefaultKeys =new char[] {'w', 'a', 'd', 'f','r'};
    private static char[] player2DefaultKeys= new char[]{'i', 'j', 'l', 'ö','p'};

    private char[] player1Keys;
    private char[] player2Keys;

    public KeyBoardLayout(){
        player1Keys = player1DefaultKeys.clone();
        player2Keys = player2DefaultKeys.clone();
    }
    public void setKeysForPlayer1(char[] keys){
        player1Keys=keys;
    }

    public void setKeysForPlayer2(char[] keys){
        player2Keys=keys;
    }

    public char[] getPlayer1Keys(){
        return player1Keys.clone();
    }

    public char[] getPlayer2Keys(){
        return player2Keys.clone();
    }

    public static char[] getPlayer1DefaultKeys(){
        return player1DefaultKeys.clone();
    }

    public static char[] getPlayer2DefaultKeys(){
        return player2DefaultKeys.clone();
    }

    public void serialize() {
        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            File file = new File(PATH);
            fileOutputStream = new FileOutputStream(file);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(this);
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (objectOutputStream != null) {
                try {
                    objectOutputStream.close();
                } catch (Exception oOSException) {
                    oOSException.printStackTrace();
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (Exception fOSException) {
                    fOSException.printStackTrace();
                }
            }
        }
    }
}

