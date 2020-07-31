package pl.ttpsc.lego.domain.sensor;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public abstract class SensorSample {
    private String sampleName;
    @JsonProperty(value = "sampleValue")
    public abstract String getSampleValue();
}
