import java.io.File;
import java.util.StringJoiner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Mine {
    // maximum amount of time to wait for thread completion
    private static final long THREADS_MAXTIME_MIN = 300; //5h

    // the truck that is currently being loaded with work units
    private Truck currentTruck;

    // the maximum number of trucks allowed in the mine at any given time
    private static final int TRUCK_PERMITS = 1;

    // Semaphore to regulate access to the trucks
    private final Semaphore truckSemaphore;

    // the ferry that transports loaded trucks across the river
    private Ferry ferry;

    private Foreman foreman;

    // the number of work units imported into the mine
    private int workUnitImportedCount = 0;

    private ExecutorService truckES = Executors.newCachedThreadPool();

    public Mine() {
        ferry = new Ferry();
        currentTruck = new Truck(this);
        truckSemaphore = new Semaphore(TRUCK_PERMITS);
    }

    /**
     * Gets the current truck that is being loaded with work units.
     * If the truck is already full, it will be sent to the ferry and a new truck will be created.
     *
     * @return the current truck being loaded with work units
     */
    public synchronized Truck getCurrentTruck() {
        if(currentTruck.isFull()){
            //send full truck to ferry
            truckES.execute(currentTruck);

            currentTruck = new Truck(this);
        }

        return currentTruck;
    }

    public synchronized Ferry getFerry() {
        return ferry;
    }

    /**
     * Gets the semaphore object used for limiting the number of trucks that can access the mine at once.
     *
     * @return the semaphore object used for limiting the number of trucks that can access the mine at once
     */
    public synchronized Semaphore getTruckSemaphore() {
        return truckSemaphore;
    }

    /**
     * Starts the simulation by creating a Foreman object and calling the startWork() method.
     * This method reads the work plan file and starts the worker threads to mine work units.
     */
    public void startSimulation() {
        foreman = new Foreman(this);

        Params params = Params.getInstance();
        File workPlanFile = new File(params.getDataFilePath());
        int workerCount = params.getcWorker();

        foreman.startWork(workPlanFile, workerCount);
    }

    /**
     * Increases the number of work units imported
     *
     * @param inc the amount to increase the work unit imported count by
     */
    public synchronized void incWorkUnitImportedCount(int inc) {
        workUnitImportedCount+= inc;
    }

    public int getWorkUnitImportedCount() {
        return workUnitImportedCount;
    }

    /**
     * Returns a string containing the number of work units each worker has completed.
     *
     * @return a string containing the number of work units each worker has completed
     */
    public String getWorkersWorkUnitCountString(){
        StringJoiner stringJoiner = new StringJoiner("\n");
        for (Worker worker :
                foreman.getWorkerList()) {
            stringJoiner.add(worker.getName() + ": " + worker.getWorkUnitCount());
        }

        return stringJoiner.toString();
    }

    /**
     * Waits until all worker and truck threads have finished executing.
     * This method uses the ExecutorService.awaitTermination() method to wait for the specified amount of time.
     */
    public void waitUntilAllThreadsFinished() {

        boolean workerThreadsFinished = true;
        boolean truckThreadsFinished = true;

        try {
            workerThreadsFinished = foreman.getWorkerES().awaitTermination(THREADS_MAXTIME_MIN, TimeUnit.MINUTES);
            truckThreadsFinished = truckES.awaitTermination(THREADS_MAXTIME_MIN, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        truckES.shutdown();

        if (workerThreadsFinished == false || truckThreadsFinished == false){
            MyLogger.error("Thread reach max time for runtime - process didnt finished");
        }
    }

    /**
     Checks if the current truck is full and sends it to the ferry if it is.
     The current truck is then set to null and the truck executor service is shutdown.
     */
    public synchronized void sendLastTruckToFerry() {
        if(currentTruck != null){
            //for different log
            currentTruck.setLastTruck(true);

            //send last truck to ferry
            truckES.execute(currentTruck);
            currentTruck = null;
            truckES.shutdown();
        }
    }
}
