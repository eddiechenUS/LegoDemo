package pl.ttpsc.lego.communication;

import com.thingworx.communications.client.ConnectedThingClient;
import com.thingworx.communications.client.things.VirtualThing;
import com.thingworx.metadata.FieldDefinition;
import com.thingworx.metadata.annotations.ThingworxPropertyDefinition;
import com.thingworx.metadata.annotations.ThingworxPropertyDefinitions;
import com.thingworx.metadata.annotations.ThingworxServiceDefinition;
import com.thingworx.metadata.annotations.ThingworxServiceParameter;
import com.thingworx.metadata.annotations.ThingworxServiceResult;
import com.thingworx.types.BaseTypes;
import com.thingworx.types.InfoTable;
import com.thingworx.types.collections.ValueCollection;
import com.thingworx.types.primitives.BooleanPrimitive;
import com.thingworx.types.primitives.NumberPrimitive;
import com.thingworx.types.primitives.StringPrimitive;
import org.apache.log4j.Logger;
import pl.ttpsc.lego.domain.statistic.ProgramStatistics;
import pl.ttpsc.lego.exception.SystemEnvNotFoundException;
import pl.ttpsc.lego.name.LegoMotorMark;
import pl.ttpsc.lego.robot.RobotArm;
import pl.ttpsc.lego.robot.bindings.ArmMotors;
import pl.ttpsc.lego.robot.bindings.ArmSensors;

import java.rmi.RemoteException;
import java.util.Random;

import static pl.ttpsc.lego.utils.Env.getSysEnv;

@ThingworxPropertyDefinitions(properties = {
        @ThingworxPropertyDefinition(name = "robotConneted",
                                     description = "Check if Lego Robot is conneted",
                                     baseType = "BOOLEAN",
                                     category = "Brick info",
                                     aspects = {"dataChangeType:ALWAYS", "dataChangeThreshold:0",
                                             "cacheTime:-1",
                                             "isPersistent:FALSE", "isReadOnly:TRUE",
                                             "pushType:ALWAYS",
                                             "isFolded:FALSE", "defaultValue:0"}),
        @ThingworxPropertyDefinition(name = "DeviceId",
                                     description = "DeviceId",
                                     baseType = "STRING",
                                     category = "Brick info",
                                     aspects = {"dataChangeType:ALWAYS", "dataChangeThreshold:0",
                                             "cacheTime:-1",
                                             "isPersistent:FALSE", "isReadOnly:TRUE",
                                             "pushType:ALWAYS",
                                             "isFolded:FALSE", "defaultValue:0"}),
        @ThingworxPropertyDefinition(name = "gyroAngle",
                                     description = "",
                                     baseType = "NUMBER",
                                     category = "Sensor info",
                                     aspects = {"dataChangeType:ALWAYS", "dataChangeThreshold:0",
                                             "cacheTime:-1",
                                             "isPersistent:FALSE", "isReadOnly:TRUE",
                                             "pushType:ALWAYS",
                                             "isFolded:FALSE", "defaultValue:0"}),
        @ThingworxPropertyDefinition(name = "distance",
                                     description = "",
                                     baseType = "NUMBER",
                                     category = "Sensor info",
                                     aspects = {"dataChangeType:ALWAYS", "dataChangeThreshold:0",
                                             "cacheTime:-1",
                                             "isPersistent:FALSE", "isReadOnly:TRUE",
                                             "pushType:ALWAYS",
                                             "isFolded:FALSE", "defaultValue:0"}),
        @ThingworxPropertyDefinition(name = "touch",
                                     description = "",
                                     baseType = "BOOLEAN",
                                     category = "Sensor info",
                                     aspects = {"dataChangeType:ALWAYS", "dataChangeThreshold:0",
                                             "cacheTime:-1",
                                             "isPersistent:FALSE", "isReadOnly:TRUE",
                                             "pushType:ALWAYS",
                                             "isFolded:FALSE", "defaultValue:0"}),
        @ThingworxPropertyDefinition(name = "color",
                                     description = "",
                                     baseType = "STRING",
                                     category = "Sensor info",
                                     aspects = {"dataChangeType:ALWAYS", "dataChangeThreshold:0",
                                             "cacheTime:-1",
                                             "isPersistent:FALSE", "isReadOnly:TRUE",
                                             "pushType:ALWAYS",
                                             "isFolded:FALSE", "defaultValue:0"}),
        @ThingworxPropertyDefinition(name = "voltage",
                                     description = "",
                                     baseType = "NUMBER",
                                     category = "Brick info",
                                     aspects = {"dataChangeType:ALWAYS", "dataChangeThreshold:0",
                                             "cacheTime:-1",
                                             "isPersistent:FALSE", "isReadOnly:TRUE",
                                             "pushType:ALWAYS",
                                             "isFolded:FALSE", "defaultValue:0"}),
        @ThingworxPropertyDefinition(name = "powerConsumption",
                                     description = "",
                                     baseType = "NUMBER",
                                     category = "Status",
                                     aspects = {"dataChangeType:ALWAYS", "dataChangeThreshold:0",
                                             "cacheTime:-1",
                                             "isPersistent:FALSE", "isReadOnly:TRUE",
                                             "pushType:ALWAYS",
                                             "isFolded:FALSE", "defaultValue:0"}),
        @ThingworxPropertyDefinition(name = "liftMotorAngle",
                                     description = "",
                                     baseType = "NUMBER",
                                     category = "Motor info",
                                     aspects = {"dataChangeType:ALWAYS", "dataChangeThreshold:0",
                                             "cacheTime:-1",
                                             "isPersistent:FALSE", "isReadOnly:TRUE",
                                             "pushType:ALWAYS",
                                             "isFolded:FALSE", "defaultValue:0"}),
        @ThingworxPropertyDefinition(name = "rotateMotorAngle",
                                     description = "",
                                     baseType = "NUMBER",
                                     category = "Motor info",
                                     aspects = {"dataChangeType:ALWAYS", "dataChangeThreshold:0",
                                             "cacheTime:-1",
                                             "isPersistent:FALSE", "isReadOnly:TRUE",
                                             "pushType:ALWAYS",
                                             "isFolded:FALSE", "defaultValue:0"}),
        @ThingworxPropertyDefinition(name = "handMotorAngle",
                                     description = "",
                                     baseType = "NUMBER",
                                     category = "Motor info",
                                     aspects = {"dataChangeType:ALWAYS", "dataChangeThreshold:0",
                                             "cacheTime:-1",
                                             "isPersistent:FALSE", "isReadOnly:TRUE",
                                             "pushType:ALWAYS",
                                             "isFolded:FALSE", "defaultValue:0"}),
        @ThingworxPropertyDefinition(name = "date",
                                     description = "",
                                     baseType = "STRING",
                                     category = "Brick info",
                                     aspects = {"dataChangeType:ALWAYS", "dataChangeThreshold:0",
                                             "cacheTime:-1",
                                             "isPersistent:FALSE", "isReadOnly:TRUE",
                                             "pushType:ALWAYS",
                                             "isFolded:FALSE", "defaultValue:0"})
})

public class LegoBrickThing extends VirtualThing {
    private final static Logger logger = Logger.getLogger(LegoBrickThing.class);

    private static RobotArm robot;

    static {
        try {
            robot = new RobotArm();
        } catch (SystemEnvNotFoundException e) {
            e.printStackTrace();
        }
    }

    public LegoBrickThing(String description, ConnectedThingClient client) throws Exception {
        super(getSysEnv("THINGWORX_THINGNAME"), description, client);
        this.initializeFromAnnotations();
    }

    @Override
    public void processScanRequest() {
        try {

            Random generator = new Random();

            Boolean connected = robot.getLego().isConnected();
            this.setPropertyValue("robotConneted",
                                  new BooleanPrimitive(connected));
            if (connected) {
                this.setPropertyValue("DeviceId",
                                      new StringPrimitive(
                                              robot.getLego().getBrickServices().getName()
                                      ));

                this.setPropertyValue("gyroAngle",
                                      new NumberPrimitive(
                                              robot.getLego()
                                                   .isSensorConnected(ArmSensors.GYRO.getMark()) ?
                                                      robot.getLego().getSensorServices()
                                                           .sensorGetFloat(
                                                                   ArmSensors.GYRO.getMark(),
                                                                   0)
                                                      : -1
                                      ));
                this.setPropertyValue("distance",
                                      new NumberPrimitive(
                                              robot.getLego()
                                                   .isSensorConnected(ArmSensors.DISTANCE.getMark())
                                                      ?
                                                      robot.getLego().getSensorServices()
                                                           .sensorGetFloat(
                                                                   ArmSensors.DISTANCE.getMark(), 0)
                                                      : -1
                                      ));
                this.setPropertyValue("touch",
                                      new BooleanPrimitive(
                                              robot.getLego()
                                                   .isSensorConnected(ArmSensors.TOUCH.getMark())
                                                      ?
                                                      robot.getLego().getSensorServices()
                                                           .sensorIsTouch(
                                                                   ArmSensors.TOUCH.getMark())
                                                      : false
                                      ));
                this.setPropertyValue("color",
                                      new StringPrimitive(
                                              robot.getLego()
                                                   .isSensorConnected(ArmSensors.COLOR.getMark()) ?
                                                      robot.getLego().getSensorServices()
                                                           .sensorGetColor(
                                                                   ArmSensors.COLOR.getMark())
                                                           .name()
                                                      : "-"
                                      ));
                this.setPropertyValue("voltage",
                                      new NumberPrimitive(
                                              robot.getLego().getBrickServices().getVoltage()
                                      ));
                this.setPropertyValue("powerConsumption",
                                      new NumberPrimitive(
                                              robot.getLego().getBrickServices().getBatteryCurrent()

                                      ));
                this.setPropertyValue("liftMotorAngle",
                                      new NumberPrimitive(
                                              robot.getLego().getMotorServices().motorGetTachoCount(
                                                      ArmMotors.LIFT_MOTOR.getMark())
                                      ));
                this.setPropertyValue("rotateMotorAngle",
                                      new NumberPrimitive(
                                              robot.getLego().getMotorServices().motorGetTachoCount(
                                                      ArmMotors.ROTATE_MOTOR.getMark())
                                      ));
                this.setPropertyValue("handMotorAngle",
                                      new NumberPrimitive(
                                              robot.getLego().getMotorServices().motorGetTachoCount(
                                                      ArmMotors.HAND_MOTOR.getMark())
                                      ));
                this.setPropertyValue("date",
                                      new StringPrimitive(
                                              "NO DATA"
                                              //                                              generateString("Date:")
                                      ));
            } else {
                this.setPropertyValue("DeviceId", new StringPrimitive(""));
                this.setPropertyValue("gyroAngle", new NumberPrimitive(0));
                this.setPropertyValue("distance", new NumberPrimitive(0));
                this.setPropertyValue("color", new StringPrimitive(""));
                this.setPropertyValue("voltage", new NumberPrimitive(0));
                this.setPropertyValue("powerConsumption", new NumberPrimitive(0));
                this.setPropertyValue("liftMotorAngle", new NumberPrimitive(0));
                this.setPropertyValue("rotateMotorAngle", new NumberPrimitive(0));
                this.setPropertyValue("handMotorAngle", new NumberPrimitive(00));
                this.setPropertyValue("date", new StringPrimitive(""));
            }
            this.updateSubscribedProperties(10000);
        } catch (Exception e) {
            logger.error("Exception occurred while updating properties.", e);
        }
    }

    @ThingworxServiceDefinition(name = "DisconnectRobot",
                                description = "Disconnect motors and sensors")
    @ThingworxServiceResult(name = "result", description = "", baseType = "NOTHING")
    public void DisconnectRobot() {
        logger.debug("Disconnect agent application from robot");
        robot.getLego().disconnectMotorsAndSensors();
    }

    @ThingworxServiceDefinition(name = "StopAgent",
                                description = "Stop agent application. You have to disconnect " +
                                        "lego robot first if it is connected!")
    @ThingworxServiceResult(name = "result", description = "", baseType = "NOTHING")
    public void StopAgent() {
        logger.debug("STOP agent application");
        System.exit(0);
    }

    @ThingworxServiceDefinition(name = "DisconnectAndStop",
                                description = "Disconnect motors and sensors and stops agent " +
                                        "application")
    @ThingworxServiceResult(name = "result", description = "", baseType = "NOTHING")
    public void DisconnectAndStop() {
        if (robot.getLego().isConnected()) {
            DisconnectRobot();
        }
        StopAgent();
    }

    @ThingworxServiceDefinition(name = "ConnectRobot",
                                description = "Connect to LEgo EV3 and connect robot motors and " +
                                        "sensors")
    @ThingworxServiceResult(name = "result", description = "", baseType = "NOTHING")
    public void ConnectRobot() {
        logger.debug("Connect agent application with robot and initialize motors and sensors");
        robot.initialise();
    }


    @ThingworxServiceDefinition(name = "ResetMotorTacho",
                                description = "Reset motor tacho on selected port")
    @ThingworxServiceResult(name = "result", description = "", baseType = "NOTHING")
    public void ResetMotorTacho(
            @ThingworxServiceParameter(name = "port",
                                       description = "A, B, C or D only",
                                       baseType = "STRING")
                    String port) {
        logger.debug("Reset motor tacho for port [" + port + "]");
        robot.getLego().getMotorServices().motorResetTachoCount(LegoMotorMark.valueOf(port));
    }

    @ThingworxServiceDefinition(name = "GetMotorTacho",
                                description = "Get motor tacho value on selected port")
    @ThingworxServiceResult(name = "result", description = "", baseType = "INTEGER")
    public Integer GetMotorTacho(
            @ThingworxServiceParameter(name = "port",
                                       description = "A, B, C or D only",
                                       baseType = "STRING")
                    String port) {
        logger.debug("Get motor tacho for port [" + port + "]");
        return robot.getLego().getMotorServices().motorGetTachoCount(LegoMotorMark.valueOf(port));
    }

    @ThingworxServiceDefinition(name = "RotateMotor",
                                description = "Rotate motor on selected port")
    @ThingworxServiceResult(name = "result", description = "", baseType = "NOTHING")
    public void RotateMotor(
            @ThingworxServiceParameter(name = "port",
                                       description = "A, B, C or D only",
                                       baseType = "STRING")
                    String port,
            @ThingworxServiceParameter(name = "angle",
                                       description = "Angle value",
                                       baseType = "INTEGER")
                    Integer angle) throws RemoteException {
        logger.debug("Rotate motor for port [" + port + "], angle [" + angle + "]");
        robot.getLego().getMotorServices().motorRotate(LegoMotorMark.valueOf(port), angle);
    }

    @ThingworxServiceDefinition(name = "RotateMotorTo",
                                description = "RotateTo motor on selected port")
    @ThingworxServiceResult(name = "result", description = "", baseType = "NOTHING")
    public void RotateMotorTo(
            @ThingworxServiceParameter(name = "port",
                                       description = "A, B, C or D only",
                                       baseType = "STRING")
                    String port,
            @ThingworxServiceParameter(name = "angle",
                                       description = "Angle value",
                                       baseType = "INTEGER")
                    Integer angle) throws RemoteException {
        logger.debug("RotateTo motor for port [" + port + "], angle [" + angle + "]");
        robot.getLego().getMotorServices().motorRotateTo(LegoMotorMark.valueOf(port), angle);
    }

    @ThingworxServiceDefinition(name = "StartProgram1",
                                description = "Start arm program 1")
    @ThingworxServiceResult(name = "result", description = "", baseType = "NOTHING")
    public void StartProgram1() {
        logger.debug("Start for Program1 in agent requested");
        robot.getProgram1().startProgram();
    }

    @ThingworxServiceDefinition(name = "StopProgram1",
                                description = "Stop arm program 1")
    @ThingworxServiceResult(name = "result", description = "", baseType = "NOTHING")
    public void StopProgram1() {
        logger.debug("Stop for Program1 in agent requested");
        robot.getProgram1().stopProgram();
    }

    @ThingworxServiceDefinition(name = "GetProgram1Statistics",
                                description = "Get arm program 1 statistics")
    @ThingworxServiceResult(name = "result", description = "", baseType = "INFOTABLE")
    public InfoTable GetProgram1Statistics() {
        logger.debug("Get statistics for Program1 in agent requested");
        ProgramStatistics stats = robot.getProgram1().getProgramStatistics();

        final InfoTable infoTable = new InfoTable();
        FieldDefinition field = new FieldDefinition("color", BaseTypes.STRING);
        field.setOrdinal(1);
        infoTable.addField(field);
        field = new FieldDefinition("count", BaseTypes.INTEGER);
        field.setOrdinal(2);
        infoTable.addField(field);

        stats.getStatistics().forEach((singleStat) -> {
            ValueCollection collection = new ValueCollection();
            collection.put("color", new StringPrimitive(singleStat.getName()));
            collection.put("count", new NumberPrimitive(singleStat.getValue()));
            infoTable.addRow(collection);
        });

        return infoTable;
    }

    @ThingworxServiceDefinition(name = "GetProgram1Status",
                                description = "Get arm program 1 status (running or not)")
    @ThingworxServiceResult(name = "result", description = "", baseType = "BOOLEAN")
    public Boolean GetProgram1Status() {
        logger.debug("Get status for Program1 in agent requested");
        return robot.getProgram1().isProgramLaunched();
    }

}