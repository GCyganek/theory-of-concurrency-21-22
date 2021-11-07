public class RunnableDec implements Runnable {
    Counter counter;

    public RunnableDec(Counter counter) {
        this.counter = counter;
    }

    @Override
    public void run() {
        for (int i = 0; i < 1000000; i++) {
            counter.decrementCounter();
        }
    }
}
