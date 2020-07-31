package pl.ttpsc.lego.domain.statistic;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public interface ProgramStatistics {

    @JsonProperty(value = "statistics")
    List<SingleStat> getStatistics();

}
