package pl.ttpsc.lego.name;

import lombok.Getter;

public enum LegoMotorMark {
    A(1),
    B(2),
    C(3),
    D(4);

    @Getter
    private final int value;

    LegoMotorMark(final int i) {
        this.value = i;
    }

    public static LegoMotorMark numberToMark(Integer motorId) {
        for (LegoMotorMark e : values()) {
            if (motorId.equals(e.value)) {
                return e;
            }
        }
        return null;
    }

    public static String numberToName(Integer motorId) {
        return numberToMark(motorId).name();
    }
}
