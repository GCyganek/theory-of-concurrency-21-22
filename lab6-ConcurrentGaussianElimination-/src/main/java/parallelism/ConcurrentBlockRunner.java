package parallelism;

import production.IProduction;

import java.util.AbstractQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConcurrentBlockRunner implements BlockRunner {

    private final MyLock lock = new MyLock();
    private final AbstractQueue<IProduction> list = new ConcurrentLinkedQueue<>();

    @Override
    public void startAll() {
        for (IProduction production : list) {
            runOne(production);
        }
        wakeAll();
        for (IProduction production : list) {
            try {
                production.join();
            } catch (InterruptedException e) {
                Logger.getLogger(ConcurrentBlockRunner.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        list.clear();
    }

    @Override
    public void addThread(IProduction pThread) {
        list.add(pThread);
    }

    void runOne(IProduction production) {
        production.injectRefs(lock);
        production.start();
    }

    void wakeAll() {
        lock.unlock();
    }
}
