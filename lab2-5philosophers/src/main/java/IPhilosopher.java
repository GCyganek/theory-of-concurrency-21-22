public interface IPhilosopher {
    void think() throws InterruptedException;
    void eat() throws InterruptedException;
    double getTimeSpentWaiting();
    int getId();
    boolean ateAllDishes();
}
