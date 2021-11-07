public class RunnableInc implements Runnable {
    Counter counter;

    public RunnableInc(Counter counter) {
        this.counter = counter;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10000; i++) {
            for (int j = 0; j < 10000; j++) {
                counter.incrementCounter();
            }
        }
    }
}
