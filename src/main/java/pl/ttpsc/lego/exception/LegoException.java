package pl.ttpsc.lego.exception;

public class LegoException extends Exception {

    private static final long serialVersionUID = 1L;

    public LegoException() {
        super();
    }

    public LegoException(String message, Throwable cause) {
        super(message, cause);
    }

    public LegoException(String message) {
        super(message);
    }

    public LegoException(Throwable cause) {
        super(cause);
    }

}
