/**
 * This class represents a work unit that is mined by a worker in a mine.
 * Each work unit has a unique name and a mining time in milliseconds.
 * The class provides methods to get the name and mining time of the work unit.
 */
public class WorkUnit {
    private String name;

    private static final String WORKUNIT_NAME = "WorkUnit";

    private static int workUnitCount = 0;

    private double miningTimeMs;

    public WorkUnit(double miningTimeMs) {
        this.miningTimeMs = miningTimeMs;
        this.name = WORKUNIT_NAME + workUnitCount++;
    }

    public synchronized String getName() {
        return name;
    }

    public synchronized double getMiningTimeMs() {
        return miningTimeMs;
    }
}
