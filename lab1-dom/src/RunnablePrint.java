public class RunnablePrint implements Runnable {
    private final CountingSemaphore countingSemaphore;

    public RunnablePrint(CountingSemaphore countingSemaphore) {
        this.countingSemaphore = countingSemaphore;
    }

    @Override
    public void run() {
        countingSemaphore.P();
        System.out.println("\t" + Thread.currentThread().getId() + ": work started");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("\t" + Thread.currentThread().getId() + ": work ended");
        countingSemaphore.V();
    }
}