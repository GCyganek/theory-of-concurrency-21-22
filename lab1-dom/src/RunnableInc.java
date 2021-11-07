public class RunnableInc implements Runnable {
    Counter counter;

    public RunnableInc(Counter counter) {
        this.counter = counter;
    }

    @Override
    public void run() {
        for (int i = 0; i < 1000000; i++) {
            counter.incrementCounter();
        }
    }
}
