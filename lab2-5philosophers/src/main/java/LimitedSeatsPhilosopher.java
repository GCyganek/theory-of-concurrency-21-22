import java.util.concurrent.ThreadLocalRandom;

public class LimitedSeatsPhilosopher implements IPhilosopher {
    private final int id;
    private final Fork leftFork;
    private final Fork rightFork;
    private int dishesToEat;
    private long timeSpentWaiting;
    private final Table table;


    public LimitedSeatsPhilosopher(int id, Fork leftFork, Fork rightFork, int dishesToEat, Table table) {
        this.id = id;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
        this.dishesToEat = dishesToEat;
        this.table = table;
        timeSpentWaiting = 0;
    }

    public boolean ateAllDishes() {
        return dishesToEat == 0;
    }

    public void think() throws InterruptedException {
        int randomInt = ThreadLocalRandom.current().nextInt(20, 81);
        System.out.println("Philosopher " + id + ": thinking...");
        Thread.sleep(randomInt);
    }

    public void eat() throws InterruptedException {
        long waitingStart = System.currentTimeMillis();
        System.out.println("Philosopher " + id + ": trying to grab a seat...");
        table.takePlace();
        System.out.println("Philosopher " + id + ": trying to get forks...");
        leftFork.grabFork();
        rightFork.grabFork();
        timeSpentWaiting += System.currentTimeMillis() - waitingStart;
        System.out.println("Philosopher " + id + ": eating...");
        Thread.sleep(100);
        dishesToEat--;
        System.out.println("Philosopher " + id + ": finished eating. Dishes left to eat: " + dishesToEat);
        leftFork.freeFork();
        rightFork.freeFork();
        table.freePlace();
        System.out.println("Philosopher " + id + ": forks released and freed up the seat");
    }

    public double getTimeSpentWaiting() {
        return timeSpentWaiting;
    }

    public int getId() {
        return id;
    }
}
