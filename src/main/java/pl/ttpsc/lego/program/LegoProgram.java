package pl.ttpsc.lego.program;

import pl.ttpsc.lego.domain.statistic.ProgramStatistics;

public interface LegoProgram {

    void startProgram();

    void stopProgram();

    ProgramStatistics getProgramStatistics();

    Boolean isProgramLaunched();

}
