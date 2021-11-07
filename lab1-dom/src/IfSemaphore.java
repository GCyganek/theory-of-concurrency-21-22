public class IfSemaphore implements ISemaphore {
    private boolean state = true;
    private int waiting = 0;

    public IfSemaphore() {

    }

    public synchronized void P() {
        if (!state) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        state = false;
        this.notifyAll();
    }

    public synchronized void V() {
        if (state) {
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
