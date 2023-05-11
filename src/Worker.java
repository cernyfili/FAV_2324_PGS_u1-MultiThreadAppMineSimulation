import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 The Worker class represents a worker who can mine work units from a mine and load them onto trucks.
 Each worker is associated with a foreman and a mine, and can process a work block obtained from the foreman.
 The worker works in a separate thread and continues to process work blocks until there are no more work blocks left.
 */
public class Worker implements Runnable{
    private final static int LOAD_TIME_MS = 1000;

    private final static int MINE_MAXTIME_MS;

    static {
        Params params = Params.getInstance();
        MINE_MAXTIME_MS = params.gettWorkerMs();
    }

    private static final String WORKER_NAME = "Worker";

    private static int workerCount = 0;

    private String name;

    private Foreman foreman;

    private Mine mine;

    private int workUnitCount = 0;

    public Worker(Foreman foreman, Mine mine) {
        this.name = WORKER_NAME + workerCount++;
        this.foreman = foreman;
        this.mine = mine;
    }

    /**
     Processes a work block by mining work units from the mine.
     Each work unit is mined separately and added to the list of work results.
     The mining time of all work units is added together and logged.
     @param workBlock The work block to be processed.
     @return The list of work units mined.
     */
    private List<WorkUnit> processWorkBlock(WorkBlock workBlock){

        List<WorkUnit> workResult = new ArrayList<>();

        for (int i = 0; i < workBlock.getSourceCount(); i++) {
           workResult.add(mineWorkUnit());
        }

        double miningTimeMs = 0;
        for (WorkUnit workUnit :
                workResult) {
            miningTimeMs += workUnit.getMiningTimeMs();
        }

        MyLogger.logMassage(
                Worker.class.getSimpleName(),
                "Mined work block: " + workBlock.getName() + " by worker: " + this.name,
                (long) miningTimeMs
        );

        return workResult;
    }

    /**
     Mines a single work unit from the mine.
     A random number within a certain range is generated to simulate mining time.
     The worker sleeps for the generated amount of time and creates a new work unit with the mining time.
     The work unit is then logged and returned.
     @return The work unit mined.
     */
    private WorkUnit mineWorkUnit() {

        double randomNum =  getRandomNumber(0, MINE_MAXTIME_MS);
        workMs(randomNum);
        workUnitCount++;
        WorkUnit workUnit = new WorkUnit(randomNum);

        MyLogger.logMassage(
                Worker.class.getSimpleName(),
                "Mined work unit: " + workUnit.getName() + " by worker: " + this.name,
                (long) randomNum
        );

        return workUnit;
    }

    /**
     Generates a random number within a certain range.
     @param min The minimum value of the range.
     @param max The maximum value of the range.
     @return The generated random number.
     */
    private double getRandomNumber(double min, double max) {
        return ((Math.random() * (max - min)) + min);
    }

    /**
     Suspends the thread for the specified amount of time.
     @param workTimeMs the amount of time to suspend the thread in milliseconds
     */
    private void workMs(double workTimeMs){
        try {
            TimeUnit.MILLISECONDS.sleep((long) workTimeMs);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     Returns the number of work units processed by this worker.
     @return the number of work units processed by this worker
     */
    public int getWorkUnitCount() {
        return workUnitCount;
    }

    public String getName() {
        return name;
    }

    /**
     Runs the worker's task of processing work blocks received from the foreman.
     Continuously retrieves work blocks from the foreman and processes them until there are no more work blocks left.
     For each work block, processes the number of work units specified in the work block and logs
     the mining time for the block.
     Loads each work unit into a truck and releases the semaphore to signal the availability of a
     truck for the mine to load more work units.
     */
    @Override
    public void run() {
        WorkBlock workBlock = foreman.getWorkBlock();

        while(workBlock != null){
            List<WorkUnit> workResult = processWorkBlock(workBlock);
            //System.out.println("Worker: " + this.name + " finished work block: " + workBlock.getName());

            for (WorkUnit workUnit :
                    workResult) {
                try {
                    mine.getTruckSempahore().acquire();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                Truck mineTruck = mine.getCurrentTruck();
                loadTruck(mineTruck, workUnit);


                mine.getTruckSempahore().release();
            }

            workBlock = foreman.getWorkBlock();
        }
    }

    /**
     Loads a work unit into the specified truck and suspends the thread for the loading time.
     @param mineTruck the truck to load the work unit into
     @param workUnit the work unit to be loaded into the truck
     */
    private void loadTruck(Truck mineTruck, WorkUnit workUnit) {
        //System.out.println("Worker: " + this.name + " loading work unit: " + workUnit.getName() + " into truck: " + mineTruck.getName());
        workMs(LOAD_TIME_MS);
        mineTruck.loadWorkUnit(workUnit);
    }
}
