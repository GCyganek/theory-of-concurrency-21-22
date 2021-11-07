public class Demo {
    public static void main(String[] args) throws InterruptedException {
        UnsyncCounter unsyncCounter = new UnsyncCounter();
        SyncCounter syncCounter = new SyncCounter();

        // unsynchronized counter
        System.out.println("Unsynchronized counter case:");
        testThreadsWithCounter(unsyncCounter);

        Thread.sleep(3000);

        // synchronized counter
        System.out.println("Synchronized counter case:");
        testThreadsWithCounter(syncCounter);
    }

    public static void testThreadsWithCounter(Counter counter) {
        Thread inc = new Thread(new RunnableInc(counter));
        Thread dec = new Thread(new RunnableDec(counter));

        long start = System.currentTimeMillis();

        inc.start();
        dec.start();

        try {
            inc.join();
            dec.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long end = System.currentTimeMillis();

        long elapsedTime = end - start;
        System.out.println("\tTime: " + elapsedTime);

        System.out.println("\tFinal count: " + counter.getCounter());
    }
}
