package pl.ttpsc.lego.communication.service;

import org.apache.log4j.Logger;
import pl.ttpsc.lego.communication.LegoBrickDevice;
import pl.ttpsc.lego.name.LegoMotorMark;

import java.rmi.RemoteException;

public class LegoMotors {

    private final static Logger logger = Logger.getLogger(LegoMotors.class);

    private final LegoBrickDevice robotBrick;

    public LegoMotors(final LegoBrickDevice robotBrick) {
        this.robotBrick = robotBrick;
    }

    public void motorResetToInitialPosition(final LegoMotorMark motorMark) {
        try {
            if (robotBrick.isMotorConnected(motorMark)) {
                robotBrick.getMotors().get(motorMark).rotateTo(0);
            }
        } catch (RemoteException e) {
            logger.error("Problem with resetting motor " + motorMark.name(), e);
        }
    }

    public void motorsResetToInitialPosition() {
        robotBrick.getMotors().forEach((k, v) -> {
            motorResetToInitialPosition(k);
        });
    }

    public void motorRotateTo(final LegoMotorMark motorMark, final Integer toAngle)
            throws RemoteException {
        robotBrick.getMotors().get(motorMark).rotateTo(toAngle);
    }

    public void motorRotate(final LegoMotorMark motorMark, final Integer angle)
            throws RemoteException {
        robotBrick.getMotors().get(motorMark).rotate(angle);
    }

    public void motorResetTachoCount(final LegoMotorMark motorMark) {
        try {
            if (robotBrick.isMotorConnected(motorMark)) {
                robotBrick.getMotors().get(motorMark).resetTachoCount();
            }
        } catch (RemoteException e) {
            logger.error("Problem with resetTachoCount for motor " + motorMark.name(), e);
        }
    }

    public void motorsResetTachoCount() {
        robotBrick.getMotors().forEach((k, v) -> {
            motorResetTachoCount(k);
        });
    }

    public Integer motorGetTachoCount(final LegoMotorMark motorMark) {
        Integer tachoCount = null;
        try {
            if (robotBrick.isMotorConnected(motorMark)) {
                tachoCount = robotBrick.getMotors().get(motorMark).getTachoCount();
            }
        } catch (RemoteException e) {
            logger.error("Problem with getTachoCount for motor " + motorMark.name(), e);
        }
        return tachoCount;
    }

    public void motorPrepare(final LegoMotorMark motorMark,
                             final int speed, final int acceleration) {
        try {
            if (robotBrick.isMotorConnected(motorMark)) {
                motorResetTachoCount(motorMark);
                robotBrick.getMotors().get(motorMark).setSpeed(speed);
                robotBrick.getMotors().get(motorMark).setAcceleration(acceleration);
            }
        } catch (RemoteException e) {
            logger.error("Problem with setSpeed or setAcceleration for motor "
                                 + motorMark.name(), e);
        }
    }

    public void motorsPrepare(final int speed, final int acceleration) {
        robotBrick.getMotors().forEach((k, v) -> {
            motorPrepare(k, speed, acceleration);
        });
    }

}

