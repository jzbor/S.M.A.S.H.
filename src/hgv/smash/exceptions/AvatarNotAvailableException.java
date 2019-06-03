package hgv.smash.exceptions;

public class AvatarNotAvailableException extends Exception {
    public AvatarNotAvailableException() {
    }

    public AvatarNotAvailableException(String s) {
        super(s);
    }

    public AvatarNotAvailableException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public AvatarNotAvailableException(Throwable throwable) {
        super(throwable);
    }

    public AvatarNotAvailableException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
