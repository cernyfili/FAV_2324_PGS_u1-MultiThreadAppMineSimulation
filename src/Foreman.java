import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 The Foreman class represents a manager that coordinates the work of a group of workers
 who are responsible for extracting resources from a mine and transporting them to a processing plant.
 It manages a stack of work blocks that have to be executed and creates a pool of worker threads
 to execute these blocks.
 */
public class Foreman {
    // maximum amount of time to wait for thread completion
    private static final long THREADS_TIMEOUT_MIN = 60; //5h
    private String name;
    private LinkedList<WorkBlock> workBlockStack;

    // Counts of work blocks and work sources
    private int workBlockCount = 0;

    private int workSourceCount = 0;

    // List of worker threads
    private List<Worker> workerList = new ArrayList<>();

    // Executor service for workers
    private ExecutorService workerES = Executors.newCachedThreadPool();

    private Mine mine;

    private static final String FOREMAN_NAME = "Foreman";

    private static int foremanCount = 0;

    /**
     * Constructor that initializes the Foreman object with the given mine reference.
     * @param mine reference to the mine where resources are extracted
     */
    public Foreman(Mine mine) {
        this.name = FOREMAN_NAME + foremanCount++;
        this.mine = mine;
    }

    /**
     * Starts the worker threads and waits for them to finish their assigned work.
     * After the workers have finished, checks if there is any remaining truck that is not full yet.
     * @param numWorkers number of worker threads to be created
     */
    private void startWorkers(int numWorkers){
        for (int i = 0; i < numWorkers; i++) {
            Worker worker = new Worker(this, mine);
            workerES.execute(worker);

            workerList.add(worker);
        }

        workerES.shutdown();

        boolean workerThreadsFinished = false;

        try {
            //waiting until all workers thread end
            workerThreadsFinished = workerES.awaitTermination(THREADS_TIMEOUT_MIN, TimeUnit.MINUTES);

            //send last truck to ferry
            mine.checkCurrentTruckFull();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (workerThreadsFinished == false){
            MyLogger.error("Thread reach max time for runtime - process didnt finished");
        }
    }

    /**
     * Starts the workers and assigns them work blocks from the given work plan file.
     * @param workPlanFile file containing work blocks to be executed
     * @param workersCount number of worker threads to be created
     */
    public void startWork(File workPlanFile, int workersCount){
        findWorkBlocks(workPlanFile);

        startWorkers(workersCount);
    }

    /**
     * Finds work blocks from the given work plan file and initializes the work block stack.
     * @param workPlanFile file containing work blocks to be executed
     */
    private void findWorkBlocks(File workPlanFile){
        List<WorkBlock> workBlockList;

        try {
            workBlockList = Parser.parseWorkBlocks(workPlanFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        workBlockCount = workBlockList.size();
        for (WorkBlock workBlock :
                workBlockList) {
            workSourceCount += workBlock.getSourceCount();
        }

        System.out.println("Found work blocks: " + workBlockCount);
        System.out.println("Found work sources: " + workSourceCount);

        MyLogger.logMessage(
                Foreman.class.getSimpleName(),
                "Found work blocks: " + workBlockCount + ", Found work sources: " + workSourceCount
        );



        workBlockStack = new LinkedList<>(workBlockList);
    }

    public synchronized WorkBlock getWorkBlock(){
        if (workBlockStack.isEmpty()) return null;

        return workBlockStack.pop();
    }

    public List<Worker> getWorkerList() {
        return workerList;
    }

    public ExecutorService getWorkerES() {
        return workerES;
    }
}
