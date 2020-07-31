package pl.ttpsc.lego.exception;

public class BrickAlreadyConnectedException extends LegoException {

    public BrickAlreadyConnectedException() {
        super("Brick has been already connected!");
    }
}
