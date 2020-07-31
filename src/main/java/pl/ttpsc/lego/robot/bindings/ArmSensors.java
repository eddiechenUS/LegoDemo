package pl.ttpsc.lego.robot.bindings;

import lombok.Getter;
import pl.ttpsc.lego.name.LegoSensorMark;
import pl.ttpsc.lego.name.LegoSensorType;

public enum ArmSensors {
    DISTANCE(LegoSensorMark.S3, LegoSensorType.Distance),
    COLOR(LegoSensorMark.S4, LegoSensorType.ColorID),
    GYRO(LegoSensorMark.S2, LegoSensorType.Angle),
    TOUCH(LegoSensorMark.S1, LegoSensorType.Touch);

    @Getter
    private final LegoSensorMark mark;

    @Getter
    private final LegoSensorType type;

    ArmSensors(final LegoSensorMark mark, final LegoSensorType type) {
        this.mark = mark;
        this.type = type;
    }

    public static ArmSensors markToArmSensor(LegoSensorMark mark) {
        for (ArmSensors e : values()) {
            if (mark.equals(e.mark)) {
                return e;
            }
        }
        return null;
    }

}
