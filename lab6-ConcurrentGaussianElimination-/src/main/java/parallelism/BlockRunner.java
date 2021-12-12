package parallelism;

import production.IProduction;

public interface BlockRunner {

    //starts all threads
    void startAll();

    //adds a thread to poll
    void addThread(IProduction pThread);
}
