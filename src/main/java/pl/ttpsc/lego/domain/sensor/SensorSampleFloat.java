package pl.ttpsc.lego.domain.sensor;

import lombok.Data;

@Data
public class SensorSampleFloat extends SensorSample {

    private float sampleValue;

    @Override
    public String getSampleValue() {
        return String.valueOf(sampleValue);
    }
}
