import java.util.concurrent.Semaphore;

public class Table {
    private final Semaphore semaphore;

    public Table(int seats) {
        this.semaphore = new Semaphore(seats - 1);
    }

    public void takePlace() throws InterruptedException {
        semaphore.acquire();
    }

    public void freePlace() {
        semaphore.release();
    }

}
