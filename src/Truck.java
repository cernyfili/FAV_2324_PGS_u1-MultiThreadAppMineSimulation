import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.TimeUnit;

/**
 The Truck class represents a truck that can transport work units from the mine to the ferry.
 A truck can hold a maximum number of work units specified in the parameters, and has a maximum
 time to ride also specified in the parameters. When a truck is full, it moves to the ferry to wait
 for other trucks to be loaded. When the ferry is full, the trucks depart together to the destination.
 */
public class Truck implements Runnable{

    private List<WorkUnit> load = new ArrayList<>();

    private static final String TRUCK_NAME = "Truck";

    private Mine mine;

    private static int truckCount = 0;

    private String name;

    private final static int LOAD_MAX;

    private final static int RIDE_MAXTIME_MS;

    private long timeConstructed;

    private boolean isLastTruck = false;

    static{
        Params params = Params.getInstance();
        LOAD_MAX = params.getCapLorry();
        RIDE_MAXTIME_MS = params.gettLorryMs();
    }

    public Truck(Mine mine) {

        this.mine = mine;

        name = TRUCK_NAME + truckCount++;


        timeConstructed = System.currentTimeMillis();
    }


    /**
     Loads a work unit onto the truck.
     @param workUnit The work unit to be loaded onto the truck.
     */
    public synchronized void loadWorkUnit(WorkUnit workUnit){
        load.add(workUnit);
    }

    /**
     Checks whether the truck is full.
     @return True if the truck is full, false otherwise.
     */
    public synchronized boolean isFull() {
        return load.size() >= LOAD_MAX;
    }

    /**
     Generates a random number within a given range.
     @param min The minimum value of the range.
     @param max The maximum value of the range.
     @return A random double within the given range.
     */
    private double getRandomNumber(double min, double max) {
        return ((Math.random() * (max - min)) + min);
    }

    /**
     Causes the current thread to sleep for a specified number of milliseconds.
     @param timeMs The number of milliseconds to sleep.
     */
    private void rideMs(double timeMs){
        try {
            TimeUnit.MILLISECONDS.sleep((long) timeMs);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     Runs the truck thread. The truck moves to the ferry when it is full, waits for other trucks to be loaded
     onto the ferry, and then moves to the final destination.
     */
    @Override
    public void run() {
        Ferry ferry = mine.getFerry();

        long endTime = System.currentTimeMillis();
        if (isLastTruck){
            MyLogger.logMessage(
                    Truck.class.getSimpleName(),
                    "Truck: " + this.name + " drove off with load: " + load.size(),
                    endTime - timeConstructed
            );
        }
        else {
            MyLogger.logMessage(
                    Truck.class.getSimpleName(),
                    "Truck: " + this.name + " is full and drove off with load: " + load.size(),
                    endTime - timeConstructed
            );
        }

        double randomNum = getRandomNumber(0,RIDE_MAXTIME_MS);

        rideMs(randomNum);

        MyLogger.logMessage(
                Truck.class.getSimpleName(),
                "Truck: " + this.name + " arrived to Ferry: " + ferry.getName(),
                (long) randomNum
        );

        try {

            ferry.getThreadsBarrier().await();//waitin for ferry to be full
        } catch (InterruptedException | BrokenBarrierException e) {
            throw new RuntimeException(e);
        }


        //ride to destination
        randomNum = getRandomNumber(0,RIDE_MAXTIME_MS);

        rideMs(randomNum);

        MyLogger.logMessage(
                Truck.class.getSimpleName(),
                "Truck: " + this.name + " arrived to final destination",
                (long) randomNum
        );

        mine.incWorkUnitImportedCount(load.size());

    }



    public String getName() {
        return name;
    }

    /**
     * Sets lastTruck value which depards ferry with last truck in run()
     * even without ferry fullcapacity
     * @param lastTruck lasttruck
     */
    public void setLastTruck(boolean lastTruck) {
        isLastTruck = lastTruck;
    }
}
