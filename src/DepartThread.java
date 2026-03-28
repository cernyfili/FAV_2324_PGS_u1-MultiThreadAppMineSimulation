public class DepartThread implements Runnable{
    private Ferry ferry;

    public DepartThread(Ferry ferry) {
        this.ferry = ferry;
    }

    @Override
    public void run() {
        ferry.departFerry();
    }
}
