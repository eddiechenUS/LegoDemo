package pl.ttpsc.lego.exception;

public class SystemEnvNotFoundException extends LegoException {

    public SystemEnvNotFoundException(final String name) {
        super("System Env '" + name + "' not found!");
    }
}
