package pl.ttpsc.lego.name;

import lombok.Getter;

public enum LegoMotorType {
    M(1),
    L(2);

    @Getter
    private final int value;

    LegoMotorType(final int i) {
        this.value = i;
    }

    public static LegoMotorType numberToType(Integer typeId) {
        for (LegoMotorType e : values()) {
            if (typeId.equals(e.value)) {
                return e;
            }
        }
        return null;
    }

    public static String numberToName(Integer typeId) {
        return numberToType(typeId).name();
    }

    public static char numberToCharName(Integer typeId) {
        return numberToType(typeId).name().charAt(0);
    }

    public char nameChar() {
        return this.name().charAt(0);
    }
}
