public class SyncCounter implements Counter {

    int count = 0;

    public SyncCounter() {
    }

    public synchronized void incrementCounter() {
        count++;
    }

    public synchronized void decrementCounter() {
        count--;
    }

    public int getCounter() {
        return count;
    }
}
