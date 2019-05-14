package countdownlatch.C1_zadanieCountDownLatch;

import fabryczkapomocnicza.MyThreadFactory;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * Witamy w zadaniu drugim z CountDownLatch. Po zdobyciu podstaw przedstawionych w poprzednich
 * przykładach powinno Ci ono pójść jak z płatka.
 *
 * @author Marcin Ogorzalek
 */

public class Main {
  // TODO: Michale wraz z kompanami obrabia jubilera. Policja już jedzie na miejsce zdarzenia
  // TODO: i kierowca ekipy nie może dłużej czekać
  // TODO: Napisz program który to sumuluje. Czas oczekiwania kierowcy nie może być dłuższy jak 5000 ms
  // TODO: podczas gdy chłopaki moga obrabiać jubilera nawet do 10_000 ms.
  // TODO: Kto zdąży ten ucieknie, kto nie - idzie do paki.
  // TODO: Skorzystaj śmiało z fabryki wątków, którą umieściłem w projekcie. 

  static final int ILOSC_LUDZI_W_EKIPIE = 4;

  public static void main(String[] args) {

    CountDownLatch latch = new CountDownLatch(ILOSC_LUDZI_W_EKIPIE);

    ExecutorService executorService = Executors.newFixedThreadPool(ILOSC_LUDZI_W_EKIPIE,
        new MyThreadFactory("Członek ekipy"));

    kierowcaPodjeżdzaNaRobotę(latch, executorService);
    Więzienie więzienie = new Więzienie();
    ekipaZaczynaObrabiaćJubilera(latch, executorService, więzienie);
  }

  private static void kierowcaPodjeżdzaNaRobotę(CountDownLatch latch,
      ExecutorService executorService) {
    ExecutorService executor = Executors.newSingleThreadExecutor();
    executor.submit(new Kierowca(latch, executorService, 5000));
    executor.shutdown();
  }

  private static void ekipaZaczynaObrabiaćJubilera(CountDownLatch latch,
      ExecutorService executorService, Więzienie więzienie) {
    for (int i = 0; i < ILOSC_LUDZI_W_EKIPIE; i++) {
      executorService.submit(new CzłonekEkipy(latch, więzienie));
    }
    executorService.shutdown();
  }
}

class Kierowca implements Runnable {

  private final CountDownLatch latch;
  private final ExecutorService executorService;
  private final TimeUnit timeUnit = TimeUnit.MILLISECONDS;
  private final long timeout;

  public Kierowca(CountDownLatch latch, ExecutorService executorService, long timeout) {
    this.latch = latch;
    this.executorService = executorService;
    this.timeout = timeout;
  }

  @Override
  public void run() {
    System.out.println("Szybko chłopaki, do samochodzu. Policja jest w drodze");
    ucieczka();
  }

  void ucieczka() {
    try {
      latch.await(timeout, timeUnit);
      executorService.shutdownNow();
      System.out.println("No najwyższy czas");
    } catch (InterruptedException e) {
      System.err.println("Dopadli nas!");
    }
  }
}

class CzłonekEkipy implements Runnable {

  private final CountDownLatch latch;
  private final Więzienie więzienie;

  CzłonekEkipy(CountDownLatch latch, Więzienie więzienie) {
    this.latch = latch;
    this.więzienie = więzienie;
  }


  @Override
  public void run() {
    kradzież();
  }

  void kradzież() {
    System.out.println(Thread.currentThread().getName() + " Chwila, zagarniam jeszcze łupy!!");
    try {
      Thread.sleep(ThreadLocalRandom.current().nextInt(10_000));
      czasUciekać();
    } catch (InterruptedException e) {
      System.err.println(Thread.currentThread().getName() + " Dostałem kulkę! Już po mnie");
      więzienie.złapZłodzieja(this);
    }
  }

  void czasUciekać() {
    System.out.println(Thread.currentThread().getName() + " Mam wszystko, czas się zwijać.");
    latch.countDown();
  }
}

class Więzienie {
  private final CopyOnWriteArrayList<CzłonekEkipy> więźniowie = new CopyOnWriteArrayList<>();

  void złapZłodzieja(CzłonekEkipy członekEkipy) {
    System.out.println("Bandzior złapany");
    więźniowie.add(członekEkipy);
  }

  public CopyOnWriteArrayList<CzłonekEkipy> getWięźniowie() {
    return więźniowie;
  }
}
