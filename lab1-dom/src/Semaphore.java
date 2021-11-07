public class Semaphore implements ISemaphore {
    // state = true -> semaphore is free to use
    private boolean state = true;
    private int waiting = 0;

    public Semaphore() {

    }

    // occupy semaphore
    public synchronized void P() {
        while (!state) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        state = false;
        this.notifyAll();
    }

    // free semaphore
    public synchronized void V() {
        while (state) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        state = true;
        this.notifyAll();
    }
}
