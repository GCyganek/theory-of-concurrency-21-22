public class Demo {
    public static void main(String[] args) throws InterruptedException {
        IfSemaphore ifSemaphore = new IfSemaphore();
        Counter ifCounter = new Counter(ifSemaphore);

        Semaphore semaphore = new Semaphore();
        Counter counter = new Counter(semaphore);

        System.out.println("Correct binary semaphore used:");
        semaphoreExampleTestTwoThreads(counter);

        Thread.sleep(3000);

        System.out.println("Counting semaphore test:");
        countingSemaphoreExampleTest(new CountingSemaphore(3));

        Thread.sleep(10000);

        System.out.println("Binary semaphore with ifs instead of whiles used:");
        semaphoreExampleTestTwoThreads(ifCounter);
    }

    public static void semaphoreExampleTestTwoThreads(Counter counter) {
        Thread inc = new Thread(new RunnableInc(counter));
        Thread dec = new Thread(new RunnableDec(counter));

        inc.start();
        dec.start();

        try {
            inc.join();
            dec.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\tFinal count: " + counter.getCounter());
    }

    public static void countingSemaphoreExampleTest(CountingSemaphore countingSemaphore) throws InterruptedException {
        Thread[] threads = new Thread[10];

        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(new RunnablePrint(countingSemaphore));
        }

        for (Thread thread: threads) {
            thread.start();
        }

        for (Thread thread: threads) {
            thread.join();
        }
    }
}
