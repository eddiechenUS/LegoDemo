package pl.ttpsc.lego.communication.service;

import org.apache.log4j.Logger;
import pl.ttpsc.lego.communication.LegoBrickDevice;
import pl.ttpsc.lego.domain.Color;
import pl.ttpsc.lego.exception.SystemEnvNotFoundException;
import pl.ttpsc.lego.name.LegoSensorMark;
import pl.ttpsc.lego.name.LegoSensorType;

import java.rmi.RemoteException;

import static pl.ttpsc.lego.utils.Env.getSysEnvFloat;

public class LegoSensors {

    private final static Logger logger = Logger.getLogger(LegoSensors.class);

    private final float object_maximal_distance_default;
    private final LegoBrickDevice robotBrick;

    public LegoSensors(final LegoBrickDevice robotBrick) throws SystemEnvNotFoundException {

        object_maximal_distance_default = getSysEnvFloat("PROGRAM1_OBJECT_DISTANCE");

        this.robotBrick = robotBrick;
    }

    private StringBuilder getSensorAnswerString(final LegoSensorMark sensorMark,
                                                final float[] sample) {
        StringBuilder txt = new StringBuilder();
        txt.append(sensorMark.name());
        txt.append(" => ");
        if (sample == null) {
            txt.append("null");
        } else {
            txt.append("[");
            for (int i = 0; i < sample.length; i++) {
                if (i > 0) {
                    txt.append(", ");
                }
                txt.append(Float.valueOf(sample[i]).toString());
            }
            txt.append("]");
        }
        return txt;
    }

    public float[] sensorGetFloatArray(final LegoSensorMark sensorMark)
            throws RemoteException {
        float[] sample = {};
        if (sensorMark != null && robotBrick.isSensorConnected(sensorMark)) {
            sample = robotBrick.getSensors().get(sensorMark).fetchSample();
        }
        return sample;
    }

    public Float sensorGetFloat(final LegoSensorMark sensorMark, final int sampleNumber)
            throws RemoteException {
        Float value = null;
        if (sensorMark != null && robotBrick.isSensorConnected(sensorMark)) {
            float[] sample = sensorGetFloatArray(sensorMark);
            if (sample.length > sampleNumber) {
                //                logger.debug("Lego sensor fetchSample: "
                //                                     + getSensorAnswerString(sensorMark, sample));
                value = sample[sampleNumber];
            } else {
                logger.warn("Lego sensor fetchSample failed. Nedded item[], but sensor returns "
                                    + getSensorAnswerString(sensorMark, sample));
            }
        }
        return value;
    }

    public boolean sensorResetGyro(final LegoSensorMark sensorMark) {
        Boolean reset = false;
        try {
            if (sensorMark != null && robotBrick.isSensorConnected(sensorMark)) {
                robotBrick.getSensors().get(sensorMark).close();
                robotBrick.getSensors().remove(sensorMark);
            }
            Thread.sleep(1);
            robotBrick.connectToSensor(sensorMark, LegoSensorType.Angle);
            reset = true;
        } catch (Exception e) {
            logger.error("Problem with reseting Gyro", e);
        } finally {
            return reset;
        }
    }

    public Color sensorGetColor(final LegoSensorMark sensorMark) throws RemoteException {

        Color recognizedColor = Color.NONE;
        Float value = sensorGetFloat(sensorMark, 0);
        if (value != null) {

            switch (value.intValue()) {
                case 0:
                    recognizedColor = Color.RED;
                    break;
                case 1:
                    recognizedColor = Color.GREEN;
                    break;
                case 2:
                    recognizedColor = Color.BLUE;
                    break;
                //                case 3:
                //                    recognizedColor = Color.YELLOW;
                //                    break;
                //                //                case 4:
                //                //                    recognizedColor = Color.BROWN;
                //                //                    break;
                //                case 6:
                //                    recognizedColor = Color.WHITE;
                //                    break;
                //                case 7:
                //                    recognizedColor = Color.BLACK;
                //                    break;
                default:
            }
        }
        return recognizedColor;
    }

    public boolean sensorIsObjectPresent(final LegoSensorMark sensorMark, final float borderValue)
            throws RemoteException {
        return sensorGetFloat(sensorMark, 0) < borderValue;
    }

    public boolean sensorIsObjectPresent(final LegoSensorMark sensorMark) throws RemoteException {
        return sensorIsObjectPresent(sensorMark, object_maximal_distance_default);
    }

    public boolean sensorIsTouch(final LegoSensorMark sensorMark)
            throws RemoteException {
        return sensorGetFloat(sensorMark, 0) == 1f;
    }

}
