package countdownlatch.B_przykładCountdownLatch;

import fabryczkapomocnicza.MyThreadFactory;

import java.util.concurrent.*;

/**
 * <p>Przykładowe zadanko - problem polega na tym, że mamy pociąg do Hogwartu ;D Wyobraźmy sobie, że kilku czarodziejów
 * właśnie otrzymało list, więc lecą ile sił w nogach na pociąg.
 * Konduktor jest na tyle spoko, że zaczeka na wszystkich, zanim ruszy.</p>
 *
 * @see Pociąg Zwróć uwagę na linię 26 w klasie Pociąg.
 *
 * @author Kacper Staszek
 */
public class Main {

    //TODO:Uruchom program kilka razy, prześledź działanie.
    //TODO:Zakomentuj linię 26 w klasie Pociąg, uruchom program i zrozum działanie metody await();

    private static final int ILOSC_ZAREZERWOWANYCH_BILETOW = 10;

    public static void main(String[] args) throws InterruptedException {

        BlockingQueue<Pasażer> pasażerowie = new LinkedBlockingQueue<>();

        Pociąg pociąg = new Pociąg(ILOSC_ZAREZERWOWANYCH_BILETOW, pasażerowie);

        CountDownLatch referencjaCounDownLatchZNaszegoPociągu = pociąg.getCountDownLatch();

        ThreadFactory threadFactory = new MyThreadFactory("Pasażer");

        //TODO: Zastanów się, czy egzekutor może mieć dowolny rozmiar?

        ExecutorService executorService = Executors.newFixedThreadPool(10, threadFactory);

        for (int i = 0; i < ILOSC_ZAREZERWOWANYCH_BILETOW; i++) {
            executorService.submit(new Pasażer(pociąg, referencjaCounDownLatchZNaszegoPociągu));
        }
        executorService.shutdown();

        pociąg.pociągOdjeżdża();
    }
}

