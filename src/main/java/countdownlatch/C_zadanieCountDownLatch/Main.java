package countdownlatch.C_zadanieCountDownLatch;

import fabryczkapomocnicza.MyThreadFactory;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadLocalRandom;

/**
 * <p>Czas na pierwsze większe zadanko dla Ciebie :) Będzie ono bardzo podobne do zadania z przykładu,
 * jednak dla własnego dobra staraj się tam nie zaglądać. Użyj CountDownLatch, poczytaj dokumentację jeżeli
 * nie jesteś czegoś pewien. Jeżeli zrozumiałeś koncept, z zadaniem powinieneś sobie poradzić sam :)</p>
 *
 * @author Kacper Staszek
 * @author Marcin Ogorzałek
 */
//TODO: Napisz program, który symuluje NIELEGALNE WYSCIGI W LA! Coś jak Need For Speed ;D
// 5 kierowców otrzymało smsa z miejscem rozpoczęcia, czym prędzej jadą na miejsce! Jednak każdy jest w innym
// miejscu miasta, więc czas dotarcia będzie różny. Dopiero kiedy wszyscy pojawią się na miejscu - wyścig ruszy!
// Skorzystaj śmiało z fabryki wątków, którą umieściłem w projekcie.
public class Main {

    private int liczbaUczestników;

    Main(int liczbaUczestników) {
        this.liczbaUczestników = liczbaUczestników;
    }

    public static void main(String[] args) {
        Main main = new Main(8);
        MetadaneWyścigu metadaneWyścigu = main.stwórzPotrzebneObiekty();
        main.uruchomWątki(metadaneWyścigu);
    }

    MetadaneWyścigu stwórzPotrzebneObiekty() {

        CountDownLatch latch = new CountDownLatch(liczbaUczestników);
        Wyścig wyścig = new Wyścig(latch);
        ThreadFactory threadFactory = new MyThreadFactory("Kierowca");
        ExecutorService executorService = Executors
            .newFixedThreadPool(liczbaUczestników, threadFactory);

        return new MetadaneWyścigu(latch, wyścig, executorService);
    }

    void uruchomWątki(MetadaneWyścigu doTestów) {

        new Thread(doTestów.wyścig, "Wyścig").start();
        for (int i = 0; i < liczbaUczestników; i++) {
            doTestów.executorService.submit(new Kierowca(doTestów.latch, doTestów.wyścig));
        }
        doTestów.executorService.shutdown();
    }

    class MetadaneWyścigu {

        CountDownLatch latch;
        Wyścig wyścig;
        ExecutorService executorService;

        MetadaneWyścigu(CountDownLatch countDownLatch, Wyścig wyścig,
            ExecutorService executorService) {
            this.latch = countDownLatch;
            this.wyścig = wyścig;
            this.executorService = executorService;
        }
    }
}

class Wyścig implements Runnable {
    private final CountDownLatch latch;
    private final CopyOnWriteArrayList<Kierowca> driversReadyToStart = new CopyOnWriteArrayList<>();

    Wyścig(CountDownLatch countDownLatch) {
        this.latch = countDownLatch;
    }

    @Override
    public void run() {
        organiseRace();
    }

    void organiseRace() {
        try {
            latch.await();
            startRace();
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }

    void startRace() {
        System.out.println("Wszyscy są, ruszamy");
    }

    void addDriver(Kierowca kierowca) {
        driversReadyToStart.add(kierowca);
    }

    int getDriversReadyToStart() {
        return driversReadyToStart.size();
    }

}

class Kierowca implements Runnable {
    private final CountDownLatch latch;
    private final Wyścig wyścig;

    Kierowca(CountDownLatch latch, Wyścig wyścig) {
        this.latch = latch;
        this.wyścig = wyścig;
    }

    @Override
    public void run() {
        System.out.println("Dostałem sms o wyścigu. Wyruszam na start "
                + Thread.currentThread().getName());
        driveToStartLine();
        comeToStartLine();
    }

    void driveToStartLine() {
        System.out.println("Jadę na start " + Thread.currentThread().getName());
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(1000,2000));
        } catch (InterruptedException e) {
            System.err.println("Zepsuło mi się auto " + Thread.currentThread().getName());
        }
    }

    void comeToStartLine() {
        System.out.println("Dojechałem na start i jestem gotowy "
                + Thread.currentThread().getName());
        latch.countDown();
        wyścig.addDriver(this);

    }

}
