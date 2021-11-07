public class UnsyncCounter implements Counter {

    int count = 0;

    public UnsyncCounter() {
    }

    public void incrementCounter() {
        count++;
    }

    public void decrementCounter() {
        count--;
    }

    public int getCounter() {
        return count;
    }
}
