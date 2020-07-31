package pl.ttpsc.lego.robot;

import lombok.Getter;
import org.apache.log4j.Logger;
import pl.ttpsc.lego.communication.LegoBrickDevice;
import pl.ttpsc.lego.exception.SystemEnvNotFoundException;
import pl.ttpsc.lego.program.ArmProgram;
import pl.ttpsc.lego.robot.bindings.ArmMotors;
import pl.ttpsc.lego.robot.bindings.ArmSensors;

import static pl.ttpsc.lego.utils.Env.getSysEnv;


public class RobotArm {

    private final static Logger logger = Logger.getLogger(RobotArm.class);
    
    private final String brickIp;

    @Getter
    private final LegoBrickDevice lego;

    @Getter
    private final ArmProgram program1;

    public RobotArm() throws SystemEnvNotFoundException {
        this.brickIp = getSysEnv("BRICK_IP");
        this.lego = new LegoBrickDevice();
        this.program1 = new ArmProgram(lego);
    }

    public void initialise() {
        Boolean allOk;
        try {
            allOk = lego.connectToBrick(brickIp);
            if (allOk) {
                logger.info("Robot with RemoteEV3 "
                                    + lego.getBrickServices().getName()
                                    + " - brick connected.");
            } else {
                logger.warn("Robot with RemoteEV3 "
                                    + lego.getBrickServices().getName()
                                    + " - brick not connected.");
            }
            
            if(allOk) {
                // Only if the Lego Brick is connected
                allOk &= lego.connectToMotor(ArmMotors.LIFT_MOTOR);
                allOk &= lego.connectToMotor(ArmMotors.HAND_MOTOR);
                allOk &= lego.connectToMotor(ArmMotors.ROTATE_MOTOR);
                if (allOk) {
                    logger.info("Robot with RemoteEV3 "
                                        + lego.getBrickServices().getName()
                                        + " - motors connected.");
                } else {
                    logger.warn("Robot with RemoteEV3 "
                                        + lego.getBrickServices().getName()
                                        + " - some motors not connected.");
                }
                allOk &= lego.connectToSensor(ArmSensors.DISTANCE);
                allOk &= lego.connectToSensor(ArmSensors.COLOR);
                allOk &= lego.connectToSensor(ArmSensors.GYRO);
                allOk &= lego.connectToSensor(ArmSensors.TOUCH);

                if (allOk) {
                    logger.info("Robot with RemoteEV3 "
                                        + lego.getBrickServices().getName()
                                        + " - sensors connected.");
                } else {
                    logger.warn("Robot with RemoteEV3 "
                                        + lego.getBrickServices().getName()
                                        + " - some sensors not connected.");
                }
            }
            
            if(!allOk) {
                // Disconnect all if connection had any trouble
                logger.info("Robot with RemoteEV3 "
                                    + lego.getBrickServices().getName()
                                    + " - there were some errors while connecting. " +
                                    "Disconnection starting.");
                lego.disconnectMotorsAndSensors();
            }
        } catch (Exception e) {
            logger.error("Something went wrong when connecting to Robot with RemoteEV3", e);
        }
    }


}
