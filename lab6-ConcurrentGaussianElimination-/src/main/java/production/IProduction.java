package production;

import parallelism.MyLock;

public interface IProduction<P> {

    P apply(P obj1, P obj2);

    void join() throws InterruptedException;

    void start();

    void injectRefs(MyLock myLock);

    P getObj();
}
