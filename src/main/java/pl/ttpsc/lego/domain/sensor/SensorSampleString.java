package pl.ttpsc.lego.domain.sensor;

import lombok.Data;

@Data
public class SensorSampleString extends SensorSample{

    private String sampleValue;

    @Override
    public String getSampleValue() {
        return sampleValue;
    }
}
