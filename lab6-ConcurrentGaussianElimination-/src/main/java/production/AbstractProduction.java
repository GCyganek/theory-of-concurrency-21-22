package production;

import parallelism.MyLock;

public abstract class AbstractProduction<P> implements IProduction<P> {

    private MyLock myLock;
    private final PThread thread = new PThread();
    private final P obj1;
    private final P obj2;
    private P result;

    public AbstractProduction(P obj1, P obj2) {
        this.obj1 = obj1;
        this.obj2 = obj2;
    }

    @Override
    public void join() throws InterruptedException {
        thread.join();
    }

    @Override
    public void start() {
        thread.start();
    }

    @Override
    public void injectRefs(MyLock myLock) {
        this.myLock = myLock;
    }

    @Override
    public P getObj() {
        return this.result;
    }

    private class PThread extends Thread {

        @Override
        public void run() {
            myLock.lock();
            result = apply(obj1, obj2);
        }
    }
}
