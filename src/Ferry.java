import java.util.concurrent.CountDownLatch;

/**
 * Class represents ferry which
 * carries Truck to the other end of river
 */
public class Ferry {
    private String name;

    // Name constant for ferry
    private static final String FERRY_NAME = "Ferry";

    // Counter for ferry instances
    private static int ferryCount = 0;

    // Countdown latch for maximum capacity of the ferry when dealing with truck threads
    private CountDownLatch ferryLetch;

    // Maximum load capacity of the ferry
    private static final int MAX_LOAD;

    // Start time of loading on the ferry
    private long startTimeLoading;

    // Static initialization block to set the maximum load capacity based on the value from the configuration file
    static {
        Params params = Params.getInstance();
        MAX_LOAD = params.getCapFerry();
    }

    /**
     Constructor for creating a new instance of the ferry with a unique name and a new countdown latch for maximum capacity.
     */
    public Ferry() {
        this.name = FERRY_NAME + ferryCount++;

        ferryLetch = new CountDownLatch(MAX_LOAD);

        startTimeLoading = System.currentTimeMillis();
    }

    /**

     Method for departing the ferry and writing a log message.

     It resets the countdown latch for maximum capacity and updates the start time for loading.
     */
    public synchronized void departFerry(){
        long endTime = System.currentTimeMillis();
        MyLogger.logMassage(
                Ferry.class.getSimpleName(),
                "Ferry: " + this.name + " is full",
                endTime - startTimeLoading
        );
        System.out.println("Ferry: " + name + " departed");

        ferryLetch = new CountDownLatch(MAX_LOAD);

        startTimeLoading = System.currentTimeMillis();
    }

    public synchronized CountDownLatch getFerryLetch() {
        return ferryLetch;
    }

    public String getName() {
        return name;
    }
}
