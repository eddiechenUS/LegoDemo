package pl.ttpsc.lego.robot.bindings;

import lombok.Getter;
import pl.ttpsc.lego.name.LegoMotorMark;
import pl.ttpsc.lego.name.LegoMotorType;

public enum ArmMotors {
    ROTATE_MOTOR(LegoMotorMark.C, LegoMotorType.L),
    LIFT_MOTOR(LegoMotorMark.B,LegoMotorType.L),
    HAND_MOTOR(LegoMotorMark.D,LegoMotorType.M);

    @Getter
    private final LegoMotorMark mark;

    @Getter
    private final LegoMotorType type;

    ArmMotors(final LegoMotorMark mark, final LegoMotorType type) {
        this.mark = mark;
        this.type = type;
    }

    public static ArmMotors markToArmMotor(LegoMotorMark mark) {
        for (ArmMotors e : values()) {
            if (mark.equals(e.mark)) {
                return e;
            }
        }
        return null;
    }
}
