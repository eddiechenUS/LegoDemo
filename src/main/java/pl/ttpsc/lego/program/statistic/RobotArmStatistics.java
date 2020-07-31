package pl.ttpsc.lego.program.statistic;

import pl.ttpsc.lego.domain.statistic.ProgramStatistics;
import pl.ttpsc.lego.domain.statistic.SingleStat;

import java.util.ArrayList;
import java.util.List;

public class RobotArmStatistics implements ProgramStatistics {

    public static final String RED_BLOCKS = "redBlocks";
    public static final String GREEN_BLOCKS = "greenBlocks";
    public static final String BLUE_BLOCKS = "blueBlocks";
    public static final String UNKNOWN_BLOCKS = "unknownBlocks";
    public static final String TOTAL = "total";

    private final List<SingleStat> singleStats;

    private SingleStat redStats;
    private SingleStat greenStats;
    private SingleStat blueStats;
    private SingleStat unknownStats;
    private SingleStat total;

    public RobotArmStatistics() {
        this.singleStats = new ArrayList<>();

        redStats = new SingleStat();
        redStats.setName(RED_BLOCKS);
        singleStats.add(redStats);

        greenStats = new SingleStat();
        greenStats.setName(GREEN_BLOCKS);
        singleStats.add(greenStats);

        blueStats = new SingleStat();
        blueStats.setName(BLUE_BLOCKS);
        singleStats.add(blueStats);

        unknownStats = new SingleStat();
       unknownStats.setName(UNKNOWN_BLOCKS);
        singleStats.add(unknownStats);

        total = new SingleStat();
        total.setName(TOTAL);
        singleStats.add(total);
    }

    @Override
    public List<SingleStat> getStatistics() {
        return singleStats;
    }

    public void increaseRed() {
        redStats.setValue(redStats.getValue() + 1);
        increaseTotal();
    }

    public void increaseBlue() {
        blueStats.setValue(blueStats.getValue() + 1);
        increaseTotal();
    }

    public void increaseGreen() {
        greenStats.setValue(greenStats.getValue() + 1);
        increaseTotal();
    }

    public void increaseUnknown() {
        unknownStats.setValue(unknownStats.getValue() + 1);
        increaseTotal();
    }

    private void increaseTotal() {
        total.setValue(total.getValue() + 1);
    }
}
