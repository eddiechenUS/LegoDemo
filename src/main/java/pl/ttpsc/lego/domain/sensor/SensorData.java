package pl.ttpsc.lego.domain.sensor;

import lombok.Data;

import java.util.List;

/**
 * Compound list of SensorRead objects
 */
@Data
public class SensorData {

    private List<SensorRead> sensors;

}
