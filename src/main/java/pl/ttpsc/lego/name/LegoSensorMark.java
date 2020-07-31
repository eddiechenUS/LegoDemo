package pl.ttpsc.lego.name;

import lombok.Getter;

public enum LegoSensorMark {
    S1(1),
    S2(2),
    S3(3),
    S4(4);

    @Getter
    private final int value;
    
    LegoSensorMark(final int i) {
        this.value = i;
    }

    public static LegoSensorMark numberToMark(Integer sensorId) {
        for (LegoSensorMark e : values()) {
            if (sensorId.equals(e.value)) {
                return e;
            }
        }
        return null;
    }

    public static String numberToName(Integer sensorId) {
        return numberToMark(sensorId).name();
    }
}
