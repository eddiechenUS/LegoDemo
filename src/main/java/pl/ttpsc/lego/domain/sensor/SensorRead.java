package pl.ttpsc.lego.domain.sensor;

import lombok.Data;

import java.util.List;

@Data
public class SensorRead {

    private String sensorName;
    private List<SensorSample> sensorSamples;

}
