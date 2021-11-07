public class Counter {

    private int count = 0;
    private final ISemaphore semaphore;

    public Counter(ISemaphore semaphore) {
        this.semaphore = semaphore;
    }

    public void incrementCounter() {
        semaphore.P();
        count++;
        semaphore.V();
    }

    public void decrementCounter() {
        semaphore.P();
        count--;
        semaphore.V();
    }

    public int getCounter() {
        return count;
    }
}
