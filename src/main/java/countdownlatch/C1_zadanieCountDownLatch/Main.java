package countdownlatch.C1_zadanieCountDownLatch;

import fabryczkapomocnicza.MyThreadFactory;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * Witamy w zadaniu drugim z CountDownLatch.
 * Ma ono na celu pokazdanie możliwości ustawienia czasu oczekiwania na CountDownLatch.
 * Po zdobyciu podstaw przedstawionych w poprzednich
 * przykładach powinno Ci ono pójść jak z płatka.
 * @see CountDownLatch
 *
 *
 * @author Marcin Ogorzalek
 */
// TODO: Michał wraz z kompanami obrabia jubilera. Policja już jedzie na miejsce zdarzenia
//  i kierowca ekipy nie może dłużej czekać
//  Napisz program który to sumuluje. Czas oczekiwania kierowcy nie może być dłuższy niż 5000 ms
//  podczas gdy chłopaki moga obrabiać jubilera nawet w 10_000 ms.
//  Kto zdąży ten ucieknie, kto nie - idzie do paki.
//  Skorzystaj śmiało z fabryki wątków, którą umieściłem w projekcie.

public class Main {
  private int ilośćLudziWEkipie;
  private long czasDoPrzyjazduPolicji;
  private int maksymalnyCzasRoboty;


  Main(int ilośćLudziWEkipie, long czasDoPrzyjazduPolicji, int maksymalnyCzasRoboty) {
    this.ilośćLudziWEkipie = ilośćLudziWEkipie;
    this.czasDoPrzyjazduPolicji = czasDoPrzyjazduPolicji;
    this.maksymalnyCzasRoboty = maksymalnyCzasRoboty;
  }

  public static void main(String[] args) {
    Main main = new Main(4, 5000, 10_000);
    main.uruchamianieWątków(main.tworzeniePotrzebnychObiektów());
  }

  MetadaneNapadu tworzeniePotrzebnychObiektów() {
    CountDownLatch latch = new CountDownLatch(ilośćLudziWEkipie);
    ExecutorService kierowcaExecutor = Executors.newSingleThreadExecutor();
    ExecutorService ekipaExecutor = Executors.newFixedThreadPool(ilośćLudziWEkipie,
        new MyThreadFactory("Członek ekipy"));
    Więzienie więzienie = new Więzienie();
    return new MetadaneNapadu(latch, kierowcaExecutor, ekipaExecutor, więzienie);
  }

  void uruchamianieWątków(MetadaneNapadu metadaneNapadu) {
    metadaneNapadu.kierowcaExecutor.submit(new Kierowca(metadaneNapadu.latch,
        metadaneNapadu.ekipaExecutor, czasDoPrzyjazduPolicji));
    metadaneNapadu.kierowcaExecutor.shutdown();

    for (int i = 0; i < ilośćLudziWEkipie; i++) {
      metadaneNapadu.ekipaExecutor.submit(new CzłonekEkipy(metadaneNapadu.latch,
          metadaneNapadu.więzienie, maksymalnyCzasRoboty));
    }
    metadaneNapadu.ekipaExecutor.shutdown();
  }

  class MetadaneNapadu {
    CountDownLatch latch;
    ExecutorService kierowcaExecutor;
    ExecutorService ekipaExecutor;
    Więzienie więzienie;

     MetadaneNapadu(CountDownLatch latch, ExecutorService kierowcaExecutor,
        ExecutorService ekipaExecutor, Więzienie więzienie) {
      this.latch = latch;
      this.kierowcaExecutor = kierowcaExecutor;
      this.ekipaExecutor = ekipaExecutor;
      this.więzienie = więzienie;
    }
  }
}

class Kierowca implements Runnable {

  private final CountDownLatch latch;
  private final ExecutorService ekipaExecutor;
  private final TimeUnit timeUnit = TimeUnit.MILLISECONDS;
  private final long czasDoPrzyjazduPolicji;

  Kierowca(CountDownLatch latch, ExecutorService ekipaExecutor, long czasDoPrzyjazduPolicji) {
    this.latch = latch;
    this.ekipaExecutor = ekipaExecutor;
    this.czasDoPrzyjazduPolicji = czasDoPrzyjazduPolicji;
  }

  @Override
  public void run() {
    System.out.println("Szybko chłopaki, do samochodzu. Policja jest w drodze");
    ucieczka();
  }

  void ucieczka() {
    try {
      latch.await(czasDoPrzyjazduPolicji, timeUnit);
      ekipaExecutor.shutdownNow();
      System.out.println("No najwyższy czas");
    } catch (InterruptedException e) {
      System.err.println("Dopadli nas!");
    }
  }
}

class CzłonekEkipy implements Runnable {

  private final CountDownLatch latch;
  private final Więzienie więzienie;
  private final int maksymalnyCzasRoboty;

  CzłonekEkipy(CountDownLatch latch, Więzienie więzienie, int maksymalnyCzasRoboty) {
    this.latch = latch;
    this.więzienie = więzienie;
    this.maksymalnyCzasRoboty = maksymalnyCzasRoboty;
  }

  @Override
  public void run() {
    kradzież();
  }

  void kradzież() {
    System.out.println(Thread.currentThread().getName() + " Chwila, zagarniam jeszcze łupy!!");
    try {
      Thread.sleep(ThreadLocalRandom.current().nextInt(maksymalnyCzasRoboty));
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
    więźniowie.add(członekEkipy);
    System.out.println("Bandzior złapany");
  }

  CopyOnWriteArrayList<CzłonekEkipy> getWięźniowie() {
    return więźniowie;
  }
}
