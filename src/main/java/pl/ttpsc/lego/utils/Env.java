package pl.ttpsc.lego.utils;

import pl.ttpsc.lego.exception.SystemEnvNotFoundException;

public class Env {

    public static String getSysEnv(final String name) throws SystemEnvNotFoundException {
        String env = System.getenv(name);
        if (env == null) {
            throw new SystemEnvNotFoundException(name);
        }
        return env;
    }

    public static Integer getSysEnvInteger(final String name) throws SystemEnvNotFoundException {
        return Integer.parseInt(getSysEnv(name));
    }

    public static Float getSysEnvFloat(final String name) throws SystemEnvNotFoundException {
        return Float.parseFloat(getSysEnv(name));
    }

}
