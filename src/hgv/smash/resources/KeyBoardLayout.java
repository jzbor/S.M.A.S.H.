
package hgv.smash.resources;

import java.io.Serializable;

public class KeyBoardLayout implements Serializable {
    private char[] player1Keys;
    private char[] player2Keys;

    public KeyBoardLayout(){
        player1Keys = new char[] {'w', 'f', 'a', 'd','r'};
        player2Keys = new char[]{'i', 'ö', 'j', 'l','p'};
    }
    public void setKeysForPlayer1(char[] keys){
        player1Keys=keys;
    }

    public void setKeysForPlayer2(char[] keys){
        player2Keys=keys;
    }
    public char[] getPlayer1Keys(){
        return player1Keys;
    }

    public char[] getPlayer2Keys(){
        return player2Keys;
    }
}

