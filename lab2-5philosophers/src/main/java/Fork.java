import java.util.concurrent.Semaphore;

public class Fork {
    private final int id;
    private final Semaphore semaphore;

    public Fork(int id) {
        this.id = id;
        this.semaphore = new Semaphore(1);
    }

    public void grabFork() throws InterruptedException {
        semaphore.acquire();
    }

    public void freeFork() {
        semaphore.release();
    }

    public boolean isFree() {
        return semaphore.availablePermits() == 1;
    }

    public int getId() {
        return id;
    }
}
