import java.util.concurrent.ThreadLocalRandom;

public class StarvingPhilosopher implements IPhilosopher {
    private final int id;
    private final Fork leftFork;
    private final Fork rightFork;
    private int dishesToEat;
    private long timeSpentWaiting;


    public StarvingPhilosopher(int id, Fork leftFork, Fork rightFork, int dishesToEat) {
        this.id = id;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
        this.dishesToEat = dishesToEat;
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
        var hasForks = false;
        System.out.println("Philosopher " + id + ": trying to get forks...");
        long waitingStart = System.currentTimeMillis();
        while (!hasForks) {
            leftFork.grabFork();
            if (!rightFork.isFree()) {
                leftFork.freeFork();
            } else {
                rightFork.grabFork();
                hasForks = true;
            }
        }
        timeSpentWaiting += System.currentTimeMillis() - waitingStart;
        System.out.println("Philosopher " + id + ": eating...");
        Thread.sleep(100);
        dishesToEat--;
        System.out.println("Philosopher " + id + ": finished eating. Dishes left to eat: " + dishesToEat);
        leftFork.freeFork();
        rightFork.freeFork();
        System.out.println("Philosopher " + id + ": forks released");
    }

    public double getTimeSpentWaiting() {
        return timeSpentWaiting;
    }

    public int getId() {
        return id;
    }
}
