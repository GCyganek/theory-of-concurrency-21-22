import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Demo {
    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter philosophers number, dishes number and mode (0 - starving, 1 - waiter): ");

        int philosophersNumber = scanner.nextInt();
        int dishesNumber = scanner.nextInt();
        int mode = scanner.nextInt();

        List<IPhilosopher> philosophers = new ArrayList<>();
        List<Fork> forks = new ArrayList<>();

        for (int i = 0; i < philosophersNumber; i++) {
            forks.add(new Fork(i));
        }

        if (mode == 0) {
            for (int i = 0; i < philosophersNumber; i++) {
                philosophers.add(new StarvingPhilosopher(i, forks.get(i),
                                    forks.get((i + 1) % philosophersNumber), dishesNumber));
            }
        } else {
            Table table = new Table(philosophersNumber);
            for (int i = 0; i < philosophersNumber; i++) {
                philosophers.add(new LimitedSeatsPhilosopher(i, forks.get(i),
                                    forks.get((i + 1) % philosophersNumber), dishesNumber, table));
            }
        }

        List<Thread> threads = new ArrayList<>();

        for (IPhilosopher philosopher: philosophers) {
            threads.add(new Thread(() -> {
                while (!philosopher.ateAllDishes()) {
                    try {
                        philosopher.think();
                        philosopher.eat();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }));
        }

        for (Thread thread: threads) {
            thread.start();
        }

        for (Thread thread: threads) {
            thread.join();
        }

        System.out.println("\n\nFeast has ended\n\n");

        for (IPhilosopher philosopher: philosophers) {
            double averageTimeSpentWaiting = philosopher.getTimeSpentWaiting() / dishesNumber;
            System.out.println("Philosopher " + philosopher.getId() + ": time spent waiting "
                    + philosopher.getTimeSpentWaiting() + " average time spent waiting " + averageTimeSpentWaiting);
        }
    }
}
