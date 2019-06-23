package hgv.smash.exceptions;

public class AvatarNotAvailableException extends Exception {

    public AvatarNotAvailableException(String s) {
        super(s);
    }

    public AvatarNotAvailableException(String s, Throwable throwable) {
        super(s, throwable);
    }

}
