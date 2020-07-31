package pl.ttpsc.lego.exception;

public class FileFormatException extends RuntimeException {

    public FileFormatException() {
        super("Problem with obtaining file format of WAV");
    }
}
