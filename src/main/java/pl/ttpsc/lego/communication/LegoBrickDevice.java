package pl.ttpsc.lego.communication;

import lejos.hardware.BrickFinder;
import lejos.remote.ev3.RMIRegulatedMotor;
import lejos.remote.ev3.RMISampleProvider;
import lejos.remote.ev3.RemoteEV3;
import lombok.Getter;
import org.apache.log4j.Logger;
import pl.ttpsc.lego.communication.service.LegoBrick;
import pl.ttpsc.lego.communication.service.LegoMotors;
import pl.ttpsc.lego.communication.service.LegoSensors;
import pl.ttpsc.lego.exception.LegoException;
import pl.ttpsc.lego.exception.SystemEnvNotFoundException;
import pl.ttpsc.lego.name.LegoMotorMark;
import pl.ttpsc.lego.name.LegoMotorType;
import pl.ttpsc.lego.name.LegoSensorMark;
import pl.ttpsc.lego.name.LegoSensorType;
import pl.ttpsc.lego.robot.bindings.ArmMotors;
import pl.ttpsc.lego.robot.bindings.ArmSensors;

import javax.annotation.PreDestroy;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;


public class LegoBrickDevice {

    private final static Logger logger = Logger.getLogger(LegoBrickDevice.class);

    @Getter
    private final Map<LegoMotorMark, RMIRegulatedMotor> motors = new HashMap<>(4);
    @Getter
    private final Map<LegoSensorMark, RMISampleProvider> sensors = new HashMap<>(4);
    @Getter
    private final LegoBrick brickServices;
    @Getter
    private final LegoMotors motorServices;
    @Getter
    private final LegoSensors sensorServices;
    @Getter
    private RemoteEV3 remoteEV3;
    @Getter
    private boolean isConnected;

    public LegoBrickDevice() throws SystemEnvNotFoundException {
        isConnected = false;
        brickServices = new LegoBrick(this);
        motorServices = new LegoMotors(this);
        sensorServices = new LegoSensors(this);
    }

    public boolean connectToBrick(final String brickIp) {

        boolean findIt = brickIp == null || brickIp.trim().isEmpty();
        if (findIt) {
            logger.info("LegoBrick connecting with BrickFinder (no IP)");
        } else {
            logger.info("LegoBrick connecting - IP: " + brickIp);
        }
        try {
            if (!isConnected()) {

                if (findIt) {
                    try {
                        remoteEV3 = (RemoteEV3) BrickFinder.getDefault();
                        isConnected = true;
                    } catch (Exception e) {
                        logger.error("Something went wrong when connecting to brick " +
                                             "with IP " + brickIp.toString(), e);
                    }
                } else {
                    try {
                        remoteEV3 = new RemoteEV3(brickIp);
                        isConnected = true;
                    } catch (Exception e) {
                        logger.error("Something went wrong when connecting to brick " +
                                             "with BrickFinder (no IP)", e);
                    }
                }

                if (isConnected()) {
                    logger.info("RemoteEV3 connected: " + remoteEV3.getName());
                } else {
                    logger.error("Lego brick not found with IP(s) aor by BrickFinder. " +
                                         "RemoteEV3 not connected.");
                }

            } else {
                logger.warn("RemoteEV3 previously connected with " + remoteEV3.getName()
                                    + " - action skipped.");
            }
        } catch (Exception e) {
            logger.error("Something went wrong when connecting to brick", e);
        } finally {
            return isConnected();
        }
    }

    public boolean connectToMotor(final LegoMotorMark motorMark, final LegoMotorType motorType) {
        Boolean motorConnected = false;
        try {
            if (isConnected()) {
                if (motors.containsKey(motorMark)) {
                    if (null != motors.get(motorMark)) {
                        motorConnected = true;
                    } else {
                        throw new LegoException("Motor " + motorMark.name()
                                                        + " is marked as connected, " +
                                                        "but connection is null");
                    }
                } else {
                    motors.put(motorMark, remoteEV3.createRegulatedMotor(
                            motorMark.name(), motorType.nameChar()));
                    logger.info("Motor " + motorMark.name() + "(" + motorType.name() + ") " +
                                        "connected.");
                    motorConnected = true;
                }
            } else {
                logger.error("Connecting to motor " + motorMark.name()
                                     + " with disconnected brick");
            }
        } catch (Exception e) {
            logger.error("Something went wrong when connecting to motor "
                                 + motorMark.name() + "(" + motorType.name() + ")", e);
        } finally {
            return motorConnected;
        }
    }

    public boolean connectToMotor(final ArmMotors motor) {
        return connectToMotor(motor.getMark(), motor.getType());
    }

    public boolean isMotorConnected(final LegoMotorMark motorMark) {
        return isConnected() && motors.containsKey(motorMark) && null != motors.get(motorMark);
    }

    public boolean connectToSensor(final LegoSensorMark sensorMark,
                                   final LegoSensorType sensorType) {
        Boolean sensorConnected = false;
        try {
            if (isConnected()) {
                if (sensors.containsKey(sensorMark)) {
                    if (null != sensors.get(sensorMark)) {
                        sensorConnected = true;
                    } else {
                        throw new LegoException("Sensor '" + sensorMark.name()
                                                        + "' is marked as connected, " +
                                                        "but connection is null");
                    }
                } else {
                    String port = sensorMark.name();
                    String lejosClass = sensorType.getSensorClass();
                    String typeName = sensorType.name();
                    RMISampleProvider sensor = remoteEV3.createSampleProvider(
                            port, lejosClass, typeName);
                    sensors.put(sensorMark, sensor);
                    logger.info("Sensor " + sensorMark.name() + "(" + sensorType.name() + ") " +
                                        "connected.");
                    sensorConnected = true;
                }
            } else {
                logger.error("Connecting to sensor " + sensorMark.name()
                                     + " with disconnected brick");
            }

        } catch (Exception e) {
            logger.error("Something went wrong when connecting to sensor "
                                 + sensorMark.name() + "(" + sensorType.name() + ")", e);
            // closePorts();
        } finally {
            return sensorConnected;
        }
    }

    public boolean connectToSensor(final ArmSensors sensor) {
        return connectToSensor(sensor.getMark(), sensor.getType());
    }

    public boolean isSensorConnected(final LegoSensorMark sensorMark) {
        return isConnected() && sensors.containsKey(sensorMark) && null != sensors.get(sensorMark);
    }

    @PreDestroy
    public void disconnectMotorsAndSensors() {
        logger.info("Disconnecting Motors and Sensors on EV3");
        if (isConnected()) {

            motors.forEach((k, v) -> {
                if (v != null) {
                    try {
                        v.close();
                        logger.info("Motor " + k.name() + " disconnected.");
                    } catch (RemoteException e) {
                        logger.error("Problem with disconnecting Motor " + k.name(), e);
                    }
                }
            });
            motors.clear();

            sensors.forEach((k, v) -> {
                if (v != null) {
                    try {
                        v.close();
                        logger.info("Sensor " + k.name() + " disconnected.");
                    } catch (RemoteException e) {
                        logger.error("Problem with disconnecting Sensor " + k.name(), e);
                    }
                }
            });
            sensors.clear();

            isConnected = false;
        }
    }
}
