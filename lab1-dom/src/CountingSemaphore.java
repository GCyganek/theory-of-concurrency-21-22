public class CountingSemaphore implements ISemaphore {
    private int count;
    private final ISemaphore countLock = new Semaphore();
    private final ISemaphore sectionLock = new Semaphore();

    public CountingSemaphore(int count) {
        this.count = count;

        if (count <= 0) {
            throw new IllegalArgumentException("Count must be a positive number");
        }
    }


    @Override
    public void P() {
        countLock.P();
        count--;
        if (count <= 0) {
            countLock.V();
            sectionLock.P();
        }
        else countLock.V();
    }

    @Override
    public void V() {
        countLock.P();
        count++;
        if (count <= 0) sectionLock.V();
        countLock.V();
    }
}
