package pl.ttpsc.lego.program;

import lejos.utility.Delay;
import org.apache.log4j.Logger;
import pl.ttpsc.lego.communication.LegoBrickDevice;
import pl.ttpsc.lego.domain.Color;
import pl.ttpsc.lego.domain.statistic.ProgramStatistics;
import pl.ttpsc.lego.exception.SystemEnvNotFoundException;
import pl.ttpsc.lego.program.statistic.RobotArmStatistics;
import pl.ttpsc.lego.robot.bindings.ArmMotors;
import pl.ttpsc.lego.robot.bindings.ArmSensors;

import java.rmi.RemoteException;

import static pl.ttpsc.lego.utils.Env.getSysEnvInteger;

public class ArmProgram implements LegoProgram {

    private final static Logger logger = Logger.getLogger(ArmProgram.class);
    final int initialLift;
    final int initialHandRelease;
    final int initialHandGrab;
    private final int speed;
    private final int acceleration;
    private final LegoBrickDevice lego;
    private Thread robotThread;
    private boolean isProgramExecuted;
    private RobotArmStatistics programStatistics;


    public ArmProgram(final LegoBrickDevice lego) throws SystemEnvNotFoundException {

        speed = getSysEnvInteger("PROGRAM1_SPEED");
        acceleration = getSysEnvInteger("PROGRAM1_ACCELERATION");
        initialLift = getSysEnvInteger("INITIAL_LIFT");
        initialHandRelease = getSysEnvInteger("INITIAL_HAND_RELEASE");
        initialHandGrab = getSysEnvInteger("INITIAL_HAND_GRAB");

        this.lego = lego;
        programStatistics = new RobotArmStatistics();
    }

    @Override
    public void startProgram() {
        if (isProgramExecuted == false) {
            robotThread = new Thread(new RobotBehaviour());
            robotThread.start();
            logger.info("Robot thread started");
        }
    }

    @Override
    public void stopProgram() {
        isProgramExecuted = false;
        logger.info("Robot thread stopped");
    }

    @Override
    public ProgramStatistics getProgramStatistics() {
        return programStatistics;
    }

    @Override
    public Boolean isProgramLaunched() {
        return isProgramExecuted;
    }

    class RobotBehaviour implements Runnable {

        @Override
        public void run() {
            isProgramExecuted = true;
            lego.getMotorServices().motorsResetToInitialPosition();
            prepareMotors();

            try {
                liftToNormalStartupPosition();
                Thread.sleep(5000);
                while (isProgramExecuted) {
                    if (lego.getSensorServices()
                            .sensorIsObjectPresent(ArmSensors.DISTANCE.getMark())) {
                        grabAndMoveObject();
                    } else {
                        rotateRight();
                        rotateLeft();
                        Float gyro = lego.getSensorServices().sensorGetFloat(
                                ArmSensors.GYRO.getMark(), 0);
                        if (gyro > 3 || gyro < -3) {
                            lego.getSensorServices().sensorResetGyro(ArmSensors.GYRO.getMark());
                        }
                    }
                }

                endProgramOnBrick();

            } catch (RemoteException | InterruptedException e) {
                logger.error("Problem when executing robot arm program.", e);
            }
        }

        private void grabAndMoveObject() throws RemoteException {
            unlift();
            grab();
            Color color = Color.NONE;
            for (int i = 0; i < 20; i++) {
                color = lego.getSensorServices().sensorGetColor(ArmSensors.COLOR.getMark());
                if (color != Color.NONE) {
                    break;
                }
            }
            playSound(color);
            updateStatistics(color);
            liftMore();
            rotateToBasket();
            ungrab();
            rotateLeft();
            liftToNormalStartupPosition();
        }

        private void updateStatistics(Color color) {
            switch (color) {
                case RED:
                    programStatistics.increaseRed();
                    break;
                case GREEN:
                    programStatistics.increaseGreen();
                    break;
                case BLUE:
                    programStatistics.increaseBlue();
                    break;
                case NONE:
                    programStatistics.increaseUnknown();
                    break;
            }
        }

        private void playSound(Color color) {
            switch (color) {
                case RED:
                    lego.getBrickServices().getAudio().playTone(3000, 750);
                    //robotBrick.playSound(RED_SOUND_PATH);
                    break;
                case GREEN:
                    lego.getBrickServices().getAudio().playTone(3000, 750);
                    break;
                case BLUE:
                    lego.getBrickServices().getAudio().playTone(3000, 750);
                    break;
                case NONE:
                    lego.getBrickServices().getAudio().playTone(500, 1500);
                    break;
            }
        }

        private void endProgramOnBrick() throws RemoteException {
            lego.getMotorServices().motorsResetToInitialPosition();
            Delay.msDelay(1500); // so the robot stabilized
            resetTachoCountOnMotors();
        }

        private void rotateRight() throws RemoteException {
            lego.getMotorServices().motorRotateTo(ArmMotors.ROTATE_MOTOR.getMark(), 300);
        }

        private void rotateToBasket() throws RemoteException {
            lego.getMotorServices().motorRotateTo(ArmMotors.ROTATE_MOTOR.getMark(), 600);
        }

        private void rotateLeft() throws RemoteException {
            lego.getMotorServices().motorRotateTo(ArmMotors.ROTATE_MOTOR.getMark(), 0);
        }

        private void liftToNormalStartupPosition() throws RemoteException {
            lego.getMotorServices().motorRotateTo(ArmMotors.LIFT_MOTOR.getMark(), initialLift);
        }

        private void liftMore() throws RemoteException {
            lego.getMotorServices()
                .motorRotateTo(ArmMotors.LIFT_MOTOR.getMark(), initialLift - 100);
        }

        private void unlift() throws RemoteException {
            lego.getMotorServices()
                .motorRotateTo(ArmMotors.LIFT_MOTOR.getMark(), initialLift + 255);
        }

        private void grab() throws RemoteException {
            lego.getMotorServices().motorRotateTo(ArmMotors.HAND_MOTOR.getMark(), initialHandGrab);
        }

        private void ungrab() throws RemoteException {
            lego.getMotorServices()
                .motorRotateTo(ArmMotors.HAND_MOTOR.getMark(), initialHandRelease);
        }

        private void prepareMotors() {
            resetTachoCountOnMotors();
            lego.getMotorServices().motorsPrepare(speed, acceleration);
        }

        private void resetTachoCountOnMotors() {
            lego.getMotorServices().motorsResetTachoCount();
        }
    }

}

