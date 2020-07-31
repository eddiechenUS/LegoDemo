package pl.ttpsc.lego.name;

import lombok.Getter;

public enum LegoSensorType {
    Angle("lejos.hardware.sensor.EV3GyroSensor",1),
    Distance("lejos.hardware.sensor.EV3UltrasonicSensor",2),
    ColorID("lejos.hardware.sensor.EV3ColorSensor", 3),
    Seek("lejos.hardware.sensor.EV3IRSensor", 4),
    Touch("lejos.hardware.sensor.EV3TouchSensor", 5),
    Listen("lejos.hardware.sensor.EV3UltrasonicSensor", 6);
    @Getter
    private final String sensorClass;

    @Getter
    private final int value;

    LegoSensorType(final String sensorClass, final int i) {
        this.sensorClass = sensorClass;
        this.value = i;
    }

    public static LegoSensorType numberToType(Integer typeId) {
        for (LegoSensorType e : values()) {
            if (typeId.equals(e.value)) {
                return e;
            }
        }
        return null;
    }

    public static String numberToName(Integer typeId) {
        return numberToType(typeId).name();
    }
}
