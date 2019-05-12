package cyclicbarrier.D_wprowadzenieBariera;

import fabryczkapomocnicza.MyThreadFactory;

import java.util.concurrent.*;

/**
 * Opis CyclicBarrier
 *
 * @see CyclicBarrier
 * Bariera działa bardzo podobnie do CountDownLatch, ale jest wielokrotnego użytku. Działa cyklicznie.
 * Możemy jej używać, kiedy mamy powtarzające się zadania, a któreś z nich musi poczekać na inne, zanim się rozpocznie.
 * Jeżeli zrozumiałeś mniej więcej koncept CountDownLatch - to powinno pójść gładko :)
 * W konstruktorze przekazujemy ilość zadań do zakończenia, jako drugi argument do bariery,
 * możemy przekazać jakieś zadanie (Runnable),
 * które uruchomi się za każdym razem, kiedy wykona się cykl bariery.
 */

public class WprowadzenieBariera {

    private static final int ROZMIAR_BARIERY = 3;

    public static void main(String[] args) {
        //TODO:Uruchom program, przeanalizuj działanie, prześledź kod.

        CyclicBarrier cyclicBarrier =
                new CyclicBarrier(ROZMIAR_BARIERY, () -> {
                    System.out.println("\033[1;32mCykl się skończył, robię swoje i zaraz od nowa");
                    System.out.print("\033[0m");
                    //te dziwne cyferki to tylko zmiana kolorków, żeby działanie programu było lepiej widoczne
                });
        //TODO: Zastanów się, czy rozmiar egzekutora wątków jest dowolny.
        ExecutorService executorService = Executors.newFixedThreadPool(3, new MyThreadFactory("Zadanko Cykliczna Bariera"));
        for (int i = 0; i < 1000; i++) {
            executorService.submit(new Task(cyclicBarrier));
        }
        executorService.shutdown();
    }
}

class Task implements Runnable {
    private final CyclicBarrier barrier;

    Task(CyclicBarrier barrier) {
        this.barrier = barrier;
    }

    @Override
    public void run() {
        System.out.println("Robię swoje rzeczy, ktoś tam na mnie czeka");
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(5000)); //Symulujemy, że zadanie zajmuje troszkę czasu
        } catch (InterruptedException ignored) {
            System.err.println(ignored.getMessage());
        }
        try {
            System.out.println("Skończyłem " + Thread.currentThread().getName());
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException ignored) {
            System.err.println(ignored.getMessage());
        }
    }
}
