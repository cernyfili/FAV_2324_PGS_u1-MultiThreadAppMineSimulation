import java.util.List;

/**
 * This class represents a Singleton design pattern that holds the
 * parameters necessary for the application to run.
 * It provides methods for setting and getting these parameters.
 */
public class Params {
    private static Params single_instance = null;

    private String dataFilePath = null;

    private String outputFilePath = null;

    private Integer cWorker = null;

    private Integer tWorkerMs = null;

    private Integer tLorryMs = null;

    private Integer capLorry = null;

    private Integer capFerry = null;

    public static synchronized Params getInstance() {
        if (single_instance == null)
            single_instance = new Params();

        return single_instance;
    }

    public void setAllParams(List<String> values) {

        if(values.size() != 7) {
            MyLogger.error("Error when reading arguments");
            System.exit(0);
        }

        setDataFilePath(values.get(0));
        setOutputFilePath(values.get(1));
        setcWorker(values.get(2));
        settWorkerMs(values.get(3));
        setCapLorry(values.get(4));
        settLorryMs(values.get(5));
        setCapFerry(values.get(6));
    }

    private void setcWorker(String cWorker) {
        if(this.cWorker == null) {
            this.cWorker = Integer.parseInt(cWorker);
        }
    }

    private void settWorkerMs(String tWorkerMs) {
        if(this.tWorkerMs == null) {
            this.tWorkerMs = Integer.parseInt(tWorkerMs);
        }
    }

    private void settLorryMs(String tLorryMs) {
        if(this.tLorryMs == null) {
            this.tLorryMs = Integer.parseInt(tLorryMs);
        }
    }

    private void setCapLorry(String capLorry) {
        if(this.capLorry == null) {
            this.capLorry = Integer.parseInt(capLorry);
        }
    }

    private void setCapFerry(String capFerry) {
        if(this.capFerry == null) {
            this.capFerry = Integer.parseInt(capFerry);
        }
    }

    private void setDataFilePath(String dataFilePath) {
        if(this.dataFilePath == null) {
            this.dataFilePath = dataFilePath;
        }
    }

    private void setOutputFilePath(String outputFilePath) {
        if(this.outputFilePath == null) {
            this.outputFilePath = outputFilePath;
        }
    }

    public String getDataFilePath() {
        return dataFilePath;
    }

    public String getOutputFilePath() {
        return outputFilePath;
    }

    public Integer getcWorker() {
        return cWorker;
    }

    public Integer gettWorkerMs() {
        return tWorkerMs;
    }

    public Integer gettLorryMs() {
        return tLorryMs;
    }

    public Integer getCapLorry() {
        return capLorry;
    }

    public Integer getCapFerry() {
        return capFerry;
    }

    @Override
    public String toString() {
        return "dataFilePath = " + dataFilePath + '\n' +
                "outputFilePath = " + outputFilePath + '\n' +
                "cWorker = " + cWorker + '\n' +
                "tWorker = " + tWorkerMs + '\n' +
                "tLorry = " + tLorryMs + '\n' +
                "capLorry = " + capLorry + '\n' +
                "capFerry = " + capFerry + '\n'
                ;
    }
}
